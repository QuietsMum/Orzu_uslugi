package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orzu.org.ui.login.TaskAdapter;

public class FeedbackTask extends AppCompatActivity implements MainItemSelect {

    ShimmerFrameLayout shim;
    public static Activity fa;
    View viewBack;
    ArrayList<Map<String, Object>> data;
    RecyclerView lvCat;

    TextView empty;
    MainItemSelect itemSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        setContentView(R.layout.activity_feedback_task);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Отклики");
        shim = (ShimmerFrameLayout) findViewById(R.id.feedbackshimmertask);
        shim.startShimmer();
        lvCat = findViewById(R.id.list_feedback_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lvCat.setLayoutManager(layoutManager);
        empty = findViewById(R.id.fedbacktaskempty);
        itemSelect = this;
        requestFeedback();
        fa = this;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(FeedbackTask.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FeedbackTask.this, new String[]{Manifest.permission.CALL_PHONE},0);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else
                {

                }
                return;
            }
        }
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

    public void requestFeedback(){

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=task_requests&act=view&task_id=" + Common.taskId;
        OkHttpClient client = new OkHttpClient();

        String cat = "ID";
        String name = "Имена";
        String nar = "Описание";
        String amt = "Цена";
        String idSug = "SugID";
        String stars_sad = "Sad";
        String stars_nat = "Nat";
        String stars_hap = "Hap";
        String selected = "Select";
        String phone = "Phone";
        String count = "Отзыв";
        data = new ArrayList<>();
        TaskAdapter arrayAdapter1 = new TaskAdapter(this, data);

        arrayAdapter1.setClickListener(new TaskAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String, Object> m2 = data.get(position);
                Intent intent = new Intent(getApplication(), UserView.class);
                intent.putExtra("idhis", String.valueOf(m2.get(cat)));
                Log.wtf("Click","cliked");
                startActivity(intent);
            }
        });

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.e("resultArrayFull", mMessage);

                if (mMessage.equals("No request yet")){
                    empty.setVisibility(View.VISIBLE);
                }

                try {
                    Map<String, Object> m;
                    JSONArray jsonArray = new JSONArray(mMessage);
                    String feedID = "";

                    int lenght = jsonArray.length();
                    for (int i = 0; i < lenght; i++){
                        m = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        feedID = jsonObject.getString("user_id");
                        m.put(cat, feedID);
                        m.put(nar, jsonObject.getString("narrative"));
                        m.put(name, jsonObject.getString("username"));
                        m.put(amt, jsonObject.getString("amount"));
                        m.put(idSug, jsonObject.getString("id"));
                        m.put(stars_sad, jsonObject.getString("userstars_sad"));
                        m.put(stars_nat, jsonObject.getString("userstars_neutral"));
                        m.put(stars_hap, jsonObject.getString("userstars_happy"));
                        m.put(selected, jsonObject.getString("selected"));
                        m.put(phone, jsonObject.getString("userphone"));
                        data.add(m);
                    }
                    //}

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            lvCat.setAdapter(arrayAdapter1);
                        }
                    });

                   shim.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace(); }
            }
        });
    }

    public void chooseSuggester(Object id){

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS" +
                "&opt=task_requests" +
                "&act=selected" +
                "&req_id=" + id;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.e("resultArrayFull", mMessage);

            }
        });
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelectedListener(View view, int position) {

    }
}
