   public ${pojo.getJavaTypeName(pojo.getIdentifierProperty(), jdk5)} getId() {
        return ${pojo.getIdentifierProperty().getName()};
   }

   public Object getFieldValue(String pFieldName) {

         <#foreach field in pojo.getMixedPropertiesIterator()>
         <#if field.equals(clazz.identifierProperty)>
        if (${pojo.getConstantField(pojo.getIdentifierProperty().getName())}.equals(pFieldName)) { return get${pojo.getPropertyName(field)}();
<#elseif !field.equals(clazz.identifierProperty)>} else if (${pojo.getConstantField(field.name)}.equals(pFieldName)) { return get${pojo.getPropertyName(field)}();
      </#if>
        </#foreach>} else { return null;
        }
   }

   public void setFieldValue(String pFieldName, Object pValue) {

         <#foreach field in pojo.getMixedPropertiesIterator()>
         <#if field.equals(clazz.identifierProperty)>
        if (${pojo.getConstantField(pojo.getIdentifierProperty().getName())}.equals(pFieldName)) { set${pojo.getPropertyName(field)}((${pojo.getJavaObjectTypeName(field, false)}) pValue);
<#elseif !field.equals(clazz.identifierProperty)>} else if (${pojo.getConstantField(field.name)}.equals(pFieldName)) { set${pojo.getPropertyName(field)}((${pojo.getJavaObjectTypeName(field,jdk5)}) pValue);
      </#if>
        </#foreach>} else {
        }
   }

   public List<String> getFields() {

         List<String> list = new ArrayList<String>();

        <#foreach field in pojo.getMixedPropertiesIterator()>
         list.add(${pojo.getConstantField(field.name)});
        </#foreach>
        
         return list;
   }
