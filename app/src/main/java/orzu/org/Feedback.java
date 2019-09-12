package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orzu.org.ui.login.FeedbackAdapter;

public class Feedback extends AppCompatActivity {

    ShimmerFrameLayout shim;
    View viewBack;
    ArrayList<Map<String, Object>> data;
    ListView lvCat;
    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Отзывы");
        idUser = getIntent().getExtras().getString("idUserFeedback");
        shim = (ShimmerFrameLayout) findViewById(R.id.feedbackshimmer);
        shim.startShimmer();
        lvCat = (ListView)findViewById(R.id.list_feedback);
        requestFeedback();
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

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=reviews&act=view&userid=" + idUser + "&sort=all";
        OkHttpClient client = new OkHttpClient();

        String cat = "Имена";
        String nar = "Описание";
        String img = "Картинка";
        String count = "Отзыв";
        String avatar = "Аватар";

        data = new ArrayList<>();

        FeedbackAdapter arrayAdapter1 = new FeedbackAdapter(this, data);
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
                try {
                    Map<String, Object> m;
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int bpSad =  R.drawable.ic_sad;
                    int bpNorm = R.drawable.ic_neutral;
                    int bpHappy = R.drawable.ic_happy;
                    int lenght = jsonArray.length();
                    String feedName = "";
                    for (int i = 0; i < lenght; i++){
                        m = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                       /* if (jsonObject.getString("fname").equals("null")){
                            feedName = jsonObject.getString("username");
                        } else feedName = jsonObject.getString("username") + jsonObject.getString("fname");*/
                             //feedName = jsonObject.getString("username") + jsonObject.getString("fname");
                             feedName = jsonObject.getString("username");

                        String[] splited = jsonObject.getString("avatar").split(Character.toString ((char) 94));
                        String str = Arrays.toString(splited);
                        Log.wtf("count",str+"");
                        try {
                            Bitmap bitmap = Picasso.get().load("https://orzu.org"+str.substring(1,str.length()-1)).get();
                            m.put(avatar,bitmap);
                        }catch (Exception ex){
                            Bitmap icon = BitmapFactory.decodeResource(Feedback.this.getResources(),Common.drawable);
                            m.put(avatar,icon);
                        }
                        m.put(cat, feedName);
                        long countFeed = jsonObject.getLong("like");
                        if (countFeed == 0){
                            m.put(count, "-1");
                            m.put(img, bpSad);
                        } if (countFeed == 1){
                            m.put(count, "0");
                            m.put(img, bpNorm);
                        } if (countFeed == 2){
                            m.put(count, "+1");
                            m.put(img, bpHappy);
                        }if(countFeed==3){
                            m.put(count, "+3");
                            m.put(img, bpHappy);
                        }

                        m.put(nar, jsonObject.getString("narrative"));

                        data.add(m);
                    }



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            lvCat.setAdapter(arrayAdapter1);
                            arrayAdapter1.notifyDataSetChanged();
                        }
                    });

                    shim.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace(); }
            }
        });
    }
}
