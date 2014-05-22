package com.espendwise.tools.gencode.hbmxml;


import org.hibernate.cfg.reveng.JDBCToHibernateTypeHelper;

public class DbBaseType2SqlType {


    public DbBaseType2SqlType() {
    }

    public static SqlType convert(DbBaseTypeSettings dbTypeSwttings, String pDbBaseType) {

        Integer jdbcType = dbTypeSwttings != null && dbTypeSwttings.getSpqcificDbBaseSqlTypes().containsKey(pDbBaseType)
                ? dbTypeSwttings.getSpqcificDbBaseSqlTypes().get(pDbBaseType)
                : Integer.valueOf(JDBCToHibernateTypeHelper.getJDBCType(pDbBaseType));

        return new SqlType(
                jdbcType,
                dbTypeSwttings != null ? dbTypeSwttings.getDefinitionSqlTyoes().get(jdbcType) : null,
                JDBCToHibernateTypeHelper.getJDBCTypeName(jdbcType)
        );
    }


    public static class SqlType  {
        
        private int sqlTypeCode;
        private String definitonType;
        private String jdbcType;

        public SqlType(int sqlTypeCode, String definitonType, String jdbcType) {
            this.sqlTypeCode = sqlTypeCode;
            this.jdbcType = jdbcType;
            this.definitonType = definitonType;
        }

        public int getSqlTypeCode() {
            return sqlTypeCode;
        }

        public void setSqlTypeCode(int sqlTypeCode) {
            this.sqlTypeCode = sqlTypeCode;
        }

        public String getDefinitonType() {
            return definitonType;
        }

        public void setDefinitonType(String definitonType) {
            this.definitonType = definitonType;
        }

        public String getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(String jdbcType) {
            this.jdbcType = jdbcType;
        }
    }
}
