package com.example.presskg;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class MyWebViewClient extends WebViewClient {

    private final MainActivity activity;
    private final ViewCallback callback;

    public interface ViewCallback {

        void onItemClick();
    }

    public MyWebViewClient(MainActivity activity, ViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

//    @TargetApi(Build.VERSION_CODES.N)
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        view.loadUrl(request.getUrl().toString());
//        return true;
//    }

//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                webView.setVisibility(View.GONE);
//            }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //Log.d("@@@", "shouldOverrideUrlLoading: "+url);
        if (url.contains("?p=")) {
            if (callback != null) {
                callback.onItemClick();
            }
        }

//                if (url.contains("https://example.org/")) {
//                    return false;
//                }
        // все остальные ссылки будут спрашивать какой браузер открывать
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;

        if (URLUtil.isNetworkUrl(url)) {
            return false;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.toast_no_app_found, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.cancel();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

    }
}