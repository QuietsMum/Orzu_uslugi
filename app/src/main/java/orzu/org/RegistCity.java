package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistCity extends AppCompatActivity {

    String[] cities;
    ArrayAdapter<String> adapter;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_regist_city);

        spin =findViewById(R.id.user_set_place);
        requestCity();
    }

    public void requestCity() {

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=getOther&get=cities";
        OkHttpClient client = new OkHttpClient();
        Log.e("result", "enterFunction");
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegistCity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(RegistCity.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestCity();
                                dialog.dismiss();
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        }, 500);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.e("resultArrayFull", mMessage);
                int i;
                try {
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int lenght = jsonArray.length();
                    cities = new String[lenght];
                    for (i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cities[i] = (jsonObject.getString("name"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<String>(RegistCity.this,
                                    android.R.layout.simple_spinner_dropdown_item, cities);
                            spin.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
