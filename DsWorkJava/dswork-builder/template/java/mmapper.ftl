<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC
	"-//ibatis.apache.org//DTD Mapper 3.0//EN" 
	"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<mapper namespace="${namespace}.dao.${model}Dao">

<insert id="insert" parameterType="${namespace}.model.${model}">
<#list table.column as c>
	<#if c.key && c.auto>
	<selectKey resultType="${c.type}" order="AFTER" keyProperty="${c.nameLowerCamel}">
		<include refid="dswork.rowid" />
	</selectKey>
	</#if>
</#list>
	insert into ${table.nameUpperCase}
	(<#list table.column as c>${c.nameUpperCase}<#if c_has_next>, </#if></#list>)
	values
	(<#list table.column as c>${'#'}{${c.nameLowerCamel}}<#if c_has_next>, </#if></#list>)
</insert>

<delete id="delete">
	delete from ${table.nameUpperCase} where ID=${'#'}{id}
</delete>

<update id="update" parameterType="${namespace}.model.${model}">
	update ${table.nameUpperCase} set
<#list table.columnNokey as c>
		${c.nameUpperCase}=${'#'}{${c.nameLowerCamel}}<#if c_has_next>,</#if>
</#list>
	where <#list table.columnKey as c>${c.nameUpperCase}=${'#'}{${c.nameLowerCamel}}<#if c_has_next> and </#if></#list>
</update>

<resultMap id="result" type="${namespace}.model.${model}">
<#list table.column as c>
	<#if c.key>
	<id property="${c.nameLowerCamel}" column="${c.nameUpperCase}" />
	<#else>
	<result property="${c.nameLowerCamel}" column="${c.nameUpperCase}" />
	</#if>
</#list>
</resultMap>

<sql id="columns"><#list table.column as c>${c.nameUpperCase}<#if c_has_next>, </#if></#list></sql>

<sql id="dynamicWhere">
	<where>
<#list table.column as c>
	<#if c.type=='String'>
		<#if c.length<30>
		<if test="@Ognl@isNotEmpty(${c.nameLowerCamel})"> and ${c.nameUpperCase} like ${'#'}{${c.nameLowerCamel}, typeHandler=LikePrefixTypeHandler} </if>
		<#else>
		<if test="@Ognl@isNotEmpty(${c.nameLowerCamel})"> and ${c.nameUpperCase} like ${'#'}{${c.nameLowerCamel}, typeHandler=LikeTypeHandler} </if>
		</#if>
	<#else>
		<if test="@Ognl@isNotEmpty(${c.nameLowerCamel})"> and ${c.nameUpperCase}=${'#'}{${c.nameLowerCamel}} </if>
	</#if>
</#list>
	</where>
</sql>

<select id="select" resultMap="result">
	select <include refid="columns" /> from ${table.nameUpperCase} where <#list table.columnKey as c>${c.nameUpperCase}=${'#'}{${c.nameLowerCamel}}<#if c_has_next> and </#if></#list>
</select>

<select id="query" resultMap="result">
	select <include refid="dswork.top" /><include refid="columns" /> from ${table.nameUpperCase}
	<include refid="dynamicWhere" />
</select>

<select id="queryCount" resultType="int">
	select count(1) from ${table.name}
	<include refid="dynamicWhere" />
</select>

</mapper>
