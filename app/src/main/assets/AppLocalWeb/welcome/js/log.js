/**
 * Created by meters on 15/1/6.
 */
var gACTION_FRAME = document.createElement("iframe");
gACTION_FRAME.setAttribute("style","position:absolute;z-index:-1;width:0;height:0;border:0;overflow:hidden;visibility:hidden;");
document.body.appendChild(gACTION_FRAME);
gACTION_FRAME.style.height = 0;

function log(asType, asFrom)
{
	gACTION_FRAME.src = [
		"http://dianhua.qq.com/cgi-bin/readtemplate?t=log&type=",
		asType,
		"&from=",
		asFrom
	].join("");
}