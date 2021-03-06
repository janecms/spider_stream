<!DOCTYPE html>
<html lang="en">
<head>
    <title>资讯列表</title>
<#include "/commons/header.ftl" >
<#include "/commons/allScript.ftl">
    <script type="text/javascript">
        $(function () {
            var validate = $("#webpageForm").validate({
                rules: {
                    page: {
                        required: true,
                        number: true
                    }
                },
                highlight: function (element) {
                    $(element).closest('.form-group').addClass('has-error');
                },
                success: function (label) {
                    label.closest('.form-group').removeClass('has-error');
                    label.remove();
                },
                errorPlacement: function (error, element) {
                    element.parent('div').append(error);
                }
            });

        });
        function showDetail(id) {
            rpc('/commons/webpage/getWebpageById', {id: id}, function (data) {
                $("#modalTitle").text(data.result.title);
                var modalBody = $("#modalBody");
                modalBody.html('');
                modalBody.append("<h4>正文</h4>");
                modalBody.append('<p>' + data.result.content + '</p>');
                modalBody.append("<h4>关键词</h4>");
                if (data.result.keywords != undefined) {
                    $.each(data.result.keywords, function (i, word) {
                        modalBody.append(word + ' ,');
                    });
                }
                modalBody.append("<h4>摘要</h4>");
                if (data.result.summary != undefined) {
                    modalBody.append('<p>' + data.result.summary.join(' ,') + '</p>');
                }
                modalBody.append("<h4>发布时间</h4>");
                modalBody.append('<span>' + data.result.publishTime + '</span>');
                modalBody.append("<h4>动态字段</h4>");
                if (data.result.dynamicFields != undefined) {
                    $.each(data.result.dynamicFields, function (k, v) {
                        modalBody.append("<p>" + k + " : " + v + "</p>");
                    });
                }
                $('#myModal').modal('show');
            });
        }

        function clearForm() {
            $("#query").val(null);
            $("#domain").val(null);
        }
    </script>
    <style type="text/css">
        .divide {
            height: 5px;
            width: 100%;
        }
    </style>
</head>
<body>
<#include "/commons/head.ftl">
<div class="col-md-10 col-md-offset-1">
    <div class="divide"></div>
    <div class="container">
        <form class="form-inline" id="webpageForm" action="/panel/commons/list">
            <div class="col-md-5">
                <div class="form-group">
                    <label for="query">关键词:</label>
                    <input class="form-control" id="query" name="query" value="${query!''}">
                </div>
            </div>
            <div class="col-md-5">
                <div class="form-group">
                    <label for="domain">域名:</label><span style="color: red;">(*支持模糊)</span>
                    <input class="form-control" id="domain" name="domain" value="${domain!''}">
                </div>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary" id="priceSubmit">搜索</button>
                &nbsp;
                <a href="javascript:void(0);" class="btn btn-danger" onclick="clearForm();">重置</a>
            </div>
        </form>
    </div>
    <div class="divide"></div>
    <div class="container">
        <div class="row">
            <table class="table table-border table-hover">
                <thead class="thead-inverse">
                <tr>
                    <th>#</th>
                    <th>标题</th>
                    <th>网站</th>
                    <th>时间</th>
                    <th>查看</th>
                    <th>转到</th>
                    <th>删除</th>
                </tr>
                </thead>
                <tbody>
                <#list webpages as webpage>
                <tr>
                    <th scope="row">${webpage_index}</th>
                    <td>${webpage.title}</td>
                    <td>${webpage.domain}</td>
                    <td>${webpage.gathertime?string("yyyy/MM/dd HH:mm:ss")}
                    </td>
                    <td>
                        <button onclick="showDetail('${webpage.id}')" class="btn btn-info">Show</button>
                    </td>
                    <td>
                        <a href="/panel/commons/showWebpageById?id=${webpage.id}"
                           class="btn btn-primary" target="_blank">Go</a>
                    </td>
                    <td>
                        <button onclick="rpcAndShowData('/commons/webpage/deleteById',{id:'${webpage.id}'})"
                                class="btn btn-danger"> 删除
                        </button>
                    </td>
                </tr>
                </#list>

                </tbody>
            </table>
        </div>

        <!-- 底部页码 -->
    <#include "/commons/tablePage.ftl">
    </div>

</div>
</body>
</html>