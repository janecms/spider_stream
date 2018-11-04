<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>定时网页抓取任务列表</title>
<#include "/commons/header.ftl">
<#include "/commons/allScript.ftl">
</head>
<body>
<#include "/commons/head.ftl">
<div class="container">
    <div class="row">
        <table>
            <thead>
            <tr>
                <th>网站名称</th>
                <th>上次执行时间</th>
                <th>下次执行时间</th>
                <th>创建时间</th>
                <th>删除任务</th>
            </tr>
            </thead>
            <tbody>
            <#list list?keys as key>
                <#assign  entry=list[key]>
            <tr>
                <td>${entry.value.left.siteName}</td>
                <td>${entry.value.right.previousFireTime?string("yyyy/MM/dd HH:mm:ss")}</td>
                <td>${entry.value.right.nextFireTime?string("yyyy/MM/dd HH:mm:ss")}</td>
                <td>${entry.value.right.startTime?string("yyyy/MM/dd HH:mm:ss")}</td>
                <td>
                    <button onclick="deleteQuartzJob('${entry.key}')" class="am-btn am-btn-default">删除定时任务
                    </button>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>