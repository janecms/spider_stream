<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>学校库(ES)</title>
    <link rel="stylesheet" href="/static/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="/static/layuiadmin/style/admin.css" media="all">
    <script src="/static/js/jquery.min.js?v=2.1.4"></script>
    <script src="/static/layuiadmin/layui/layui.js"></script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <input type="hidden" id="schoolId"name="schoolId" value="${schoolId!''}"/>
            <table class="layui-hide" id="school-dupex-table" lay-filter="school-dupex-table"></table>
        </div>
    </div>
</div>
<script type="text/javascript">
    layui.config({
        base: '/static/layuiadmin/' //静态资源所在路径
    }).extend({
        index: '/lib/index' //主入口模块
    }).use('table',function () {
        var table = layui.table;
        table.render({
            elem: '#school-dupex-table'
            ,url: '/school/dupex/'+${schoolId}
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,cols: [[
                {field:'id', title: 'ID',hide:true, sort: true}
                ,{field:'name', title: '学校', sort: true}
                ,{field:'sectionName',  title: '学段'}
                ,{field:'province',  title: '省'}
                ,{field:'city', title: '市'}
                ,{field:'district', title: '县'}
                ,{field:'address', title: '地址'}
                ,{field:'tagsStr', title: '属性'}
                ,{field:'state', title: '状态'}
                /*
                ,{field:'statusName', title: '来源'}
                */
            ]],response:{
                statusName: 'code'
                ,statusCode: 1
                ,msgName: 'msg'
                ,dataName: 'data'
                ,countName: 'count'
            },
            page: false

        })

    });
</script>
</body>
</html>