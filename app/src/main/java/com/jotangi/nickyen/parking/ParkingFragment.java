package com.jotangi.nickyen.parking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jotangi.nickyen.R;
import com.jotangi.nickyen.base.BaseFragment;

public class ParkingFragment extends BaseFragment implements View.OnClickListener
{
    //    ProgressBar progressBar;
//    WebView webView;
    private TextView textView;

    public ParkingFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        textView = view.findViewById(R.id.tv_noData);
        textView.setOnClickListener(this);
//        getActivity().runOnUiThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                AppUtility.showMyDialog(getActivity(), "你即將前往外部連結", "確認", "取消", new AppUtility.OnBtnClickListener()
//                {
//                    @Override
//                    public void onCheck()
//                    {
//                        Uri uri2;
//                        uri2 = Uri.parse("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
//                        startActivity(new Intent(Intent.ACTION_VIEW, uri2));
//                    }
//
//                    @Override
//                    public void onCancel()
//                    {
//
//                    }
//                });
//            }
//        });
//        progressBar = view.findViewById(R.id.progressBar);
//        webView = view.findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        //允許android調用javascript
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setAllowFileAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//        {
//            webView.getSettings().setAllowFileAccessFromFileURLs(true);
//            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        }
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.setWebViewClient(new WebViewClient()
//        {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                if (url.startsWith("http") || url.startsWith("https")) { //http和https協議開頭的執行正常的流程
//                    return false;
//                } else {  //其他的URL則會開啟一個Acitity然後去呼叫原生APP
//                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    if (in.resolveActivity(getActivity().getPackageManager()) == null) {
//                        //說明系統中不存在這個activity
//                        view.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                AppUtility.showMyDialog(getActivity(), "你即將前往外部連結", "確認", "取消", new AppUtility.OnBtnClickListener()
//                                {
//                                    @Override
//                                    public void onCheck()
//                                    {
//                                        Uri uri2;
//                                        uri2 = Uri.parse("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
//                                        startActivity(new Intent(Intent.ACTION_VIEW, uri2));
//                                    }
//
//                                    @Override
//                                    public void onCancel()
//                                    {
//
//                                    }
//                                });
//                            }
//                        });
//
//                    } else {
//                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        startActivity(in);
//                        //如果想要載入成功跳轉可以 這樣
//                        view.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.loadUrl("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
//                            }
//                        });
//                    }
//                    return true;
//                }
//            }
//        });
//        webView.loadUrl("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_noData:
                Uri uri2;
                uri2 = Uri.parse("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
                startActivity(new Intent(Intent.ACTION_VIEW, uri2));
                break;
        }
    }
}