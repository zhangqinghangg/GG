package utlis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 描述：基于webkit修改的X5内核浏览器的webview
 */
public class X5WebView extends WebView {
	TextView title;
	private WebViewClient client = new WebViewClient() {

		//防止加载网页时调起系统浏览器
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.getView().setClickable(true);
	}
	/**
	 * 描述:X5webview的初始化设置
	 */
	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
		//WebSettings类作用：对WebView进行配置和管理

		/*WebSettings webSettings = xwv.getSettings();
		webSettings.setJavaScriptEnabled(true); // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		webSettings.setAllowFileAccess(true);// 允许启用或禁用WebView访问文件数据
		webSettings.setPluginsEnabled(true);// 支持插件
		webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
		webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。若为false，则该WebView不可缩放
		webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
		webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式*/

	}

	// @Override
	/*
	 * protected boolean drawChild(Canvas canvas, View child, long drawingTime)
	 * { boolean ret = super.drawChild(canvas, child, drawingTime);
	 * canvas.save(); Paint paint = new Paint(); paint.setColor(0x7fff0000);
	 * paint.setTextSize(24.f); paint.setAntiAlias(true); if
	 * (getX5WebViewExtension() != null) {
	 * canvas.drawText(this.getContext().getPackageName() + "-pid:" +
	 * android.os.Process.myPid(), 10, 50, paint); canvas.drawText( "X5  Core:"
	 * + QbSdk.getTbsVersion(this.getContext()), 10, 100, paint); } else {
	 * canvas.drawText(this.getContext().getPackageName() + "-pid:" +
	 * android.os.Process.myPid(), 10, 50, paint); canvas.drawText("Sys Core",
	 * 10, 100, paint); } canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
	 * canvas.drawText(Build.MODEL, 10, 200, paint); canvas.restore(); return
	 * ret; }
	 */

	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}

}
