package com.meituan.android.tax_calculator.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meituan.android.tax_calculator.R;

public class RuleActivity extends AppCompatActivity {

    private final String pageURL = "http://map.baidu.com/zt/client/privacy/index.html";
    private static final String TAG = "RuleActivity";
    private WebView webView;
    LinearLayout loadingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        loadingLayout = findViewById(R.id.loading_layout);

        initActionBar();

        initWebView();

    }

    private void initWebView() {
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(RuleActivity.this, "页面加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl(pageURL);
        webView.setLongClickable(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    private void initActionBar(){
        ActionBarFragment fragment = (ActionBarFragment) getFragmentManager().findFragmentByTag("action_bar_fragment");
        fragment.setBackIcon(R.drawable.ic_left_dark);
        TextView textView = fragment.getTitle();
        textView.setText(getString(R.string.rule_titile));
        textView.setTextColor(Color.BLACK);

        FrameLayout actionBarFragmentLayout = fragment.getActionBarFragmentLayout();
        actionBarFragmentLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
