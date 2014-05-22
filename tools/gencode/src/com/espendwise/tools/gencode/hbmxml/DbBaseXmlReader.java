package com.espendwise.tools.gencode.hbmxml;

import com.espendwise.tools.gencode.dbxml.Database;
import com.espendwise.tools.gencode.dbxml.FKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.cfg.reveng.*;
import org.hibernate.mapping.*;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DbBaseXmlReader {

    private static final int ZERO = 0;

    private static enum DIALECTS {
        ORACLE("org.hibernate.dialect.OracleDialect", "org.hibernate.dialect.Oracle8iDialect", "org.hibernate.dialect.Oracle9Dialect", "org.hibernate.dialect.Oracle9iDialect", "org.hibernate.dialect.Oracle10gDialect"),
        POSTGRES("org.hibernate.dialect.PostgresPlusDialect","org.hibernate.dialect.PostgreSQLDialect");
        private Set<String> sets;
        DIALECTS(String... s) {  sets = new HashSet<String>(Arrays.asList(s)); }
        public boolean contains(String s) {    return sets.contains(s); }
    }
    
  private static final Log log = LogFactory.getLog(DbBaseXmlReader.class);

    private final ReverseEngineeringStrategy revengStrategy;
    private Database metadata;
    private String dialect;

    public DbBaseXmlReader(Database metadata, String dialect, ReverseEngineeringStrategy reveng) {
        this.metadata = metadata;
        this.revengStrategy = reveng;
        this.dialect =  dialect;
    }

    public List<Table> readDatabaseSchema(DatabaseCollector dbs) throws Exception {

        ReverseEngineeringRuntimeInfo info = ReverseEngineeringRuntimeInfo.createInstance(null, null, dbs);
        revengStrategy.configure(info);

        Set<Table> hasIndices = new HashSet<Table>();

        List<Table> processedTables = processTables(dbs, hasIndices);

        for (Table table : processedTables) {
            processBasicColumns(table);
            processPrimaryKey(table);
            if (hasIndices.contains(table)) {
                processIndices(table);
            }
        }

        Iterator<Table> tables = processedTables.iterator();//dbs.iterateTables();
        Map<Object, List<Object>> oneToManyCandidates = resolveForeignKeys(dbs, tables);

        dbs.setOneToManyCandidates(oneToManyCandidates);

        return processedTables;

    }

    
    private Map<Object, List<Object>> resolveForeignKeys(DatabaseCollector dbs, Iterator<Table> tables) throws Exception {
        
        List<ForeignKeysInfo> fks = new ArrayList<ForeignKeysInfo>();
       
        while ( tables.hasNext() ) {
            Table table =  tables.next();
            ForeignKeysInfo foreignKeys = processForeignKeys(dbs, table);
            fks.add( foreignKeys );
        }

        Map<Object, List<Object>> oneToManyCandidates = new HashMap<Object, List<Object>>();
        for (ForeignKeysInfo element : fks) {
            Map<Object, List<Object>> map = element.process(revengStrategy); // the actual foreignkey is created here.
            HbmAssist.mergeMultiMap(oneToManyCandidates, map);
        }
        
        return oneToManyCandidates;
    }

    static class ForeignKeysInfo {

        final Map<String, Table> dependentTables;
        final Map<String, List<Column>> dependentColumns;
        final Map<String, List<Column>> referencedColumns;
        private final Table referencedTable;

        public ForeignKeysInfo(Table referencedTable, Map<String, Table> tables, Map<String, List<Column>> columns, Map<String, List<Column>> refColumns) {
            this.referencedTable = referencedTable;
            this.dependentTables = tables;
            this.dependentColumns = columns;
            this.referencedColumns = refColumns;
        }

        Map<Object, List<Object>> process(ReverseEngineeringStrategy revengStrategy) {
            Map<Object, List<Object>> oneToManyCandidates = new HashMap<Object, List<Object>>();
            for (Map.Entry<String, Table> entry : dependentTables.entrySet()) {
                String fkName = entry.getKey();
                Table fkTable = entry.getValue();
                List<Column> columns = dependentColumns.get(fkName);
                List<Column> refColumns = referencedColumns.get(fkName);

                String className = revengStrategy.tableToClassName(TableIdentifier.create(referencedTable));

                ForeignKey key = fkTable.createForeignKey(fkName, columns, className, refColumns);
                key.setReferencedTable(referencedTable);

                HbmAssist.addToMultiMap(oneToManyCandidates, className, key);
            }
            // map<className, foreignkey>
            return oneToManyCandidates;
        }
    }

    protected ForeignKeysInfo processForeignKeys(DatabaseCollector dbs, Table referencedTable) throws Exception {

        com.espendwise.tools.gencode.dbxml.Table xtm = selectXmlTableModel(getMetaData(), referencedTable);
        if (xtm == null) {
            log.debug("processForeignKeys()=> Skiping ..., XML table model not found for table '" + referencedTable.getName() + "'");
            return null;
        }

        log.debug("processForeignKeys()=> Finding exported foreignkeys on " + referencedTable.getName());

        List<com.espendwise.tools.gencode.dbxml.FKey> foreignKeyModels = selectForeignKeyModel(getMetaData(), xtm);

        log.debug("processForeignKeys()=> Found " + foreignKeyModels.size() + " fkeys for table " + referencedTable.getName());

        Map<String, List<Column>> dependentColumns = new HashMap<String, List<Column>>();

        // foreign key name to Table
        Map<String, Table> dependentTables = new HashMap<String, Table>();
        Map<String, List<Column>> referencedColumns = new HashMap<String, List<Column>>();

        for (com.espendwise.tools.gencode.dbxml.FKey fkm : foreignKeyModels) {

            String fkCatalog = null;
            String fkSchema = null;
            String fkTableName = fkm.getFkTable();
            String fkColumnName = fkm.getFKeyRef().getFkColumnName();
            String fkName = fkm.getName();

            Table fkTable = dbs.getTable(fkSchema, fkCatalog, fkTableName);
            if (fkTable == null) {
                //	filter out stuff we don't have tables for!
                log.debug("processForeignKeys()=> Foreign key " + fkName + " references unknown or filtered table " + Table.qualify(fkCatalog, fkSchema, fkTableName));
                continue;
            }


            if (fkName == null) {
                throw new Exception("Foreign key name not defined");
            }

            List<Column> depColumns = dependentColumns.get(fkName);
            if (depColumns == null) {
                depColumns = new ArrayList<Column>();
                dependentColumns.put(fkName, depColumns);
                dependentTables.put(fkName, fkTable);
            } else {
                Object previousTable = dependentTables.get(fkName);
                if (fkTable != previousTable) {
                    throw new Exception("Foreign key name (" + fkName + ") mapped to different tables! previous: " + previousTable + " current:" + fkTable);
                }
            }

            Column column = new Column(fkColumnName);
            Column existingColumn = fkTable.getColumn(column);
            column = existingColumn == null ? column : existingColumn;

            depColumns.add(column);
        }


        List userForeignKeys = revengStrategy.getForeignKeys(TableIdentifier.create(referencedTable));
        if (userForeignKeys != null) {
            for (Object userForeignKey : userForeignKeys) {

                ForeignKey element = (ForeignKey) userForeignKey;

                if (!HbmAssist.equalTable(referencedTable, element.getReferencedTable())) {
                    log.debug("processForeignKeys()=> Referenced table " + element.getReferencedTable().getName() + " is not " + referencedTable + ". Ignoring userdefined foreign key " + element);
                    continue; // skip non related foreign keys
                }

                String userfkName = element.getName();
                Table userfkTable = element.getTable();

                List userColumns = element.getColumns();
                List userrefColumns = element.getReferencedColumns();

                 Table deptable =  dependentTables.get(userfkName);
                if (deptable != null) { // foreign key already defined!?
                    throw new MappingException("Foreign key " + userfkName + " already defined in the database!");
                }

                deptable = dbs.getTable(userfkTable.getSchema(), userfkTable.getCatalog(), userfkTable.getName());
                if (deptable == null) {
                    //	filter out stuff we don't have tables for!
                    log.debug("processForeignKeys()=> User defined foreign key " + userfkName + " references unknown or filtered table " + TableIdentifier.create(userfkTable));
                    continue;
                }

                dependentTables.put(userfkName, deptable);

                List<Column> depColumns = new ArrayList<Column>(userColumns.size());
                Iterator colIterator = userColumns.iterator();
                while (colIterator.hasNext()) {
                    Column jdbcColumn = (Column) colIterator.next();
                    Column column = new Column(jdbcColumn.getName());
                    Column existingColumn = deptable.getColumn(column);
                    column = existingColumn == null ? column : existingColumn;
                    depColumns.add(column);
                }

                List<Column> refColumns = new ArrayList<Column>(userrefColumns.size());
                colIterator = userrefColumns.iterator();
                while (colIterator.hasNext()) {
                    Column jdbcColumn = (Column) colIterator.next();
                    Column column = new Column(jdbcColumn.getName());
                    Column existingColumn = referencedTable.getColumn(column);
                    column = existingColumn == null ? column : existingColumn;
                    refColumns.add(column);
                }

                referencedColumns.put(userfkName, refColumns);
                dependentColumns.put(userfkName, depColumns);
            }
        }


        return new ForeignKeysInfo(referencedTable, dependentTables, dependentColumns, referencedColumns);

    }



    private void processPrimaryKey(Table table) {

        com.espendwise.tools.gencode.dbxml.Table xtm = selectXmlTableModel(getMetaData(), table);
        if (xtm == null) {
            log.debug("processPrimaryKey()=> Skiping ..., XML table model not found for table '" + table.getName() + "'");
            return;
        }


        com.espendwise.tools.gencode.dbxml.PrimaryKey primaryKeyModel = xtm.getPrimaryKey();
        if (primaryKeyModel == null) {
            log.debug("processPrimaryKey()=> Skiping ..., Primary key not found for table '" + table.getName() + "'");
            return;
        }

        com.espendwise.tools.gencode.dbxml.Column primaryKeyColumnModel = selectXmlPrimaryColumnModel(xtm);
        if (primaryKeyColumnModel == null) {
            log.debug("processPrimaryKey()=> Skiping ..., Primary column not found for table '" + table.getName() + "'");
            return;
        }

        String columnName = primaryKeyColumnModel.getName();
        String name = primaryKeyModel.getName();

        PrimaryKey key = new PrimaryKey();
        key.setName(name);
        key.setTable(table);
        key.addColumn(getColumn(table, columnName));

        table.setPrimaryKey(key);

        log.debug("processPrimaryKey()=> primary key for " + table + " -> " + key + " bound");
        log.debug("processPrimaryKey()=> END.");
    }

    private List<Table> processTables(DatabaseCollector dbs, Set<Table> hasIndices) {

        log.info("processTables()=> BEGIN");
        
        List<Table> processedTables = new ArrayList<Table>();
        
        List<com.espendwise.tools.gencode.dbxml.Table> tables = getMetaData().getTable();

        log.info("processTables()=> Tables Size : " + tables.size());

        for (com.espendwise.tools.gencode.dbxml.Table table : tables) {

            String schemaName = null;
            String catalogName = null;
            String tableName = table.getName();

            log.debug("processTables()=> processing  ' " + tableName + " ' ...");

            TableIdentifier ti = new TableIdentifier(catalogName, schemaName, tableName);

            if (!excludeTable(ti)) {
                Table mappingTable = dbs.addTable(HbmAssist.quote(getSchemaForModel(ti.getSchema())), getCatalogForModel(ti.getCatalog()), HbmAssist.quote(ti.getName()));
                log.info("processTables()=> Table  ' " + tableName + " '  indices");
                hasIndices.add(mappingTable);
                processedTables.add(mappingTable);
            } else {
                log.debug("processTables()=> Table " + ti + " excluded by strategy");
            }

        }


        log.info("processTables()=> END, Processed Tables: "+processedTables.size());
       
        return processedTables;
    
    }


    private void processBasicColumns(Table table) {

        log.debug("processBasicColumns()=> BEGIN");

        com.espendwise.tools.gencode.dbxml.Table xtm = selectXmlTableModel(getMetaData(), table);
        if (xtm == null) {
            log.debug("processBasicColumns()=> Skiping ..., XML table model not found for table '" + table.getName() + "'");
            return;
        }

        String qualify = Table.qualify(table.getCatalog(), table.getSchema(), table.getName());
        log.debug("processBasicColumns()=> Table: " + table.getName() + ", Qualify: " + qualify);


        List<com.espendwise.tools.gencode.dbxml.Column> columnModels = xtm.getColumn();
        log.debug("processBasicColumns()=> Columns Size: " + columnModels.size());


        for (com.espendwise.tools.gencode.dbxml.Column cm : columnModels) {

            String sqlTypeName = cm.getType();
            String columnName = cm.getName();

            boolean isNullable = HbmAssist.isTrue(cm.getNullable());

            DbBaseType2SqlType.SqlType sqlType = HbmAssist.getSqlType(createSQlTypeSettings(), sqlTypeName);
            Integer size = cm.getSize() != null ? HbmAssist.toInt(cm.getSize()) : null;
            Integer decimalDigits = cm.getDigits() != null ? HbmAssist.toInt(cm.getDigits()) : null;


            TableIdentifier ti = TableIdentifier.create(table);

            log.debug("processBasicColumns()=> processing column: " + ti + "." + columnName +
                    "(" +
                    "sqlTypeName:" + sqlTypeName + ", " +
                    "sqlType:" + sqlType.getSqlTypeCode() + ", " +
                    "isNullable:" + isNullable + ", " +
                    "size: " + size + ", " +
                    "decimalDigits: " + decimalDigits
                    + ")");

            if (!excludeColumn(ti, columnName)) {

                Column column = new Column();
                column.setName(HbmAssist.quote(columnName));
                column.setNullable(isNullable);
                column.setSqlTypeCode(sqlType.getSqlTypeCode());

                if (sqlType.getDefinitonType() != null) {
                    column.setSqlType(sqlType.getDefinitonType());
                }

                if (size != null && HbmAssist.intBounds(size)) {
                    if (JDBCToHibernateTypeHelper.typeHasLength(sqlType.getSqlTypeCode())) {
                        column.setLength(size);
                    }
                    if (JDBCToHibernateTypeHelper.typeHasScaleAndPrecision(sqlType.getSqlTypeCode())) {
                        column.setPrecision(size);
                    }
                }

                if (JDBCToHibernateTypeHelper.typeHasScaleAndPrecision(sqlType.getSqlTypeCode())) {
                    if (decimalDigits == null) {
                        column.setScale(ZERO);
                    } else if (HbmAssist.intBounds(decimalDigits)) {
                        column.setScale(decimalDigits);
                    }
                }

                table.addColumn(column);


            } else {

                log.debug("processBasicColumns()=> Column " + ti + "." + columnName + " excluded by strategy");

            }


        }

        log.debug("processBasicColumns()=> END.");

    }

    private DbBaseTypeSettings createSQlTypeSettings() {
         if(DIALECTS.ORACLE.contains(getDialect())){
              return  new OracleSqlTypeSettings();
         } else  if(DIALECTS.POSTGRES.contains(getDialect())) {
             return new PostgresSqlTypeSettings();
         } else  {
            return null;
         }
     }

    private void processIndices(Table table) throws Exception {

        log.debug("processIndices()=> BEGIN");

        com.espendwise.tools.gencode.dbxml.Table xtm = selectXmlTableModel(getMetaData(), table);
        if (xtm == null) {
            log.debug("processIndices()=> Skiping ..., XML table model not found for table '" + table.getName() + "'");
            return;
        }

        List<com.espendwise.tools.gencode.dbxml.Index> indexesModels = selectXmlIndexModel(getMetaData(), xtm);

        log.debug("processIndices()=> found "+ indexesModels.size()+ "index for table "+table.getName());

        Map<String, Index> indexes = new HashMap<String, Index>(); // indexname (String) -> Index

        for (com.espendwise.tools.gencode.dbxml.Index im : indexesModels) {

            String indexName = im.getName();
            String columnName = im.getColumnref().getName();

            log.debug("processIndices()=> index '"+indexName+"' processing ... , Columnref "+columnName);

            if (columnName != null || indexName != null) {

                Index index = indexes.get(indexName);
                if (index == null) {
                    index = new Index();
                    index.setName(indexName);
                    index.setTable(table);
                    table.addIndex(index);
                    indexes.put(indexName, index);
                }

                Column column = getColumn(table, columnName);
                index.addColumn(column);
            }

        }
    }

    private com.espendwise.tools.gencode.dbxml.Table selectXmlTableModel(Database metadata, Table table) {
        for(com.espendwise.tools.gencode.dbxml.Table t : metadata.getTable()){
            if(t.getName().equalsIgnoreCase(table.getName())){
                return t;
            }
        }
        return null;
    }

    private com.espendwise.tools.gencode.dbxml.Column selectXmlPrimaryColumnModel(com.espendwise.tools.gencode.dbxml.Table table) {
        for (com.espendwise.tools.gencode.dbxml.Column c : table.getColumn()) {
            if (HbmAssist.isTrue(c.getIsPrimaryKey())) {
                return c;
            }
        }
        return null;
    }


    private List<FKey> selectForeignKeyModel(Database metadata, com.espendwise.tools.gencode.dbxml.Table table) {
        List<FKey> fklist = new ArrayList<FKey>();
        for (FKey fk : metadata.getFkey()) {
            if (fk.getPkTable().equalsIgnoreCase(table.getName())) {
                fklist.add(fk);
            }
        }
        return fklist;
    }


    private List<com.espendwise.tools.gencode.dbxml.Index> selectXmlIndexModel(Database metadata, com.espendwise.tools.gencode.dbxml.Table pTable) {
        List<com.espendwise.tools.gencode.dbxml.Index> ilist = new ArrayList<com.espendwise.tools.gencode.dbxml.Index>();
        for (com.espendwise.tools.gencode.dbxml.Index i : metadata.getIndex()) {
            if (i.getTable().equalsIgnoreCase(pTable.getName())) {
                ilist.add(i);
            }
        }
        return ilist;
    }

    private Column getColumn(Table table, String columnName) {
        Column column = new Column();
        column.setName(HbmAssist.quote(columnName));
        Column existing = table.getColumn(column);
        if(existing!=null) {
            column = existing;
        }
        return column;
    }

    protected String getCatalogForModel(String catalog) {
        if(catalog==null) return null;
        return catalog;
    }

     protected String getSchemaForModel(String schema) {
        if(schema==null) return null;
       return schema;
    }

    public String getDialect() {
        return dialect;
    }

    private Database getMetaData() {
        return metadata;
    }

    private boolean excludeTable(TableIdentifier ti) {
        return  revengStrategy.excludeTable(ti) ;
    }

    private boolean excludeColumn(TableIdentifier ti, String columnName) {
        return revengStrategy.excludeColumn(ti, columnName);
    }

}


