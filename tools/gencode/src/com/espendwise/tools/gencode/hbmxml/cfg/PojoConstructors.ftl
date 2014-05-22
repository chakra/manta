
<#--  /** default constructor */ -->
    public ${pojo.getDeclarationName()}() {
    }
<#if pojo.needsMinimalConstructor()>	<#-- /** minimal constructor */ -->
    public ${pojo.getDeclarationName()}(${pojo.asParameterList(pojo.getPropertyClosureForMinimalConstructor(), jdk5)}) {
<#if pojo.isSubclass() && !pojo.getPropertyClosureForSuperclassMinimalConstructor().isEmpty()>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForSuperclassMinimalConstructor())});
</#if>
<#foreach field in pojo.getPropertiesForMinimalConstructor()>
        this.set${pojo.getPropertyName(field)}(${field.name});
</#foreach>
    }
</#if>
<#if pojo.needsFullConstructor()>
<#-- /** full constructor */ -->

    public ${pojo.getDeclarationName()}(${pojo.asParameterList(pojo.getPropertyClosureForFullConstructor(), jdk5)}) {
<#if pojo.isSubclass() && !pojo.getPropertyClosureForSuperclassFullConstructor().isEmpty()>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForSuperclassFullConstructor())});
</#if>
<#foreach field in pojo.getPropertiesForFullConstructor()>
        this.set${pojo.getPropertyName(field)}(${field.name});
</#foreach>
    }
</#if>
