package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phone;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);



        phone = (EditText)findViewById(R.id.editTextPhone);
        button = (Button)findViewById(R.id.button_phone_login);

        button.setOnClickListener(this);

    }

    public final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String postOkhttp (String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }



    @Override
    public void onClick(View view) {

        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);

        /*class post extends AsyncTask<String,String, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... strings)  {

                JSONObject jsonParam = new JSONObject();

                try {
                    jsonParam.put("timestamp", 1488873360);

                jsonParam.put("message", String.valueOf(phone));
                jsonParam.put("latitude", 0D);
                jsonParam.put("longitude", 0D);
                postOkhttp("https://orzu.org/login", jsonParam.toString() );
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }

        post catTask = new post();
        catTask.execute();*/

        finish();

    }
}
