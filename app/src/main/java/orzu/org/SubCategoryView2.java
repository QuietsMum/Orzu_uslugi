package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubCategoryView2 extends AppCompatActivity {

    ArrayList<Map<String, Object>> data;
    SimpleAdapter arrayAdapter1;
    RecyclerView suggestResultView;
    ArrayAdapterMy resultAdapter;
    List<String> suggestResult;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_view2);

        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        toolbar.setTitle("Фильтры");

        pr = findViewById(R.id.progrescity);

        suggestResultView = findViewById(R.id.result_city);
        suggestResultView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        suggestResultView.setLayoutManager(layoutManager);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapterMy(this, suggestResult);
        suggestResultView.setAdapter(resultAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(suggestResultView.getContext(),
                layoutManager.getOrientation());
        suggestResultView.addItemDecoration(dividerItemDecoration);

        requestCity();

    }

    public void requestCity(){

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=getOther&get=cities";
        OkHttpClient client = new OkHttpClient();
        Log.e("result", "enterFunction");
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SubCategoryView2.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(SubCategoryView2.this, android.R.style.Theme_Material_Light_NoActionBar);
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
                        pr.setVisibility(View.INVISIBLE);
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
                    suggestResult.clear();
                    for (i = 0; i < lenght; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        suggestResult.add(jsonObject.getString("name"));

                    }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultAdapter.notifyDataSetChanged();
                            }
                        });

                        pr.setVisibility(View.INVISIBLE);


                    ArrayAdapterMy.setSelect(new NameItemSelect() {
                            @Override
                            public void onItemSelectedListener(View view, int position) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", suggestResult.get(position));
                                Log.e("resultCity", suggestResult.get(position));
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();

                            }

                            @Override
                            public void onClick(View view) {

                            }
                        });


                } catch (JSONException e) {
                    e.printStackTrace(); }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
