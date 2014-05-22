/*
 * Created on 2004-11-23
 *
 */
package com.espendwise.tools.gencode.hbmxml;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.DuplicateMappingException;
import org.hibernate.FetchMode;
import org.hibernate.MappingException;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.reveng.*;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.Versioning;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.*;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.util.JoinedIterator;
import org.hibernate.util.StringHelper;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DbBaseXmlBinder {

     private static final Log log = LogFactory.getLog(DbBaseXmlBinder.class);

    private final Mappings mappings;

    private final DbBaseConfiguration cfg;
    private ReverseEngineeringStrategy revengStrategy;


    public DbBaseXmlBinder(DbBaseConfiguration cfg, Mappings mappings, ReverseEngineeringStrategy revengStrategy) {
        this.cfg = cfg;
        this.mappings = mappings;
        this.revengStrategy = revengStrategy;
    }

 
    public void readFromDatabase(Mapping mapping) {
        try {
            DatabaseCollector collector = readDatabaseSchema();
            createPersistentClasses(collector, mapping); //move this to a different step!
        }  catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public DatabaseCollector readDatabaseSchema() throws Exception {
        DbBaseXmlReader reader = new DbBaseXmlReader(cfg.getDatabase(), cfg.getDialect(), revengStrategy);
        DatabaseCollector dbs = new MappingsDatabaseCollector(mappings);
        reader.readDatabaseSchema(dbs);
        return dbs;
    }


    private void createPersistentClasses(DatabaseCollector collector, Mapping mapping) {

        Map manyToOneCandidates = collector.getOneToManyCandidates();

        for (Iterator iter = mappings.iterateTables(); iter.hasNext();) {
            Table table = (Table) iter.next();
            if (table.getColumnSpan() == 0) {
                log.debug("createPersistentClasses()=> Cannot create persistent class for " + table + " as no columns were found.");
                continue;
            }

            if(revengStrategy.isManyToManyTable(table)) {
                log.debug( "createPersistentClasses()=> Ignoring " + table + " as class since rev.eng. says it is a many-to-many" );
                continue;
            }

            RootClass rc = new RootClass();
            TableIdentifier tableIdentifier = TableIdentifier.create(table);
            String className = revengStrategy.tableToClassName( tableIdentifier );
            rc.setEntityName( className );
            rc.setClassName( className );
            rc.setProxyInterfaceName(rc.getEntityName() );
            rc.setLazy(true);

            rc.setMetaAttributes( safeMeta(revengStrategy.tableToMetaAttributes( tableIdentifier )) );

            rc.setDiscriminatorValue( rc.getEntityName() );
            rc.setTable(table);

            try {
                mappings.addClass(rc);
            } catch(DuplicateMappingException dme) {
                PersistentClass class1 = mappings.getClass(dme.getName());
                Table table2 = class1.getTable();
                throw new BinderException("Duplicate class name '" + rc.getEntityName() + "' generated for '" + table + "'. Same name where generated for '" + table2 + "'");
            }

            mappings.addImport( rc.getEntityName(), rc.getEntityName() );

            Set<Column> processed = new HashSet<Column>();

            bindPrimaryKeyToProperties(table, rc, processed, mapping, collector);
            bindColumnsToVersioning(table, rc, processed, mapping);
            bindOutgoingForeignKeys(table, rc, processed);
            bindColumnsToProperties(table, rc, processed, mapping);
            List incomingForeignKeys = (List) manyToOneCandidates.get( rc.getEntityName() );
            bindIncomingForeignKeys(rc, processed, incomingForeignKeys, mapping);
        }

    }

    private Map safeMeta(Map map) {
        return map == null ? new HashMap() : map;
    }


    private void bindIncomingForeignKeys(PersistentClass rc, Set<Column> processed, List foreignKeys, Mapping mapping) {
        if(foreignKeys!=null) {

            for (Object oForeignKey : foreignKeys) {

                ForeignKey foreignKey = (ForeignKey) oForeignKey;

                if (revengStrategy.excludeForeignKeyAsCollection(
                        foreignKey.getName(),
                        TableIdentifier.create(foreignKey.getTable()),
                        foreignKey.getColumns(),
                        TableIdentifier.create(foreignKey.getReferencedTable()),
                        foreignKey.getReferencedColumns())) {

                    log.debug("bindIncomingForeignKeys()=> Rev.eng excluded one-to-many for foreignkey " + foreignKey.getName());

                } else {

                    Property property = bindOneToMany(rc, foreignKey, processed, mapping);
                    rc.addProperty(property);
                }
            }
        }
    }


    private Property bindManyToOne(String propertyName, Table table, ForeignKey fk, Set<Column> processedColumns) {

        ManyToOne value = new ManyToOne(table);

        value.setReferencedEntityName( fk.getReferencedEntityName() );

        Iterator columns = fk.getColumnIterator();

        while ( columns.hasNext() ) {
            Column fkcolumn = (Column) columns.next();
            checkColumn(fkcolumn);
            value.addColumn(fkcolumn);
            processedColumns.add(fkcolumn);
        }

        value.setFetchMode(FetchMode.SELECT);

        return makeProperty(
                TableIdentifier.create(table),
                propertyName,
                value,
                true,
                true,
                value.getFetchMode()!=FetchMode.JOIN,
                null,
                null
        );

    }


    private Property bindOneToMany(PersistentClass rc, ForeignKey foreignKey, Set<Column> processed, Mapping mapping) {

        Table collectionTable = foreignKey.getTable();

        Collection collection = new org.hibernate.mapping.Set(rc);

        collection.setCollectionTable(collectionTable); // CHILD+

        boolean manyToMany = revengStrategy.isManyToManyTable( collectionTable );
        if(manyToMany) {
            log.debug("bindOneToMany(0=> Rev.eng said here is a many-to-many");
        }


        if(manyToMany) {

            ManyToOne element = new ManyToOne( collection.getCollectionTable() );
            Iterator foreignKeyIterator = foreignKey.getTable().getForeignKeyIterator();
            List keys = new ArrayList();
            while ( foreignKeyIterator.hasNext() ) {
                Object next = foreignKeyIterator.next();
                if(next!=foreignKey) {
                    keys.add(next);
                }
            }

            if(keys.size()>1) {
                throw new BinderException("more than one other foreign key to choose from!");
            }

            ForeignKey fk = (ForeignKey) keys.get(0);

            String tableToClassName = bindCollection( rc, foreignKey, fk, collection );

            element.setReferencedEntityName( tableToClassName );
            element.addColumn( fk.getColumn( 0 ) );
            collection.setElement( element );

        } else {

            String tableToClassName = bindCollection( rc, foreignKey, null, collection );

            OneToMany oneToMany = new OneToMany( collection.getOwner() );

            oneToMany.setReferencedEntityName( tableToClassName ); // Child
            mappings.addSecondPass( new DbBaseCollectionSecondPass(mappings, collection) );

            collection.setElement(oneToMany);
        }

        // bind keyvalue
        KeyValue referencedKeyValue;
        String propRef = collection.getReferencedPropertyName();
        if (propRef==null) {
            referencedKeyValue = collection.getOwner().getIdentifier();
        } else {
            referencedKeyValue = (KeyValue) collection.getOwner()
                    .getProperty(propRef)
                    .getValue();
        }

        SimpleValue keyValue = new DependantValue( collectionTable, referencedKeyValue );
        //key.setCascadeDeleteEnabled( "cascade".equals( subnode.attributeValue("on-delete") ) );
        Iterator columnIterator = foreignKey.getColumnIterator();
        while ( columnIterator.hasNext() ) {
            Column fkcolumn = (Column) columnIterator.next();
            if(fkcolumn.getSqlTypeCode()!=null) {
                guessAndAlignType(collectionTable, fkcolumn, mapping, false);
            }
            keyValue.addColumn( fkcolumn );
        }

        collection.setKey(keyValue);

        mappings.addCollection(collection);

        return makeProperty(TableIdentifier.create( rc.getTable() ), StringHelper.unqualify( collection.getRole() ), collection, true, true, true, "all", null);

    }

    private String bindCollection(PersistentClass rc, ForeignKey fromForeignKey, ForeignKey toForeignKey, Collection collection) {

        ForeignKey targetKey = fromForeignKey;
        String collectionRole;
        boolean collectionLazy;
        boolean collectionInverse;
        TableIdentifier foreignKeyTable;
        String tableToClassName;

        if(toForeignKey!=null) {
            targetKey = toForeignKey;
        }

        boolean uniqueReference = isUniqueReference(targetKey);
        foreignKeyTable = TableIdentifier.create( targetKey.getTable() );
        TableIdentifier foreignKeyReferencedTable = TableIdentifier.create( targetKey.getReferencedTable() );

        if(toForeignKey==null) {

            collectionRole = revengStrategy.foreignKeyToCollectionName(
                    fromForeignKey.getName(),
                    foreignKeyTable,
                    fromForeignKey.getColumns(),
                    foreignKeyReferencedTable,
                    fromForeignKey.getReferencedColumns(),
                    uniqueReference
            );

            tableToClassName = revengStrategy.tableToClassName( foreignKeyTable );

        } else {

            collectionRole = revengStrategy.foreignKeyToManyToManyName(
                    fromForeignKey,
                    TableIdentifier.create( fromForeignKey.getTable()),
                    toForeignKey,
                    uniqueReference
            );

            tableToClassName = revengStrategy.tableToClassName( foreignKeyReferencedTable );
        }

        collectionInverse = revengStrategy.isForeignKeyCollectionInverse(targetKey.getName(),
                foreignKeyTable,
                targetKey.getColumns(),
                foreignKeyReferencedTable,
                targetKey.getReferencedColumns());

        collectionLazy = revengStrategy.isForeignKeyCollectionLazy(targetKey.getName(),
                foreignKeyTable,
                targetKey.getColumns(),
                foreignKeyReferencedTable,
                targetKey.getReferencedColumns());

        collectionRole = makeUnique(rc,collectionRole);

        String fullRolePath = StringHelper.qualify(rc.getEntityName(), collectionRole);
        if (mappings.getCollection(fullRolePath)!=null) {
            log.debug(fullRolePath + " found twice!");
        }

        collection.setRole(fullRolePath);
        collection.setInverse(collectionInverse);
        collection.setLazy(collectionLazy);
        collection.setFetchMode(FetchMode.SELECT);

        return tableToClassName;
    }

   private boolean isUniqueReference(ForeignKey foreignKey) {

        Iterator foreignKeyIterator = foreignKey.getTable().getForeignKeyIterator();
        while ( foreignKeyIterator.hasNext() ) {
            ForeignKey element = (ForeignKey) foreignKeyIterator.next();
            if(element!=foreignKey && element.getReferencedTable().equals(foreignKey.getReferencedTable())) {
                return false;
            }
        }
        return true;
    }

    private void bindPrimaryKeyToProperties(Table table, RootClass rc, Set<Column> processed, Mapping mapping, DatabaseCollector collector) {

        SimpleValue id;
        String idPropertyname;

        List keyColumns;
        if (table.getPrimaryKey()!=null) {
            keyColumns = table.getPrimaryKey().getColumns();
        }
        else {
            log.debug("bindPrimaryKeyToProperties()=> No primary key found for " + table + ", using all properties as the identifier.");
            keyColumns = new ArrayList();
            Iterator iter = table.getColumnIterator();
            while (iter.hasNext() ) {
                Column col = (Column) iter.next();
                keyColumns.add(col);
            }
        }

        final TableIdentifier tableIdentifier = TableIdentifier.create(table);

        String tableIdentifierStrategyName;

        boolean naturalId;

        tableIdentifierStrategyName = "assigned";

        if (keyColumns.size()>1) {
            log.debug("bindPrimaryKeyToProperties()=> id strategy for " + rc.getEntityName() + " since it has a multiple column primary key");
            naturalId = true;
            id = handleCompositeKey(rc, processed, keyColumns, mapping);
            idPropertyname = revengStrategy.tableToIdentifierPropertyName(tableIdentifier);
            if(idPropertyname==null) {
                idPropertyname = "id";
            }
        }  else {
            String suggestedStrategy = revengStrategy.getTableIdentifierStrategyName(tableIdentifier);
            if(suggestedStrategy==null) {
                suggestedStrategy = collector.getSuggestedIdentifierStrategy( tableIdentifier.getCatalog(), tableIdentifier.getSchema(), tableIdentifier.getName() );
                if(suggestedStrategy==null) {
                    suggestedStrategy = "assigned";
                }
                tableIdentifierStrategyName = suggestedStrategy;
            } else {
                tableIdentifierStrategyName = suggestedStrategy;
            }

            naturalId = "assigned".equals( tableIdentifierStrategyName );
            Column pkc = (Column) keyColumns.get(0);
            checkColumn(pkc);

            id = bindColumnToSimpleValue(table, pkc, mapping, !naturalId);

            idPropertyname = revengStrategy.tableToIdentifierPropertyName(tableIdentifier);
            if(idPropertyname==null) {
                idPropertyname = revengStrategy.columnToPropertyName(tableIdentifier, pkc.getName() );
            }

            processed.add(pkc);
        }

        id.setIdentifierGeneratorStrategy(tableIdentifierStrategyName);
        id.setIdentifierGeneratorProperties(revengStrategy.getTableIdentifierProperties(tableIdentifier));

        if(naturalId) {
            id.setNullValue("undefined");
        }


        Property property = makeProperty(tableIdentifier, makeUnique(rc,idPropertyname), id, true, true, false, null, null);
        rc.setIdentifierProperty(property);
        rc.setIdentifier(id);

    }


    private void bindOutgoingForeignKeys(Table table, RootClass rc, Set<Column> processedColumns) {

        // Iterate the outgoing foreign keys and create many-to-one's
        for(Iterator iterator = table.getForeignKeyIterator(); iterator.hasNext();) {
            ForeignKey foreignKey = (ForeignKey) iterator.next();

            boolean mutable = true;
            if ( contains( foreignKey.getColumnIterator(), processedColumns ) ) {
                if ( !cfg.preferBasicCompositeIds() ) continue; //it's in the pk, so skip this one
                mutable = false;
            }

            if(revengStrategy.excludeForeignKeyAsManytoOne(foreignKey.getName(),
                    TableIdentifier.create(foreignKey.getTable() ),
                    foreignKey.getColumns(),
                    TableIdentifier.create(foreignKey.getReferencedTable() ),
                    foreignKey.getReferencedColumns())) {
                log.debug("bindOutgoingForeignKeys()=> Rev.eng excluded many-to-one for foreignkey " + foreignKey.getName());
            } else {

                boolean isUnique = isUniqueReference(foreignKey);

                String propertyName = revengStrategy.foreignKeyToEntityName(
                        foreignKey.getName(),
                        TableIdentifier.create(foreignKey.getTable() ),
                        foreignKey.getColumns(),
                        TableIdentifier.create(foreignKey.getReferencedTable() ),
                        foreignKey.getReferencedColumns(),
                        isUnique
                );

                Property property = bindManyToOne(
                        makeUnique(rc, propertyName),
                        table,
                        foreignKey,
                        processedColumns
                );

                property.setUpdateable(mutable);
                property.setInsertable(mutable);

                rc.addProperty(property);
            }
        }
    }


    private void bindColumnsToProperties(Table table, RootClass rc, Set<Column> processedColumns, Mapping mapping) {

        for (Iterator iterator = table.getColumnIterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            if ( !processedColumns.contains(column) ) {
                checkColumn(column);

                String propertyName = revengStrategy.columnToPropertyName(TableIdentifier.create(table), column.getName() );

                Property property = bindBasicProperty(
                        makeUnique(rc,propertyName),
                        table,
                        column,
                        processedColumns,
                        mapping
                );

                rc.addProperty(property);
            }
        }
    }

    private void bindColumnsToVersioning(Table table, RootClass rc, Set<Column> processed, Mapping mapping) {

        TableIdentifier identifier = TableIdentifier.create(table);

        String optimisticLockColumnName = revengStrategy.getOptimisticLockColumnName(identifier);

        if(optimisticLockColumnName!=null) {

            Column column = table.getColumn(new Column(optimisticLockColumnName));
            if(column==null) {
                log.debug("bindColumnsToVersioning()=> Column " + column + " wanted for <version>/<timestamp> not found in " + identifier);
            } else {
                bindVersionProperty(table, identifier, column, rc, processed, mapping);
            }

        } else {

            log.debug("bindColumnsToVersioning()=> Scanning " + identifier + " for <version>/<timestamp> columns.");

            Iterator columnIterator = table.getColumnIterator();
            while(columnIterator.hasNext()) {
                Column column = (Column) columnIterator.next();
                boolean useIt = revengStrategy.useColumnForOptimisticLock(identifier, column.getName());
                if(useIt && !processed.contains(column)) {
                    bindVersionProperty( table, identifier, column, rc, processed, mapping );
                    return;
                }
            }
            log.debug("bindColumnsToVersioning()=> No columns reported while scanning for <version>/<timestamp> columns in " + identifier);
        }
    }

    private void bindVersionProperty(Table table, TableIdentifier identifier, Column column, RootClass rc, Set<Column> processed, Mapping mapping) {

        processed.add(column);
        String propertyName = revengStrategy.columnToPropertyName( identifier, column.getName() );
        Property property = bindBasicProperty(makeUnique(rc, propertyName), table, column, processed, mapping);
        rc.addProperty(property);
        rc.setVersion(property);
        rc.setOptimisticLockMode(Versioning.OPTIMISTIC_LOCK_VERSION);

        log.debug("bindColumnsToVersioning()=> Column " + column.getName() + " will be used for <version>/<timestamp> columns in " + identifier);

    }

    private Property bindBasicProperty(String propertyName, Table table, Column column, Set<Column> processedColumns, Mapping mapping) {

        SimpleValue value = bindColumnToSimpleValue( table,
                column,
                mapping,
                false
        );

        return makeProperty(TableIdentifier.create( table ),
                propertyName,
                value,
                true,
                true,
                false,
                null,
                null
        );
    }

    private SimpleValue bindColumnToSimpleValue(Table table, Column column, Mapping mapping, boolean generatedIdentifier) {
        SimpleValue value = new SimpleValue(table);
        value.addColumn(column);
        value.setTypeName(guessAndAlignType(table, column, mapping, generatedIdentifier));
        return value;
    }

    private boolean contains(Iterator columnIterator, Set<Column> processedColumns) {
        while (columnIterator.hasNext() ) {
            Column element = (Column) columnIterator.next();
            if(processedColumns.contains(element) ) {
                return true;
            }
        }
        return false;
    }

    private void checkColumn(Column column) {
        if(column.getValue()!=null) {
        }
    }


    private String guessAndAlignType(Table table, Column column, Mapping mapping, boolean generatedIdentifier) {

        // maybe we should copy the column instead before calling this method.
        Integer sqlTypeCode = column.getSqlTypeCode();

        String location = "Table: " + Table.qualify(table.getCatalog(), table.getSchema(), table.getQuotedName() ) + " column: " + column.getQuotedName();

        if(sqlTypeCode==null) {
            throw new BinderException("sqltype is null for " + location);
        }

        String preferredHibernateType = revengStrategy.columnToHibernateTypeName(
                TableIdentifier.create(table),
                column.getName(),
                sqlTypeCode,
                column.getLength(),
                column.getPrecision(),
                column.getScale(),
                column.isNullable(),
                generatedIdentifier
        );

        Type wantedType = TypeFactory.heuristicType(preferredHibernateType);
        if(wantedType!=null) {

            int[] wantedSqlTypes = wantedType.sqlTypes(mapping);

            if(wantedSqlTypes.length>1) {
                throw new BinderException("The type " + preferredHibernateType + " found on " + location + " spans multiple columns. Only single column types allowed.");
            }

            int wantedSqlType = wantedSqlTypes[0];
            if (wantedSqlType != sqlTypeCode) {
                log.debug("guessAndAlignType()=> Sql type mismatch for " + location + " between DBBASEXML and wanted hibernate type. Sql type set to " + typeCodeName(sqlTypeCode.intValue()) + " instead of " + typeCodeName(wantedSqlType));
                column.setSqlTypeCode(wantedSqlType);
            }
        }
        else {
            log.info("guessAndAlignType()=> No Hibernate type found for " + preferredHibernateType + ". Most likely cause is a missing UserType class.");
        }



        if(preferredHibernateType==null) {
            throw new BinderException("Could not find javatype for " + typeCodeName(sqlTypeCode.intValue()));
        }

        return preferredHibernateType;
    }

    private String typeCodeName(int sqlTypeCode) {
        return sqlTypeCode + "(" + JDBCToHibernateTypeHelper.getJDBCTypeName(sqlTypeCode) + ")";
    }


    private SimpleValue handleCompositeKey(RootClass rc, Set<Column> processedColumns, List keyColumns, Mapping mapping) {

        Component pkc = new Component(rc);

        pkc.setMetaAttributes(Collections.EMPTY_MAP);
        pkc.setEmbedded(false);

        String compositeIdName = revengStrategy.tableToCompositeIdName(TableIdentifier.create(rc.getTable()));
        if(compositeIdName==null) {
            compositeIdName = revengStrategy.classNameToCompositeIdName(rc.getClassName());
        }
        pkc.setComponentClassName(compositeIdName);
        Table table = rc.getTable();
        List list = null;
        if (cfg.preferBasicCompositeIds() ) {
            list = new ArrayList(keyColumns);
        } else {
            list = findForeignKeys(table.getForeignKeyIterator(), keyColumns);
        }

        for (Object element : list) {

            Property property;

            if (element instanceof Column) {
                Column column = (Column) element;
                if (processedColumns.contains(column)) {

                    throw new BinderException("Binding column twice for primary key should not happen: " + column);

                } else {

                    checkColumn(column);

                    String propertyName = revengStrategy.columnToPropertyName(TableIdentifier.create(table), column.getName());
                    property = bindBasicProperty(makeUnique(pkc, propertyName), table, column, processedColumns, mapping);

                    processedColumns.add(column);
                }
            } else if (element instanceof ForeignKeyForColumns) {
                ForeignKeyForColumns fkfc = (ForeignKeyForColumns) element;
                ForeignKey foreignKey = fkfc.key;
                String propertyName = revengStrategy.foreignKeyToEntityName(
                        foreignKey.getName(),
                        TableIdentifier.create(foreignKey.getTable()),
                        foreignKey.getColumns(), TableIdentifier.create(foreignKey.getReferencedTable()), foreignKey.getReferencedColumns(),
                        true
                );
                property = bindManyToOne(makeUnique(pkc, propertyName), table, foreignKey, processedColumns);
                processedColumns.addAll(fkfc.columns);
            } else {
                throw new BinderException("unknown thing");
            }

            markAsUseInEquals(property);
            pkc.addProperty(property);

        }

        return pkc;
    }


    private void markAsUseInEquals(Property property) {
        Map<String, MetaAttribute> m = new HashMap<String, MetaAttribute>();
        MetaAttribute ma = new MetaAttribute("use-in-equals");
        ma.addValue("true");
        m.put(ma.getName(),ma);
        property.setMetaAttributes(m);
    }


    private List findForeignKeys(Iterator foreignKeyIterator, List pkColumns) {

        List tempList = new ArrayList();
        while(foreignKeyIterator.hasNext()) {
            tempList.add(foreignKeyIterator.next());
        }

//    	Collections.reverse(tempList);

        List result = new ArrayList();
        Column myPkColumns[] = (Column[]) pkColumns.toArray(new Column[pkColumns.size()]);

        for (int i = 0; i < myPkColumns.length; i++) {

            boolean foundKey = false;
            foreignKeyIterator = tempList.iterator();
            while(foreignKeyIterator.hasNext()) {
                ForeignKey key = (ForeignKey) foreignKeyIterator.next();
                List<Column> matchingColumns = columnMatches(myPkColumns, i, key);
                if(matchingColumns!=null) {

                    result.add(new ForeignKeyForColumns(key, matchingColumns));

                    i+=matchingColumns.size()-1;
                    foreignKeyIterator.remove();
                    foundKey=true;
                    break;
                }
            }

            if(!foundKey) {
                result.add(myPkColumns[i]);
            }

        }

        return result;
    }

    private List<Column> columnMatches(Column[] myPkColumns, int offset, ForeignKey key) {

        if(key.getColumnSpan()>(myPkColumns.length-offset)) {
            return null; // not enough columns in the key
        }

        List<Column> columns = new ArrayList<Column>();
        for (int j = 0; j < key.getColumnSpan(); j++) {
            Column column = myPkColumns[j+offset];
            if(!column.equals(key.getColumn(j))) {
                return null;
            } else {
                columns.add(column);
            }
        }
        return columns.isEmpty()?null:columns;
    }

    static class ForeignKeyForColumns {

        protected final List<Column> columns;
        protected final ForeignKey key;

        public ForeignKeyForColumns(ForeignKey key, List<Column> columns) {
            this.key = key;
            this.columns = columns;
        }
    }

    private Property makeProperty(TableIdentifier table, String propertyName, Value value, boolean insertable, boolean updatable, boolean lazy, String cascade, String propertyAccessorName) {

        Property prop = new Property();
        prop.setName(propertyName);
        prop.setValue(value);
        prop.setInsertable(insertable);
        prop.setUpdateable(updatable);
        prop.setLazy(lazy);
        prop.setCascade(cascade==null?"none":cascade);
        prop.setPropertyAccessorName(propertyAccessorName==null?"property":propertyAccessorName);
        bindMeta(prop, table);

        return prop;
    }

    private Property bindMeta(Property property, TableIdentifier identifier) {
        Iterator columnIterator = property.getValue().getColumnIterator();
        while(columnIterator.hasNext()) {
            Column col = (Column) columnIterator.next();

            Map map = revengStrategy.columnToMetaAttributes( identifier, col.getName() );
            if(map!=null) {
                property.setMetaAttributes( map );
            }
        }

        return property;
    }


    private String makeUnique(Component clazz, String propertyName) {
        return makeUnique(clazz.getPropertyIterator(), propertyName);
    }

    private String makeUnique(PersistentClass clazz, String propertyName) {
        List<Property> list = new ArrayList<Property>();

        if( clazz.hasIdentifierProperty() ) {
            list.add( clazz.getIdentifierProperty() );
        }

        if( clazz.isVersioned() ) {
            list.add( clazz.getVersion() );
        }

        JoinedIterator iterator = new JoinedIterator( list.iterator(),clazz.getPropertyClosureIterator() );
        return makeUnique(iterator, propertyName);
    }

    private static String makeUnique(Iterator props, String originalPropertyName) {
        int cnt = 0;
        String propertyName = originalPropertyName;
        Set<String> uniqueNames = new HashSet<String>();

        while ( props.hasNext() ) {
            Property element = (Property) props.next();
            uniqueNames.add( element.getName() );
        }

        while( uniqueNames.contains(propertyName) ) {
            cnt++;
            propertyName = originalPropertyName + "_" + cnt;
        }

        return propertyName;
    }

    public static void bindCollectionSecondPass(
            Collection collection,
            java.util.Map persistentClasses,
            Mappings mappings,
            java.util.Map inheritedMetas) throws MappingException {

        if(collection.isOneToMany() ) {
            OneToMany oneToMany = (OneToMany) collection.getElement();
            PersistentClass persistentClass = mappings.getClass(oneToMany.getReferencedEntityName() );
            if (persistentClass==null) {
                throw new MappingException("Association " + collection.getRole() + " references unmapped class: " + oneToMany.getReferencedEntityName() );
            }

            oneToMany.setAssociatedClass(persistentClass); // Child
        }

    }

    static class DbBaseCollectionSecondPass extends ColSecondPass {

        DbBaseCollectionSecondPass(Mappings mappings, Collection coll) {
            super(mappings, coll);
        }

        public void secondPass(Map persistentClasses, Map inheritedMetas) throws MappingException {
            DbBaseXmlBinder.bindCollectionSecondPass(collection, persistentClasses, mappings, inheritedMetas);
        }
    }

}

