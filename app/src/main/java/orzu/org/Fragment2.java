package orzu.org;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    String url = "https://orzu.org/tasks/new/techrepair/techrepairother";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState){
        final View view = inflater.inflate(R.layout.fragment_main_2, container, false);


        ProgressBar progressBar = view.findViewById(R.id.progressBarMain2);


        WebView mWebView = (WebView) view.findViewById(R.id.web_view_main_fragment_2);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.loadUrl(url);

        return view;
    }

    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {

            view.loadUrl(url);

            return true;
        }
    }
}

