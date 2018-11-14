;layui.define(['jquery','form',"layer"],function (exports) {
    var $ = layui.jquery;
    var form = layui.form;
    var layer = layui.layer;
    form.verify({
        email_exist: function(value) {
            //验证邮箱
            if (value){
                var  str="";
                $.ajax({
                    type: 'GET',
                    url: '/mgr/code',
                    dataType: "json",
                    async: false,
                    data: {"account":value},
                    success: function(result) {
                        if (result.code!=1){
                            str="该邮箱未注册,如需注册请联系管理员";
                        }
                    },
                    error: function(data) {
                    }
                });
                return str;
            }
        }
    });
    $(".layadmin-user-jump-change").on("click",function () {
       layer.msg("请联系管理员徐峰重置密码");
    });
    form.on("submit(user-login-submit)",function (form) {
      console.log("login-submit!--");
    });
    //TODO 需要优化提示风格;
})