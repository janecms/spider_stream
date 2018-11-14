/** 用户管理*/
;layui.extend({ajax_object:"lib/ajax_object"})
    .define(['jquery','form',"layer","table","ajax_object"],function (exports) {
        var $ = layui.jquery;
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
        var $ax=layui.ajax_object;

        form.on("submit(user-submit)",function (req) {
            new $ax("/mgr/add",function (data) {
                layer.msg("保存成功");
                //重置表单
                $(req.form).get(0).reset();
            }).setData(req.field).start();
            return false;
        });

        form.on("submit(user-update-submit)",function (req) {
            $.post("/mgr/edit",req.field,function (data) {
                layer.msg("更新成功");
            });
            return false;
        });

        table.render({
            elem: '#school-table'
            ,url: '/school/list'
            ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            ,cols: [[
                {field:'id', title: 'ID',hide:true, sort: true}
                ,{field:'name', title: '学校', sort: true}
                ,{field:'sectionName',  title: '学段'}
                ,{field:'province',  title: '省'}
                ,{field:'city', title: '市'}
                ,{field:'district', title: '县'}
                ,{field:'address', title: '地址'}
               /* ,{field:'teacherCount', title: '老师人数'}
                ,{field:'studentCount', title: '学生人数'}*/
                ,{field:'tagsStr', title: '属性'}
                ,{field:'dupex', title: '重复度'}
                ,{field:'dupex',sort: false, toolbar:'#school-table-dupex-bar'}
                ,{field:'state', title: '状态'}
                /*
                ,{field:'statusName', title: '来源'}
                */
                ,{field:'score', title: '操作', sort: false,toolbar: '#school-table-operate-bar'}
            ]],response:{
                statusName: 'code'
                ,statusCode: 1
                ,msgName: 'msg'
                ,dataName: 'data'
                ,countName: 'count'
            },request:{limitName:"pageSize",pageName:"pageNum"},
            page: true

        });;
        //监听搜索
        form.on('submit(school-list-search)', function(data){
            var field = data.field;
            table.reload('school-table', {
                where: field
            });

            return false;
        });

        //监听工具条
        table.on('tool(school-table)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){

            } else if(obj.event === 'edit'){
                openwin_edit(data.id);
            }else if (obj.event==='dupex'){
                layer.open({type:2,
                    title:'重复',
                    content:"/school/listdupex/"+data.id,
                    maxmin: true,
                    area: ['500px', '300px'],
                    fixed: false
                });
            }
        });//
        //checkbox 不能完全
        $(document).ready(function () {
            $(":checkbox[name='tag']").change(function () {
                var  checkedTag=$(":checkbox[name='tag']:checked");
                var checkedTagIds= $.map( checkedTag, function( val, i ) {
                    return val.value;
                });
                if (checkedTagIds) {
                    $("#tags").val(checkedTagIds.join(","))
                }
            });
        });
    })