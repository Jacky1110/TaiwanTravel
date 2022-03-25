package com.jotangi.nickyen.pointshop;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.model.UserBean;

import java.io.File;

public class PointShopFragment extends Fragment implements View.OnClickListener
{

    ImageButton btnGoBack, btnOrder;
    ProgressBar progressBar;
    WebView webView;
    String s1 = AppUtility.toHex(UserBean.member_id);
    String s2 = AppUtility.toHex(UserBean.member_pwd);

    public PointShopFragment()
    {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_point_shop, container, false);

        initView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView(View view)
    {
        btnGoBack = view.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        btnOrder = view.findViewById(R.id.ib_record);
        btnOrder.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progressBar);
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error)
            {
                super.onReceivedSslError(view, handler, error);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("SSL驗證失敗，是否繼續前往？");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        handler.proceed();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        handler.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
            }

        });
        String str = "https://ml.jotangi.com.tw/shop?id=" + s1 + "&pwd=" + s2;
        Log.d("豪豪", str);
        webView.loadUrl(str);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.ib_go_back:
                getActivity().onBackPressed();
                break;
            case R.id.ib_record:
                String str = "https://ml.jotangi.com.tw/order?id=" + s1 + "&pwd=" + s2;
                webView.loadUrl(str);
                Log.d("豪豪", str);
                break;
        }
    }

    @Override
    public void onDestroy() {
        //销毁webview，避免内在泄漏
        if (webView != null) {
            //移除webView确保Detach
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.clearHistory();
            webView.destroy();
            webView.clearCache(true);
            webView = null;
        }
        super.onDestroy();
    }
}