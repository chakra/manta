package com.espendwise.tools.gencode.hbmxml;

import org.hibernate.tool.hbm2x.pojo.POJOClass;

import java.util.Map;


public class AppHbm2JavaFullEntityExporter extends AppHbm2JavaExporter {

    public AppHbm2JavaFullEntityExporter() {
        super();
    }

    protected void exportPOJO(Map pMap, POJOClass pPojoClass) {
        if (javaname.isEmpty() || javaname.contains(pPojoClass.getShortName())) {
            super.exportSuperPOJO(pMap,
                    new AppFullEntityPOJOClass(
                            getMappedClass(pPojoClass),
                            getCfg2JavaTool(),
                            super.getConfiguration(),
                            getSerialVersionUID(pPojoClass)
                    )
            );
        }
    }

}