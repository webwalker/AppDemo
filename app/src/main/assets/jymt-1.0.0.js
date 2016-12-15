//add by xujian
(function() {
    var initialize = false;
    var configData;
    var configIsValid = false;
    var readyCallback = function () {
        var callback = readyCallback._que.shift();
        while (callback) {
            if (typeof callback === 'function') callback();
            callback = readyCallback._que.shift();
        }
    };
    var ymt = window.ymt = {
        initCallback: initCallback,
        config: config,
        ready: ready,
        test: test,
        //基础接口
        closeWin: closeWin,
        openWin: openWin,
        titleBar: titleBar,
        bottomBar: bottomBar,
        pageRefreshType: pageRefreshType,
        attach: attach,
        //用户接口
        userLogin: userLogin,
        getLoginStatus: getLoginStatus,
        getUserInfo: getUserInfo,
        uploadUserIcon: uploadUserIcon,
        interestMap: interestMap,
        //图像接口
        chooseImage: chooseImage,
        uploadImage: uploadImage,
        //支付接口
        pay: pay,
        order: order,
        orderPackage: orderPackage,
        notifyPay: notifyPay,
        orderDetail: orderDetail,
        withdraw: withdraw,
        //分享接口
        share: share,
        //评论接口
        comment: comment,
        replyComment: replyComment,
        //日记接口
        noteDetail: noteDetail,
        publishNote: publishNote,
        noteFansList: noteFansList,
        activityPartnerList: activityPartnerList,
        noteBrand: noteBrand,
        noteType: noteType,
        countryList: countryList,
        fansUserList: fansUserList,
        followUserList: followUserList,
        //消息接口
        showMsgIcon: showMsgIcon,
        onlineService: onlineService,
        openChat: openChat,
        //业务接口
        liveDetail: liveDetail,
        productDetail: productDetail,
        tabHome: tabHome,
        feedBack: feedBack,
        contactBook: contactBook,
        bindMobile: bindMobile,
        couponProducts: couponProducts,
        similarProduct: similarProduct,
        similarTopic: similarTopic,
        search: search,
        promotionProduct: promotionProduct,
        topicDetail: topicDetail,
        topicList: topicList,
        //事件接口
        listenPageEvent: listenPageEvent,
        notifyEvent: notifyEvent,
        //系统接口
        getDeviceInfo: getDeviceInfo,
        callPhone: callPhone,
        screenShot: screenShot,
        getNetworkType: getNetworkType,
        clipboard: clipboard,
        //监控接口
        sendUmengLog: sendUmengLog,
        sendYLog: sendYLog,
	    command: command,
	    registEvent: registEvent,
	    scrollEvent: scrollEvent
    };

    readyCallback._que = []; // manage ready callback event

    function registBridge(){
        var iframe = document.getElementById("initIframe");
        if(iframe) {
            initCallback();
            return true;
        }
        iframe = document.createElement('iframe');
        iframe.id = "initIframe";
        iframe.style.display = 'none';
        iframe.src = 'ymtapi://init/';
        document.documentElement.appendChild(iframe);
        return false;
    }

    // wait for bridge object injected
    function prepare(callback){
        if (window.WebViewJavascriptBridge) {
            callback();
        } else {
            document.addEventListener('WebViewJavascriptBridgeReady',
            function() {
                callback();
            }, false);
        }
    }

    // invoke native methods
    function execute(method, data, callback){
       // send to default handler via window.WebViewJavascriptBridge.send
       window.WebViewJavascriptBridge.callHandler(
           method
           , data
           , function(result) {
               callback(result);
           }
       );
    }

    //无通用回调
    function execute2(method, data){
        if(!isValid()) return;
        window.WebViewJavascriptBridge.callHandler2(method, data);
    }

    //配置化完成后，执行Ready设置的回调(如果ready没有执行过)
    function config(data){
        configData = data;
        registBridge();
    }

    // 调用底层方法，在底层完成config状态检测，执行回调(如果config没有执行过)
    function ready(callback){
        if (initialize) callback();
        else readyCallback._que.push(callback);
    }

    function initCallback(){
        prepare(function(){
            WebViewJavascriptBridge.debug = configData.debug;
            initialize = true;
            configIsValid = true;
            readyCallback();
            /*
            execute('config', configData, function(result){
                if(result.code == 1) {
                    configIsValid = true;
                    readyCallback();
                }
            });*/
            //set as a default js message handler
            window.WebViewJavascriptBridge.init(function(message, responseCallback) {
            });
        });
    }

    // check the valid invoke status, when any api method invoked
    function isValid(){
        var ret = true;
        if(!initialize) ret = false;
        if(!configIsValid) ret = false;

        if(!ret) {
            alert("bridge module hasn't been initialized.");
            return ret;
        }
        var domain = window.location.host;
        if(domain.indexOf("ymatou.com") == -1) {
            //alert("对不起，你没有调用权限，请联系管理员.");
            //return false;
        }
        return ret;
    }

    function test(data) { execute2('test', data); }

    function closeWin() { execute2('closeWin', ""); }

    function openWin(data) { execute2('openWin', data); }

    function titleBar(data) { execute2('titleBar', data); }

    function bottomBar(data) { execute2('bottomBar', data); }

    function pageRefreshType(data) { execute2('pageRefreshType', data); }

    function attach(data) { execute2('attach', data); }

    function userLogin() { execute2('userLogin', ''); }

    function getLoginStatus(data) { execute2('getLoginStatus', data); }

    function getUserInfo(data) { execute2('getUserInfo', data); }

    function uploadUserIcon(data) { execute2('uploadUserIcon', data); }

    function interestMap() { execute2('interestMap', ''); }

    function chooseImage(data) { execute2('chooseImage', data); }

    function uploadImage(data) { execute2('uploadImage', data); }

    function pay(data) { execute2('pay', data); }

    function order(data) { execute2('order', data); }

    function orderDetail(data) { execute2('orderDetail', data); }

    function orderPackage(data) { execute2('orderPackage', data); }

    function withdraw() { execute2('withdraw', ''); }

    function notifyPay(data) { execute2('notifyPay', data); }

    function share(data) { execute2('share', data); }

    function comment(data) { execute2('comment', data); }

    function replyComment(data) { execute2('replyComment', data); }

    function noteDetail(data) { execute2('noteDetail', data); }

    function publishNote(data) { execute2('publishNote', data); }

    function noteFansList(data) { execute2('noteFansList', data); }

    function activityPartnerList(data) { execute2('activityPartnerList', data); }

    function noteBrand() { execute2('noteBrand', ''); }

    function noteType(data) { execute2('noteType', data); }

    function countryList() { execute2('countryList', ''); }

    function followUserList(data) { execute2('followUserList', data); }

    function fansUserList(data) { execute2('fansUserList', data); }

    function showMsgIcon() { execute2('showMsgIcon', ''); }

    function onlineService() { execute2('onlineService', ''); }

    function openChat(data) { execute2('openChat', data); }

    function liveDetail(data) { execute2('liveDetail', data); }

    function productDetail(data) { execute2('productDetail', data); }

    function tabHome(data) { execute2('tabHome', data); }

    function feedBack() { execute2('feedBack', ''); }

    function contactBook() { execute2('contactBook', ''); }

    function bindMobile(data) { execute2('bindMobile', data); }

    function couponProducts(data) { execute2('couponProducts', data); }

    function similarProduct(data) { execute2('similarProduct', data); }

    function similarTopic(data) { execute2('similarTopic', data); }

    function search(data) { execute2('search', data); }

    function promotionProduct(data) { execute2('promotionProduct', data); }

    function topicList(data) { execute2('topicList', data); }

    function topicDetail(data) { execute2('topicDetail', data); }

    function listenPageEvent(data) { execute2('listenPageEvent', data); }

    function notifyEvent(data) { execute2('notifyEvent', data); }

    function getDeviceInfo(data) { execute2('getDeviceInfo', data); }

    function callPhone(data) { execute2('callPhone', data); }

    function screenShot(data) { execute2('screenShot', data); }

    function getNetworkType(data) { execute2('getNetworkType', data); }

    function clipboard(data) { execute2('clipboard', data); }

    function sendUmengLog(data) { execute2('sendUmengLog', data); }

    function sendYLog(data) { execute2('sendYLog', data); }

    function registEvent(data) { execute2('registEvent', data); }

    function scrollEvent(data) { execute2('scrollEvent', data); }

    function command(name, data) { execute2(name, data); }
})();