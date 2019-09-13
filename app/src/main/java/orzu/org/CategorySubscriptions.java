package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orzu.org.ui.login.model;

public class CategorySubscriptions extends AppCompatActivity implements View.OnClickListener {
    //    ArrayList<Map<String, Object>> data;
//    ArrayList<Map<String, Object>> data2;
    ExpandableListView lvCat;
    //    SimpleExpandableListAdapter adapter;
    ExpandableListView expandableListView;
    View podstilka;
//    private String[] mGroupsArrayName;
//    private String[] mGroupsArrayID;
//    private String[] mSubCategoryArray;
//    Map<String, String> map;
//    ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
//    ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
//    ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
//    ListView lvSubCat;
//    ImageView imageArrow;
//    Boolean arrowTrue = true;


    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<String> groupId = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();
    AdapterRespandableLV mNewAdapter;
    ShimmerFrameLayout shim;
    TextView btn;
    String idUser;
    DBHelper dbHelper;
    ArrayList<String> subsServer = new ArrayList<>();
    int counter;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_subscriptions);
        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        getSupportActionBar().setElevation(0);
        toolbar.setTitle("Подписка на категории");
        requestSubsServer();
        counter = 0;
        String groupFrom[] = new String[]{"Category"};
        int groupTo[] = new int[]{R.id.textViewCatSub};
        String childFrom[] = new String[]{"SubCategory"};
        int childTo[] = new int[]{R.id.radioButnSub};
        pb = findViewById(R.id.progresSubscriptions);
        pb.setVisibility(View.INVISIBLE);
        btn = findViewById(R.id.confirmSubs);
        btn.setOnClickListener(this);

        mNewAdapter = new AdapterRespandableLV(groupItem, childItem);
        mNewAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);

        expandableListView = (ExpandableListView) findViewById(R.id.expListView);
        podstilka = findViewById(R.id.podstilkaSuibs);
        shim = findViewById(R.id.shimSubs);
        shim.startShimmer();
        expandableListView.setIndicatorBoundsRelative(50, 50);
        expandableListView.setAdapter(mNewAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.wtf("asdsdq", "asdsad");
                if (((ArrayList<SubItem>) childItem.get(i)).get(i1).getCheck()) {
                    ((ArrayList<SubItem>) childItem.get(i)).get(i1).setCheck(false);
                    model.mapa.remove(((ArrayList<SubItem>) childItem.get(i)).get(i1).getParent_id()+";"+((ArrayList<SubItem>) childItem.get(i)).get(i1).getId());
                } else {
                    ((ArrayList<SubItem>) childItem.get(i)).get(i1).setCheck(true);
                    model.mapa.put(((ArrayList<SubItem>) childItem.get(i)).get(i1).getParent_id()+";"+((ArrayList<SubItem>) childItem.get(i)).get(i1).getId(),((ArrayList<SubItem>) childItem.get(i)).get(i1).getId());
                    for (Map.Entry<String, String> entry : model.mapa.entrySet()) {
                        Log.wtf("forik", entry.getKey());
                    }
                }
                mNewAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestCategoryList() {

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_parent";
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
                try {
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int lenght = jsonArray.length();
                    String feedName = "";
                    String[] feedID = new String[lenght];
                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        feedName = jsonObject.getString("name");
                        feedID[i] = jsonObject.getString("id");
                        groupId.add(jsonObject.getString("id"));
                        groupItem.add(feedName);
                    }
                    requestSubCategoryList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    int por = 0;

    public void requestSubCategoryList() {

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_subcat&id=" + groupId.get(por);
        por++;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        int finalI = por;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();

                try {
                    Map<String, Object> msub;
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int lenght = jsonArray.length();
                    String feedName = "";
                    String feedID = "";
                    String parentId = "";
                    ArrayList<SubItem> child = new ArrayList<SubItem>();
                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        feedName = jsonObject.getString("name");
                        feedID = jsonObject.getString("id");
                        parentId = jsonObject.getString("parent_id");
                        child.add(new SubItem(feedName, parentId, feedID));
                        if (model.arraySubs.contains(feedID)) {
                            child.get(i).setCheck(true);
                            model.mapa.put(parentId+";"+feedID,parentId+";"+feedID);
                        }
                    }
                    childItem.add(child);

                    if (por != groupId.size()) {

                        requestSubCategoryList();
                        Log.wtf("kakdela", "");

                    } else {
                        shim.setVisibility(View.INVISIBLE);
                        podstilka.setVisibility(View.INVISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNewAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }

    public void requestSubsServer() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int tokenColIndex = c.getColumnIndex("id");
        idUser = c.getString(tokenColIndex);
        c.close();
        db.close();
        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=view_user&user_cat=" + idUser;

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

                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    Iterator<String> iter = jsonObject.keys();
                    int i = 0;
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            Object value = jsonObject.get(key);
                            subsServer.add(key);
                        } catch (JSONException e) {
                        }
                        i++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                model.arraySubs = subsServer;
                requestCategoryList();

            }
        });

    }

    public void requestSubsServerAdd() {
        pb.setVisibility(View.VISIBLE);
        String modelString = "";
        for (Map.Entry<String, String> entry : model.mapa.entrySet()) {
            modelString = modelString + "&cat[]=" + entry.getKey();
        }
        final char dm = (char) 34;

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=user_param" +
                "&userid=" + idUser +
                "&act=subscribe" + modelString;
        Log.e("stringModelFull", url);
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
                Log.e("result", mMessage);
                if (mMessage.equals(dm + "Success" + dm)){
                    pb.setVisibility(View.INVISIBLE);
                    finish();
                }

            }
        });

    }
    @Override
    public void onClick(View view) {
        requestSubsServerAdd();
    }
}
