/**
 * ${table.comment}Model
 */
package ${namespace}.model;
public class ${model}
{
<#list table.column as c>
	// ${c.comment}
	<#if c.type == 'Long' || c.type == 'long' || c.type == 'int' || c.type == 'float'>
	private ${c.type} ${c.nameLowerCamel} = ${c.defaultvalue!};
	<#elseif c.type == 'boolean'>
	private boolean ${c.nameLowerCamel} = false;
	<#elseif c.type == 'date'>
	private java.util.Date ${c.nameLowerCamel};
	<#elseif c.type == 'String'>
	private String ${c.nameLowerCamel} = "${c.defaultvalue!}";
	<#else>
	private ${c.type} ${c.nameLowerCamel};
	</#if>
</#list>
<#list table.column as c>

	public ${c.type} get${c.nameUpperCamel}()
	{
		return ${c.nameLowerCamel};
	}

	<#if c.type == 'date'>
	public void set${c.nameUpperCamel}(java.util.Date ${c.nameLowerCamel})
	{
		this.${c.nameLowerCamel} = ${c.nameLowerCamel};
	}
	<#else>
	public void set${c.nameUpperCamel}(${c.type} ${c.nameLowerCamel})
	{
		this.${c.nameLowerCamel} = ${c.nameLowerCamel};
	}
	</#if>
</#list>
}