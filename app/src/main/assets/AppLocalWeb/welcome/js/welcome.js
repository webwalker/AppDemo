
var WelcomeJS = window.WelcomeJS || {};
var mainWrap = $(".mod-wrap");
var pageNumber = 1;

var screenHeight = Math.max($(window).height(),$(document.body).height());;

function startTouch(event) {
    if (!event.touches.length) {
        return;
    }
    tmpEndY = 0;
    var touch = event.touches[0];
    tmpStartY = touch.pageY;

    
}

function moveTouch(event) {
    event.preventDefault();
    if (!event.touches.length) {
        return;
    }
    var touch = event.touches[0];
    tmpEndY = touch.pageY;
}

// 触摸结束时判断执行上翻或者下翻
function endTouch(event){
    // if (event.target.localName == "path") {return};
    var endY = tmpEndY;
    var startY = tmpStartY;
    if (endY && endY !== startY && endY-startY<=-25) {       

        if(pageNumber==5) return;        
        slideDown();        

    }else if(endY && endY !== startY && endY-startY>=25){
        
        if(pageNumber==1) return;
        slideUP();
    }
}

// 向上滚一瓶
function slideUP(){
    $(".stateTip").remove();
    var translateString,transitionString;
    pageNumber--;
    if (pageNumber == 4) {
        $(".mod-skip").fadeIn();
        $(".mod-next").fadeIn();
    };
    console.log("slide down! pageNumber :" + pageNumber);
    currentDistance=screenHeight*(pageNumber-1);
    translateString="translate3d(0, -"+currentDistance+"px, 0)";
    transitionString="all 0.5s ease-in";

    mainWrap.css({"-webkit-transform":translateString,"transform":translateString,"-webkit-transition":transitionString,"transition":transitionString});
}

// 向下滚一瓶
function slideDown(){
    $(".stateTip").remove();
    var translateString,transitionString;
    pageNumber++;
    if(pageNumber == 5){
        $(".mod-skip").fadeOut();
        $(".mod-next").fadeOut();
    }
    console.log("slide down! pageNumber :" + pageNumber);
    currentDistance=screenHeight*(pageNumber-1)
    translateString="translate3d(0, -"+currentDistance+"px, 0)";
    transitionString="all 0.5s ease-in";

    mainWrap.css({"-webkit-transform":translateString,"transform":translateString,"-webkit-transition":transitionString,"transition":transitionString});


}




WelcomeJS = {
    init:function(){
        this.initHeight();
        this.bindSwipe();
        this.bindSkip();
        this.bindStart();
        this.bindShare();
    },

    initHeight:function(){    
        $(".mod-container").height(screenHeight);
        $(".mod-page").height(screenHeight);
        
        
    },
    uaIs: function () {
        var ua = navigator.userAgent;
        var checker = {
            ios: ua.match(/(iPhone|iPod|iPad)/),
            android: ua.match(/Android/)
        };
        return checker;
    },
    bindSwipe:function(){
        
        mainWrap.on("touchstart",function(e){
            startTouch(e);
        });
        mainWrap.on("touchmove",function(e){
            moveTouch(e);
        });
        mainWrap.on("touchend",function(e){
            endTouch(e);
        });
    },
    bindSkip:function(){
        $(".mod-skip").on('touchstart',function(){
            var ua = WelcomeJS.uaIs();
            if(ua.ios){
                location.href="pbjs://welcomeSkip";
                log("welcome","ios_skip");
            }
            else{
                console.log("welcomeSkip");
                log("welcome","android_skip");
            }
        });


    },
    bindStart:function(){
        $(".mod-start").on('touchend',function(){
            var ua = WelcomeJS.uaIs();
            if(ua.ios){
                location.href="pbjs://welcomeStart";
                log("welcome","ios_start");
            }
            else{
                log("welcome","android_start");
                console.log("welcomeStart");
            }
        });
    },
    bindShare:function(){
        var highVersionAndroidApp = {
            shareToFriend:function(title,url,desc){
                console.log(["shareToFriend",title,url,desc].join("^"));
            },
            shareToMoment:function(title,url,desc){
                console.log(["shareToMoment",title,url,desc].join("^"));
            }
        };
        
        var iosApp = {
            shareToFriend: function (title, url, desc) {
                location.href = "sharecalllog://dianhua.qq.com/?type=friend&title=" + encodeURIComponent("微信电话本网络测速") + "&url=" + "$link_friend.DATA$" + "&desc=" + encodeURIComponent("$desc.DATA$");
            },
            shareToMoment: function (title, url, desc) {
                location.href = "sharecalllog://dianhua.qq.com/?type=moment&title=" + encodeURIComponent(title) + "&url=" + encodeURIComponent(url) + "&desc=" + encodeURIComponent(desc);
            }
        };
        
        var ua = WelcomeJS.uaIs();
        if(ua.ios){
            fallbackApp = iosApp;

        }
        else{
            fallbackApp = highVersionAndroidApp;

        }
        window.App = window.App || fallbackApp;


        $(".mod-share").on('click',function(){
            App.shareToMoment("为什么隔壁老王用微信电话本那么顺？", "http://dianhua.qq.com/cgi-bin/readtemplate?t=dianhuaben_welcome", "微信电话本欢迎页分享");
            console.log("welcomeShare");


            if(ua.ios){
                log("welcome","ios_share");
            }
            else{
                log("welcome","android_share");
            }
        });
    },
    renderShareButton:function(isWeixinInstalled){
        if(parseInt(isWeixinInstalled)==1){
            $(".mod-share").show();
        }
        else{
            $(".mod-share").hide();   
        }
    }






}



