package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_subscriptions);
        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        getSupportActionBar().setElevation(0);
        toolbar.setTitle("Подписка на категории");

        requestCategoryList();
        // список атрибутов групп для чтения
        String groupFrom[] = new String[]{"Category"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[]{R.id.textViewCatSub};

        //   groupDataList = new ArrayList<>();

        // список атрибутов элементов для чтения
        String childFrom[] = new String[]{"SubCategory"};

        // список ID view-элементов, в которые будет помещены атрибуты
        // элементов
        int childTo[] = new int[]{R.id.radioButnSub};

        btn = findViewById(R.id.confirmSubs);
        btn.setOnClickListener(this);

        mNewAdapter = new AdapterRespandableLV(groupItem, childItem);
        mNewAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);

        expandableListView = (ExpandableListView) findViewById(R.id.expListView);
        podstilka =  findViewById(R.id.podstilkaSuibs);
        shim = findViewById(R.id.shimSubs);
        shim.startShimmer();
        expandableListView.setIndicatorBoundsRelative(50, 50);
        expandableListView.setAdapter(mNewAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.wtf("asdsdq", "asdsad");
                if(((ArrayList<SubItem>)childItem.get(i)).get(i1).getCheck()){
                    ((ArrayList<SubItem>)childItem.get(i)).get(i1).setCheck(false);
                    model.mapa.remove(((ArrayList<SubItem>)childItem.get(i)).get(i1).getId());
                }else{
                    ((ArrayList<SubItem>)childItem.get(i)).get(i1).setCheck(true);
                    model.mapa.put(((ArrayList<SubItem>)childItem.get(i)).get(i1).getId(),true);
                    for (Map.Entry<String, Boolean> entry : model.mapa.entrySet()) {
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
                    //    mGroupsArrayName = new String[lenght];
                    //      mGroupsArrayID = new String[lenght];

                    //     сhildDataList = new ArrayList<>();
                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        feedName = jsonObject.getString("name");
                        feedID[i] = jsonObject.getString("id");
                        groupId.add(jsonObject.getString("id"));
//                        mGroupsArrayName[i] = feedName;
//                        mGroupsArrayID[i] = feedID;
//                        map = new HashMap<>();
//                        map.put("Category", feedName); // время года
//                        groupDataList.add(map);

                        groupItem.add(feedName);

                        Log.wtf("forid", feedID[i]);


                        //     requestSubCategoryList(mGroupsArrayID[i]);
                    }
                    requestSubCategoryList();

                   /* lvCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            imageArrow = view.findViewById(R.id.image_arrow_menu);
                            requestSubCategoryList(mGroupsArrayID[i]);
                            if (arrowTrue){
                                arrowTrue = false;
                                imageArrow.setImageResource(0);
                                imageArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                imageArrow.setColorFilter(ContextCompat.getColor(CategorySubscriptions.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else {
                                arrowTrue = true;
                                imageArrow.setImageResource(0);
                                imageArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                                imageArrow.setColorFilter(ContextCompat.getColor(CategorySubscriptions.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                            }

                        }
                    });*/


                    //shim.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    int por = 0;

    public void requestSubCategoryList() {

        Log.wtf("kakdela",por+"");
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
                    // mSubCategoryArray = new String[100];
                    //   сhildDataItemList = new ArrayList<>();
                    ArrayList<SubItem> child = new ArrayList<SubItem>();
                    for (int i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        feedName = jsonObject.getString("name");
                        feedID = jsonObject.getString("id");
                        parentId = jsonObject.getString("parent_id");
                        child.add(new SubItem(feedName, parentId, feedID));
                    }
                    childItem.add(child);
                    if (por != groupId.size()) {
                        requestSubCategoryList();
                        Log.wtf("kakdela","horowo");
                    } else{
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

    @Override
    public void onClick(View view) {
        finish();
    }
}
