$(function() {// 初始化内容
    $('#myForm').submit(function () {
        $("#uploadBtn").attr("disabled", "disabled");
        var options = {
            target: '#output',    // 把服务器返回的内容放入id为output的元素中
            success: showResponse,    // 提交后的回调函数
            // url : url,    //默认是form的action，如果申明，则会覆盖
            // type : type,    // 默认值是form的method("GET" or "POST")，如果声明，则会覆盖
            dataType: 'json',    // html（默认）、xml、script、json接受服务器端返回的类型
            // clearForm : true,    // 成功提交后，清除所有表单元素的值
            // resetForm : true,    // 成功提交后，重置所有表单元素的值
            timeout: 50000    // 限制请求的时间，当请求大于3秒后，跳出请求
        }
        $(this).ajaxSubmit(options);
        return false;
    });
    $("i#add").click(function () {
        addSpot(this, spotMax);
    });
});

function showResponse(responseText, statusText) {
    $("#name").html(responseText.name);
    $("#type").html(responseText.type);
    $("#size").html(responseText.size);
    $("#result").html(responseText.result);
    $("#uploadBtn").removeAttr("disabled");
}

var spotMax = 10;
if ($('div.spot').size() >= spotMax) {
    $(obj).hide();
}


function addSpot(obj, sm) {
    var size = "输入关键词" + (parseInt($('div.spot').size()) + 1);
    var inputId = "inputId" + (parseInt($('div.spot').size()) + 1);
    console.log(size);
    $('div#spots').append(
        '<div class="spot">' +
        '<div class="input-group">' +
        '<input type="text" class="form-control input-sm" style="margin-bottom: 1px;" name="spot_title" id=' + inputId + ' placeholder=' + size + ' /> ' +
        '<span class="input-group-btn">' +
        '<button class="remove btn btn-default btn-sm" type="button" value="X">X</button>' +
        '</span>' +
        '</div>' +
        '</div>')
        .find("button.remove").click(function () {
        $(this).parent().parent().parent().remove();
        $('i#add').show();
    });
    if ($('div.spot').size() >= sm) {
        alert("你只能添加10个关键词!");
        $("#add").hide();
    }
    ;
}
// var myChart = echarts.init(document.getElementById('main'));



function fenxi() {
    if (!$("#name").html()) {
        alert("你还没有上传文件！");
        return;
    }
    if(!$('div.spot').size()){
        alert("你还没有添加要分析的字段!");
        return;
    }
    var data = {
        id: $("#name").html(),
        keyword:[]
    }
    for (var i=1;i<10;i++) {
        var id = 'inputId'+i;
        if($("#"+id).val()){
            data.keyword.push($("#"+id).val());
        }
    }

    if(!data.keyword.length){
        alert("你还没有输入任何要统计的关键词!");
        return;
    }


    // 指定图表的配置项和数据
    var option = {
        title: {
            text: '《'+$('#name').html()+'》词频统计'
        },
        tooltip: {},
        legend: {
            data:[]
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            itemStyle:{
                normal:{
                    color:function (params) {
                        var colorList = ['#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ];
                        return colorList[params.dataIndex];
                    }
                }
            },
            name: '词频',
            type: 'bar',
            data: []
        }]
    };
    $.ajax({
        type: 'POST',
        url: '/solr/getindex',
        data: JSON.stringify(data),
        async: false,
        success: function (data) {
            for(var i in data) {
                option.xAxis.data.push(i);
                option.series[0].data.push(data[i]);
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        },
        dataType: "json",
        contentType:"application/json"
    });
}