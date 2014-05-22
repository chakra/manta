package com.espendwise.tools.gencode.hbmxml;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class PostgresSqlTypeSettings implements DbBaseTypeSettings{

    private static final Map<String, Integer> SPECIFIC_DBBASE_SQLTYPES = new HashMap<String, Integer>();
    private static final Map< Integer, String> DEFINITION_SQLTYPES = new HashMap< Integer, String>();

    static {
        SPECIFIC_DBBASE_SQLTYPES.put( "NUMBER", Types.NUMERIC);
        SPECIFIC_DBBASE_SQLTYPES.put( "VARCHAR2", Types.VARCHAR);
        SPECIFIC_DBBASE_SQLTYPES.put( "CLOB", Types.CLOB);
    }

    static {
        DEFINITION_SQLTYPES.put( Types.NUMERIC, "numeric");
        DEFINITION_SQLTYPES.put( Types.CLOB, "clob");
    }

    @Override
    public Map<Integer, String> getDefinitionSqlTyoes() {
        return DEFINITION_SQLTYPES;
    }

    @Override
    public Map<String, Integer> getSpqcificDbBaseSqlTypes() {
        return SPECIFIC_DBBASE_SQLTYPES;
    }

}
