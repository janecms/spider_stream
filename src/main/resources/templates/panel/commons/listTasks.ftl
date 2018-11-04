<!DOCTYPE html>
<html lang="en">
<head>
    <title>抓取任务列表</title>
<#include "/commons/header.ftl">
<#include "/commons/allScript.ftl">
    <script>
        function showTable(taskId) {
            rpc('/commons/spider/getTaskById', {
                taskId: taskId,
                containsExtraInfo: false
            }, function (data) {
                $("#taskListTableBody").html("");
                $.each(data.result.descriptions, function (k, v) {
                    $('<tr style="word-break:break-all; word-wrap:break-word;"><th scope="row">' + k + '</th><td>' + v + '</td></tr>')
                            .appendTo("#taskListTableBody");
                });
                $('#taskListModal').modal('show');
            });
        }
    </script>
</head>
<body>
<#include "/commons/head.ftl">
<div id="taskListModal" class="modal fade" tabindex="-1" role="dialog" style="overflow:scroll">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title">任务状态列表</h4>
            </div>
            <div class="modal-body">
                <table class="table">
                    <thead class="thead-inverse">
                    <tr>
                        <th>时间</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody id="taskListTableBody">
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<div class="container">
    <button onclick="rpcAndShowData('/commons/spider/deleteAll')"
            class="btn btn-danger">删除全部已停止爬虫
    </button>
    <div class="alert alert-success" role="alert">
        <strong>抓取任务数:</strong>${resultBundle.count}
        <strong>运行任务数:</strong>${runningTaskCount}
    </div>
    <table class="table table-hover">
        <thead class="thead-inverse">
        <tr>
            <th>#</th>
            <th>任务名称</th>
            <th>已抓取数量</th>
            <th>抓取状态</th>
            <th>抓取状态列表</th>
            <th>查看详情</th>
            <th>查看数据</th>
            <th>停止</th>
            <th>删除</th>
            <th>导出数据</th>
        </tr>
        </thead>
        <tbody>
        <#list resultBundle.resultList as task>
        <tr>
            <th scope="row">${task_index}</th>
            <td>${task.name}</td>
            <td>${task.count}</td>
            <td>${task.state}</td>
            <td>
                <button onclick="showTable('${task.taskId}')" class="btn btn-info">查看状态</button>
            </td>
            <td>
                <a href="/commons/spider/getTaskById?taskId=${task.taskId}"
                   class="btn btn-warning" target="_blank">查看详情</a>
            </td>

            <td>
                <a href="/panel/commons/list?domain=${task.name}"
                   class="btn btn-secondary" target="_blank">查看数据</a>
            </td>
            <td>
                <#if task.state == 'RUNNING'>
                    <button onclick="rpcAndShowData('/commons/spider/stop',{uuid:'${task.taskId}'})"
                            class="btn btn-danger">停止
                    </button>
                </#if>
                <#if task.state == 'STOP'>
                    <button onclick="rpcAndShowData('/commons/spider/stop',{uuid:'${task.taskId}'})"
                            class="btn btn-danger" disabled>停止
                    </button>
                </#if>
            </td>
            <td>
                <#if task.state == 'RUNNING'><a class="btn btn-danger disabled">正在抓取</a></#if>
                <#if task.state == 'STOP'>
                    <button onclick="rpcAndShowData('/commons/spider/delete',{uuid:'${task.taskId}'})"
                            class="btn btn-danger">删除
                    </button>
                </#if>
            </td>
        <td>
            </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>