${pojo.getPackageDeclaration()}

// Generated by Hibernate Tools
<#assign classbody>

/**
${pojo.getClassJavaDoc(pojo.getDeclarationName() + " generated by hbm2java", 0)}
*/
<#include "Ejb3TypeDeclaration.ftl"/>
public interface ${pojo.getDeclarationName()}  {

    <#foreach field in pojo.getAllPropertiesIterator()>
    public static final String ${pojo.getConstantField(field)} = "${field.name}";
    </#foreach>

    <#foreach property in pojo.getAllPropertiesIterator()>
        <#foreach fk in pojo.getFKKeysIterator()>
            <#if pojo.isPkForFk(property,fk)>
    public static final String ${pojo.getConstantField(property.name)} = "${pojo.getFKConstantValue(property, fk)}";
            </#if>
        </#foreach>
    </#foreach>

    <#foreach property in pojo.getAllPropertiesIterator()>
    public void set${pojo.getPropertyName(property)}(${pojo.getJavaTypeName(property, jdk5)} ${property.name});
        <#include "GetPropertyAnnotation.ftl"/>
    public ${pojo.getJavaTypeName(property, jdk5)} ${pojo.getGetterSignature(property)}();

    </#foreach>
}
</#assign>

${pojo.generateImports()}
${classbody}
