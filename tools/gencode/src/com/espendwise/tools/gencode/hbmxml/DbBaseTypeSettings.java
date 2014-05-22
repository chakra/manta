package com.espendwise.tools.gencode.hbmxml;


import java.util.Map;

public interface DbBaseTypeSettings {
   public Map<Integer, String> getDefinitionSqlTyoes();
   public Map< String, Integer> getSpqcificDbBaseSqlTypes();
}
