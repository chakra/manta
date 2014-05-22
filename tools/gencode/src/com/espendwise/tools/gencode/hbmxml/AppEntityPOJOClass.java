package com.espendwise.tools.gencode.hbmxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.MetaAttribute;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.tool.hbm2x.Cfg2JavaTool;
import org.hibernate.tool.hbm2x.pojo.EntityPOJOClass;
import org.hibernate.type.BooleanType;

import java.util.*;

public class AppEntityPOJOClass extends EntityPOJOClass {

    private final static Log log = LogFactory.getLog(AppEntityPOJOClass.class);

    private String serialVesrionUID;
    protected Configuration cfg;
    public boolean ejb3;
    public boolean jdk5;

    public static final Set<String> IGNORED_IMPORTS = new HashSet<String>();
    static {
        IGNORED_IMPORTS.add("static javax.persistence.GenerationType.SEQUENCE");
        IGNORED_IMPORTS.add("com.espendwise.tools.gencode.hbmxml.NumberBooleanType");
    }

    public static final Set<String> CONVERT_STATIC_IMPORTS_TO_LOCAL_MEMBERS = new HashSet<String>();
    static {
        CONVERT_STATIC_IMPORTS_TO_LOCAL_MEMBERS.add("SEQUENCE");
    }

    public static final Map<String, String[]> IGNORED_COLUMN_BASIC_ANNOTATION = new HashMap<String, String[]>();
    static {
        IGNORED_COLUMN_BASIC_ANNOTATION.put("version", new String[]{"@Version"});
    }

    public AppEntityPOJOClass(PersistentClass persistentClass, Configuration pConfiguration, Cfg2JavaTool cfg2JavaTool, String pSerialVesrionUID) {
        super(persistentClass, cfg2JavaTool);
        this.serialVesrionUID = pSerialVesrionUID;
        this.cfg = pConfiguration;
        this.ejb3 = true;
    }

    public boolean isUseSerialVersionUID() {
        return this.serialVesrionUID != null;
    }

    public String getSerialVessionUID() {
        return this.serialVesrionUID;
    }

    public boolean getEjb3() {
        return ejb3;
    }

    public void setEjb3(boolean ejb3) {
        this.ejb3 = ejb3;
    }


    public String getConstantField(String pName) {
        String constant = "";
        char[] chars = pName.toCharArray();
        for (char ch : chars) {
            if (Character.isUpperCase(ch)) {
                constant += "_" + Character.toUpperCase(ch);
            } else {
                constant += Character.toUpperCase(ch);
            }
        }
        return constant;
    }

    @Override
    public String generateBasicAnnotation(Property property) {

        String ann;
        if (property.getType() instanceof BooleanType) {
            ann = "    @" +importType("org.hibernate.annotations.Type") + "(type=\"com.espendwise.manta.support.hibernate.NumberBooleanType\")";
        } else {
            ann = super.generateBasicAnnotation(property);
        }

        String[] ingnored = getIgnoredColumnBasicAnnotation().get(property.getName());

        if (ingnored != null) {
            for (String ignore : ingnored) {
                if (ann.trim().startsWith(ignore)) {
                    return "";
                }
            }
        }

        return ann;
    }

    public String getJavaObjectTypeName(Property property, boolean b) {
        return asObjType(super.getJavaTypeName(property, b));
    }

    @Override
    public String generateAnnIdGenerator() {
        return splitAnn(super.generateAnnIdGenerator());
    }

    public String splitAnn(String s) {
        StringBuilder buffer = new StringBuilder();
        int i = s.indexOf("@");
        if (i >= 0) {
            String[] splitStr = s.substring(i).split("@");
            int index = 0;
            for (String str : splitStr) {
                if (str.trim().length() > 0 ) {
                    if (index > 0) {
                        buffer.append("\n");
                    }
                    buffer.append(s.substring(0, i));
                    buffer.append("@");
                    if(str.startsWith("SequenceGenerator")){
                        str = str.replace(")", ", allocationSize=1)");
                    }
                    buffer.append(str);



                    index++;
                }
            }
        } else {
            if(s.startsWith("SequenceGenerator")){
                s = s.replace(")", ", allocationSize=1)");
            }
            buffer.append(s);

        }
        return buffer.toString();
    }

    @Override
    public String generateImports() {
        return filterImports(super.generateImports(), getIgnoredImports());
    }

    public String staticImport(String fqcn, String member) {
        Set<String> set = getConvertStaticInportsToLocalMembers();
        return set.contains(member) ? fqcn + "." + member : super.staticImport(fqcn, member);
    }

    public String filterImports(String s, Set<String> pFilter) {

        StringBuilder buffer = new StringBuilder();

        String[] imports = s.split(";");
        if (imports != null && imports.length > 0) {
            for (String _import : imports) {
                String str = _import.trim().replace("\n", "").replaceAll(" ", "").replaceAll("import", "");
                if (!str.isEmpty()) {
                    if (!pFilter.contains(str)) {
                        buffer.append(_import).append(";");
                    }
                }
            }
        }

        return buffer.toString();
    }

    public String asObjType(String pType) {
        if ("byte".equals(pType)) {
            return "java.lang.Byte";
        } else if ("char".equals(pType)) {
            return "java.lang.Character";
        } else if ("short".equals(pType)) {
            return "java.lang.Short";
        } else if ("int".equals(pType)) {
            return "java.lang.Integer";
        } else if ("long".equals(pType)) {
            return "java.lang.Long";
        } else if ("float".equals(pType)) {
            return "java.lang.Float";
        } else if ("double".equals(pType)) {
            return "java.lang.Double";
        } else if ("NumberBooleanType".equals(pType)) {
            return "Boolean";
        }  else if ("number_boolean".equals(pType)) {
            return "Boolean";
        } else {
            return pType;
        }
    }

    public String getJavaTypeName(Property property, boolean b) {
        return asNativeType(super.getJavaTypeName(property, b));
    }

    public String asNativeType(String pType) {
        if ("NumberBooleanType".equals(pType)) {
            return "Boolean";
        }  else if ("number_boolean".equals(pType)) {
            return "Boolean";
        } else {
            return pType;
        }
    }



    public String asParameterList(List fieldList, boolean useGenerics) {
        StringBuilder buf = new StringBuilder();
        Iterator fields = fieldList.iterator();
        while (fields.hasNext()) {
            Property field = (Property) fields.next();
            buf.append(getJavaTypeName(field, useGenerics))
                    .append(" ")
                    .append(field.getName());
            if (fields.hasNext()) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    public Iterator getMixedPropertiesIterator() {
        Set<String> duplCtrlSet = new HashSet<String>();
        List<Property> properties = new ArrayList<Property>();
        getMixedPropertiesIterator(cfg.getClassMapping(this.getMappedClassName()), properties, duplCtrlSet);
        return properties.iterator();
    }

    public void getMixedPropertiesIterator(PersistentClass pClass, List<Property> pProperties, Set<String> duplCtrlSet) {

        Iterator propertiesIterator = this.getAllPropertiesIterator(pClass);
        while (propertiesIterator.hasNext()) {
            Property p = (Property) propertiesIterator.next();
            if (!duplCtrlSet.contains(p.getName())) {
                pProperties.add(p);
                duplCtrlSet.add(p.getName());
            }
        }

        AppEntityPOJOClass appPojo = new AppEntityPOJOClass(pClass,
                this.cfg,
                this.c2j,
                null
        );

        if (appPojo.getExtends() != null) {
            PersistentClass superClass = cfg.getClassMapping(appPojo.getExtends());
            if (superClass != null) {
                getMixedPropertiesIterator(superClass, pProperties, duplCtrlSet);

            }
        }

    }

    public boolean getExtendsFields() {
        MetaAttribute meta = this.meta.getMetaAttribute("extends-fields");
        return meta == null || meta.getValue() == null || meta.getValue().equalsIgnoreCase("true");
    }


    public boolean getJdk5() {
        return jdk5;
    }

    public void setJdk5(boolean jdk5) {
        this.jdk5 = jdk5;
    }

    public Map<String, String[]> getIgnoredColumnBasicAnnotation() {
        return IGNORED_COLUMN_BASIC_ANNOTATION;
    }

    public Set<String> getConvertStaticInportsToLocalMembers() {
        return CONVERT_STATIC_IMPORTS_TO_LOCAL_MEMBERS;
    }

    public Set<String> getIgnoredImports() {
        return IGNORED_IMPORTS;
    }
}
