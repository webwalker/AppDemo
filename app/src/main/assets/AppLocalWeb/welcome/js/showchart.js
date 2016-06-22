
    var jsonData = {};
    var tmpStr = "";
    var stateDataObj = {}, cmccObj = {}, unicomObj = {}, telecomObj = {}, mapObj = {};
    var cmccMapObj = {}, unicomMapObj = {}, telecomMapObj = {};
    var tabItem = $('.mod-tab__item');
    var modMap = $('.mod-map');
    var ChinaMap = $('#ChinaMap');
    var ChinaMap2 = $('#ChinaMap2');
    var ChinaMap3 = $('#ChinaMap3');
    var sColor = '', dColor = '';
    var myState = {};

    //微信中访问隐藏分享按钮
    if (window.WeixinJSBridge){
        $('.mod-button').hide();
    }

    WelcomeJS.showMap = function(datastring){

        //设置目前该区域所有运营商的数据
        tmpStr = datastring;
        
        jsonData = $.parseJSON(datastring);
 
        if(jsonData.data && (jsonData.data.CMCC || jsonData.data.ChinaTelecom || jsonData.data.ChinaUnicom)){          
            
            $.each(jsonData.data, function(key, value){                
                switch(key){
                    case 'CMCC':
                        $.each(value, function(area, data){                                                        
                            saveObj('CMCC', area, data);
                        });
                        break;

                    case 'ChinaTelecom':
                        $.each(value, function(area, data){                            
                            saveObj('ChinaTelecom', area, data);
                        });
                        break;

                    case 'ChinaUnicom':
                        $.each(value, function(area, data){                            
                            saveObj('ChinaUnicom', area, data);
                        });
                        break;
                }
            });
        }

        function saveObj(type, area,  value){
            var stateInitColor = 2;
            var name = '';
            var stateSelectedColor = '';

            //移动数据
            if(type == 'CMCC'){
                
                if(value.data < 60){
                    stateInitColor = 0;
                    stateSelectedColor = 'e84138';
                }else{
                    stateSelectedColor = '62c280';
                }

                if(area == 'taiwan' || area == 'hongkong' || area == 'macau'){
                    name = value.name;
                }else{
                    name = value.name + '移动';
                }

                cmccObj[area] = {
                    data: value.data,
                    name: name,
                    stateInitColor: stateInitColor,
                    stateSelectedColor: stateSelectedColor,
                    stateHoverColor: stateSelectedColor
                }

            }

            //联通数据
            if(type == 'ChinaUnicom'){
                if(value.data < 60){
                    stateInitColor = 0;
                    stateSelectedColor = 'e84138';
                }else{
                    stateSelectedColor = '62c280';
                }

                if(area == 'taiwan' || area == 'hongkong' || area == 'macau'){
                    name = value.name;
                }else{
                    name = value.name + '联通';
                }

                unicomObj[area] = {
                    data: value.data,
                    name: name,
                    stateInitColor: stateInitColor,
                    stateSelectedColor: stateSelectedColor,
                    stateHoverColor: stateSelectedColor
                }
            }

            //电信数据
            if(type == 'ChinaTelecom'){

                if(value.data < 60){
                    stateInitColor = 0;
                    stateSelectedColor = 'e84138';
                }else{
                    stateSelectedColor = '62c280';
                }

                if(area == 'taiwan' || area == 'hongkong' || area == 'macau'){
                    name = value.name;
                }else{
                    name = value.name + '电信';
                }

                telecomObj[area] = {
                    data: value.data,
                    name: name,
                    stateInitColor: stateInitColor,
                    stateSelectedColor: stateSelectedColor,
                    stateHoverColor: stateSelectedColor
                }
            }
        }
        
        //标签切换
        tabItem.on('touchstart', function(){
            var that = $(this);
            var type = that.attr('data');            

            tabItem.removeClass('mod-tab__item_active');
            that.addClass('mod-tab__item_active');

            switch(type){

                case '0':
                    stateDataObj = cmccObj;
                    modMap.hide();
                    ChinaMap.show();
                    svgMapFn(ChinaMap, cmccObj, cmccMapObj);
                    break;
                case '1': 
                    stateDataObj = telecomObj;
                    modMap.hide();
                    ChinaMap2.show();
                    svgMapFn(ChinaMap2, telecomObj, telecomMapObj);
                    break;
                case '2': 
                    stateDataObj = unicomObj;
                    modMap.hide();
                    ChinaMap3.show();
                    svgMapFn(ChinaMap3, unicomObj, unicomMapObj);
                    break;
            }

        });
        
        //根据用户当前运营商显示tab状态
        // if(jsonData.my.type == '中国移动' || jsonData.my.type == '移动'){
            stateDataObj = cmccObj;
            mapObj = cmccMapObj;
            modMap.hide();
            ChinaMap.show();
            svgMapFn(ChinaMap, cmccObj, cmccMapObj);
            tabItem.removeClass('mod-tab__item_active');
            $(tabItem[1]).addClass('mod-tab__item_active');
        // }
        // if(jsonData.my.type == '中国电信' || jsonData.my.type == '电信'){
        //     stateDataObj = telecomObj;
        //     mapObj = telecomMapObj;
        //     modMap.hide();
        //     ChinaMap2.show();
        //     svgMapFn(ChinaMap2, telecomObj, telecomMapObj);
        //     tabItem.removeClass('mod-tab__item_active');
        //     $(tabItem[1]).addClass('mod-tab__item_active');
        // }
        // if(jsonData.my.type == '中国联通' || jsonData.my.type == '联通'){
        //     stateDataObj = unicomObj;
        //     mapObj = unicomMapObj;
        //     modMap.hide();
        //     ChinaMap3.show();
        //     svgMapFn(ChinaMap3, unicomObj, unicomMapObj);
        //     tabItem.removeClass('mod-tab__item_active');
        //     $(tabItem[2]).addClass('mod-tab__item_active');
        // }



        

        //显示当前用户所在省份状态
        // if(jsonData.my.areaId){


        //     if(jsonData.my.data > 49){
        //         sColor = '#e84138';
        //         dColor = '#FF5955';
        //     }else{
        //         sColor = '#62c280';
        //         dColor = '#69D463';
        //     };

        //     $(document.body).append('<div id="StateTip" style="width:86px;"></div');

        //     $('#StateTip').css({
        //         left: $(mapObj[jsonData.my.areaId].node).offset().left - 35,
        //         top: $(mapObj[jsonData.my.areaId].node).offset().top - 35
        //     }).html(stateDataObj[jsonData.my.areaId].name + '<br/>接通率：' + stateDataObj[jsonData.my.areaId].data + '%').show();
        //     mapObj[jsonData.my.areaId].attr({
        //         fill: sColor
        //     });
        // }

        

        function svgMapFn(id, option, mapObject){
            //地图初始化
            id.SVGMap({
                mapName: 'china',
                mapWidth: ($(window).width()>320)?320:300,
                mapHeight: ($(window).width()>320)?320:300,
                external: mapObject,
                stateData: option,
                stateTipHtml: function(stateData, obj) {
                    return stateDataObj[obj.id].name + '<br/>接通率：' + stateDataObj[obj.id].data + '%';
                }
            });

        };

            if(!jsonData.local){
                this.showChart();  
            }  


        
    };

    WelcomeJS.showChart = function(){
        
        var hasLargeNum = false;
        var modCmcc = $('.mod-cmcc');
        var modUnicm = $('.mod-unicom');
        var modTelecom = $('.mod-telecom');
        var areaOther = $('.mod-other');

        if(jsonData.my){


            myState.areaId = jsonData.my.areaId;
            myState.area = jsonData.my.area;
            var myStateID = myState.areaId;    
            $.each(jsonData.data, function(key, value){                
                switch(key){
                    case 'CMCC':
                        $.each(value, function(area, data){                            
                            if(area == myStateID) myState.CMCC = data.data;                        
                        });
                        break;

                    case 'ChinaTelecom':
                        $.each(value, function(area, data){
                            if(area == myStateID) myState.ChinaTelecom = data.data;                        
                        });
                        break;

                    case 'ChinaUnicom':
                        $.each(value, function(area, data){
                            if(area == myStateID) myState.ChinaUnicom = data.data;                        
                        });
                        break;
                }
            });

        }

        else{
            myState = {
                'area':'other',
                'areaId':'other',
                'CMCC':49,
                'ChinaTelecom':79,
                'ChinaUnicom':81
            }

        }


        
      

         //设置目前该区域所有运营商的数据
        if(jsonData.data && (jsonData.data.CMCC || jsonData.data.ChinaTelecom || jsonData.data.ChinaUnicom)){


            $(".mod-chart__item").removeClass("mod-chart__item_red");

            initView('CMCC', myState.CMCC);
            initView('ChinaTelecom', myState.ChinaTelecom);
            initView('ChinaUnicom', myState.ChinaUnicom);

            $(".mod-chart__captain-mystate").html(stateName(myState.area));

           
            function stateName(areaString){
                if(areaString == "other"){
                    areaString = "全国";
                    return areaString;
                }
                if (areaString!='tianjin' && areaString!='beijing' && areaString!='chongqing' && areaString!='macau' && areaString!='shanghai' && areaString!='taiwan' && areaString!='hongkong')
                {
                     areaString += "省";
                }

                return areaString
            }

            function initView(type, value){

                value = parseInt(value);
                
                var offset = 0;
                //移动数据
                if(type == 'CMCC'){

                    //当数据大于等于50，柱形图显示红色
                    if(value < 60){
                        hasLargeNum = true;
                        modCmcc.addClass('mod-chart__item_red');
                        offset = 5;

                    }else{
                        modCmcc.removeClass('mod-chart__item_red');
                    }

                    modCmcc.css('height', value + offset + '%');
                    
                    modCmcc.find('.mod-chart__percent').html( value + '%' );
                }

                //联通数据
                if(type == 'ChinaUnicom'){

                    //当数据大于等于60，柱形图显示红色
                    if(value < 60){
                        hasLargeNum = true;
                        modUnicm.addClass('mod-chart__item_red');
                        offset = 5;
                    }else{
                        modUnicm.removeClass('mod-chart__item_red');
                    }

                    modUnicm.css('height', value + offset + '%');

                    modUnicm.find('.mod-chart__percent').html( value + '%' );
                }

                //电信数据
                if(type == 'ChinaTelecom'){

                    //当数据大于等于60，柱形图显示红色
                    if(value < 60){
                        hasLargeNum = true;
                        modTelecom.addClass('mod-chart__item_red');
                        offset = 5;                        
                    }else{
                        modTelecom.removeClass('mod-chart__item_red');
                    }


                    modTelecom.css('height', value + offset + '%');

                    modTelecom.find('.mod-chart__percent').html( value + '%' );
                }

                

            }

        }
                        
    };

    







    


