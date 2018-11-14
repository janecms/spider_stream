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
        <form class="layui-form layui-card-header layuiadmin-card-header-auto">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">名称</label>
                    <div class="layui-input-inline">
                        <input type="text" name="name" placeholder="请输入" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">标签</label>
                    <div class="layui-input-inline">
                        <#list tags as tag>
                            <input type="checkbox" name="tag" lay-filter="tags-input" value="${tag}" lay-ignore>${tag}
                        </#list>
                        <input type="hidden" id="tags" name="tags">
                    </div>
                </div>
                <div class="layui-inline">
                    <button class="layui-btn layuiadmin-btn-list" lay-submit lay-filter="school-list-search">
                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                    </button>
                </div>
            </div>
        </form>

        <div class="layui-card-body">
            <table class="layui-hide" id="school-table" lay-filter="school-table"></table>
        </div>
    </div>
</div>
<div id="pop-container">
    <table class="layui-hide" id="school-dupex-table" lay-filter="school-dupex-table"></table>
</div>
<script type="text/html" id="school-table-operate-bar">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>

<script type="text/html" id="school-table-dupex-bar">
    <a class="layui-btn layui-btn-xs" lay-event="dupex">重复</a>
</script>
<script type="text/javascript">
    layui.config({
        base: '/static/layuiadmin/' //静态资源所在路径
    }).extend({
        index: '/lib/index' //主入口模块
    }).use('lib/stream/school');
</script>
</body>
</html>