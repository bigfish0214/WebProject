<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello ${name}!</title>
    
</head>
<body>
	<#--
		我是注释！
	-->
	<#assign title = "hello">
	<#include "header.ftl">
    <h2 class="hello-title">
    	Hello <#if name == "loli"> beautiful </#if> ${name} ${name1!"visitor"}! </br>
    	<#if name=="loli">
    		1
    	<#elseif name ="cc">
    		2
    	<#else>
    		3
    	</#if>
    	</br>
    </h2>
    
    <ul>
    	<#list colors as color>
    		<li>This is Color:${color}</li>
    	</#list>
    </ul>
    <ul>
    	<#list m?keys as key>
    		<li> ${key} : ${m[key]}</li>
    	</#list>
    </ul>
    ${user.description}</br>
    ${user.getDescription()}</br>
    
    ${title}</br>
    <#macro greet person cl>
    	<font size="+2" color = "${cl}">Hello ${person}!</font>
    </#macro>
    <@greet person = "lisi" cl = "blue"/>
    
</body>
</html>