package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebViewActivity extends AppCompatActivity {

    String url = "https://orzu.org/tasks/view/";
    String id = "";
    Handler uiHandler = new Handler();
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        //id = new ;
        if (intent != null)
        {
            id = "" + intent.getStringExtra("id");
            Log.e("newwnewnewnewnwenwe", id);
        }

        url = url + id;

        webView = (WebView)findViewById(R.id.web_view_task);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            //do whatever you want with the url that is clicked inside the webview.
            //for example tell the webview to load that url.
            view.loadUrl(url);
            //return true if this method handled the link event
            //or false otherwise
            return true;
        }
    }


    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod(){

            try {
                Document htmlDocument = Jsoup.connect(url + id).get();
                Elements element = htmlDocument.select("div#taskdetail");
                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadData(html, "text/html", "UTF-8");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
