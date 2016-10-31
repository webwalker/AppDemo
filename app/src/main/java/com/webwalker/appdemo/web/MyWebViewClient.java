package com.webwalker.appdemo.web;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.webwalker.framework.utils.FileUtil;
import com.webwalker.framework.utils.HttpUtil;
import com.webwalker.framework.utils.Loggers;

/**
 * @author xu.jian
 *
 */
public class MyWebViewClient extends WebViewClient {

	boolean haSet = false;

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
		// return super.shouldOverrideUrlLoading(view, url);

		// 自定义页面所有_blank标签的打开方式
		/*
		 * System.out.println(url); if (url.startsWith("newtab:")) { String
		 * realUrl = url.substring(7, url.length()); Intent it = new
		 * Intent(Intent.ACTION_VIEW); it.setData(Uri.parse(realUrl));
		 * view.getContext().startActivity(it); } else { view.loadUrl(url); }
		 * return true;
		 */
	}

	@Override
	public void onPageStarted(final WebView view, final String url,
							  Bitmap favicon) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
//					String html = HttpUtil.getHtml(url);
//					Loggers.d(html);
//					FileUtil.writeFile(view.getContext().getFilesDir()
//							+ "/html.txt", html);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		// if (!haSet) {
		// String tt =
		// "http://a.utanbaby.com/trace/pageclick/?osname=linux&ostype=unknown&screen_size=1080x1920&screen_color=24-bit&rurl=http%3A%2F%2Fwww.utanbaby.com%2Factive%2Fguimilianmengdahui%3Ffrom%3Dtimeline%26isappinstalled%3D0&dr=utanbaby.com&dz=www.utanbaby.com&gd=bpmtfdn9plk43dg9kr0ghg8im3.1425607250&icp=180.166.27.254&isp=58.215.49.35&mt=GET&ref=http%3A%2F%2Fwww.utanbaby.com%2Factive%2Fguimilianmengdahui%3Ffrom%3Dtimeline%26isappinstalled%3D0&tm=2015-03-06%2010:10:38&uid=24305104&sid=bpmtfdn9plk43dg9kr0ghg8im3&active=guimilianmeng2015&deviceid=0b4eb74a50a4a60d0c7258801061c7b0&area=GMLMDH-btn-4";
		// String js = "javascript:window.location.href='" + tt + "'";
		//
		// view.loadUrl(js);
		// haSet = true;
		// }

		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		// 自定义页面所有_blank标签的打开方式
		// view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.setAttribute('target','_self');link.href = 'newtab:'+link.href;}}}");
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
								String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

}