<!DOCTYPE html>
<html lang="en">
<head>
    <title>网站列表</title>
<#include "/commons/header.ftl">
<#include "/commons/allScript.ftl">
</head>
<body>
<#include "/commons/head.ftl">
<div class="container">
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>网站域名</th>
            <th>资讯数</th>
            <th>查看列表</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody>
        <#list domainList?keys as key>
            <#assign value=domainList[key] />
        <tr>
            <th scope="row">${key_index}</th>
            <td>${key}</td>
            <td>${value}</td>
            <td><a class="btn btn-info"
                   href="/panel/commons/list?domain=${value}">查看资讯列表</a>
            </td>
            <td><a class="btn btn-danger"
                   onclick="rpcAndShowData('/commons/webpage/deleteByDomain', {domain: '${value}'});">删除网站数据</a>
            </td>
            <td><a class="btn btn-info"
                   href="/commons/webpage/exportWebpageJSONByDomain?domain=${value}">导出该网站数据JSON</a>
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>