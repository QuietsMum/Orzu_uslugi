package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import orzu.org.ui.login.model;

public class SubCategoryView extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Map<String, Object>> data;
    SimpleAdapter arrayAdapter;
    ListView lvCat;
    String taskList = "Категория";
    Boolean check = false;
    Boolean setChkd = false;
    String ckeckList = "Check";
    String idList = "ID";
    Long [] idArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_view);

        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        toolbar.setTitle("Фильтры");

        String cat = "Категории";
        lvCat = (ListView)findViewById(R.id.list_sub_cat_sub);

        TextView parent = findViewById(R.id.textViewParent);
        FrameLayout fmLay = findViewById(R.id.frameLayoutfilt);
        fmLay.setOnClickListener(this);

        Long[] idArrayEmpt = new Long[1];
        idArrayEmpt[0] = 0L;
        model.array = idArrayEmpt;

        data = new ArrayList<>();

        final String categoryList = "Категория задачи";

        String idList = "ID";
        String idIntent = getIntent().getExtras().getString("id");
        String nameIntent = getIntent().getExtras().getString("name");
        parent.setText(nameIntent);
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];


        class AsyncOrzuTasks extends AsyncTask<String,String,ArrayList<Map<String, Object>>> {

            //final ViewHolder holder = new ViewHolder();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Map<String, Object>> doInBackground(String... strings)  {

                orzuEndpoint[0] = null;
                JsonReader[] jsonReader = new JsonReader[1];

                try {
                    orzuEndpoint[0] = new URL("https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_subcat&id=" + idIntent);


                    myConnection[0] =
                            (HttpsURLConnection) orzuEndpoint[0].openConnection();
                    if (myConnection[0].getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection[0].getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");

                        jsonReader[0] = new JsonReader(responseBodyReader);
                    }

                    data = new ArrayList<>();
                    jsonReader[0].beginArray(); // Start processing the JSON object
                    while (jsonReader[0].hasNext()) { // Loop through all keys

                        data.add(readMessage(jsonReader[0]));

                    }
                    jsonReader[0].endArray();


                    jsonReader[0].close();
                    myConnection[0].disconnect();
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(ArrayList<Map<String, Object>> result) {
                super.onPostExecute(result);

                idArray = new Long[data.size()];
                String[] from = { taskList, ckeckList};
                int[] to = { R.id.textItemSubCat, R.id.checkBoxFilt};

                final ViewHolder[] holder = {null};
                arrayAdapter = new SimpleAdapter(getBaseContext(), data, R.layout.sub_cat_item, from, to);

                lvCat.setAdapter(arrayAdapter);
                lvCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Map<String, Object> map;
                        map = data.get(i);
                        holder[0] = new ViewHolder();
                        holder[0].check = view.findViewById(R.id.checkBoxFilt);
                        view.setTag(holder);
                        if (!holder[0].check.isChecked()) {
                            idArray[i] =  (Long) map.get(idList);
                            holder[0].check.setChecked(true);
                            map.put(ckeckList, true);
                        } else {
                            idArray[i] = 0L;
                            holder[0].check.setChecked(false);
                            map.put(ckeckList, false);
                        }
                        arrayAdapter.notifyDataSetChanged();
                        lvCat.invalidateViews();
                    }
                });
                arrayAdapter.notifyDataSetChanged();
                lvCat.invalidateViews();
                lvCat.setAdapter(arrayAdapter);
            }
        }


        AsyncOrzuTasks catTask = new AsyncOrzuTasks();
        catTask.execute();

    }

    private Map<String, Object> readMessage(JsonReader reader) throws IOException {
        long id = 1;
        String text = null;

        Map<String, Object> m = new HashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
                m.put(idList, id);
            } else if (name.equals("name")) {
                text = reader.nextString();
                m.put(taskList, text);
                m.put(ckeckList, check);
            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return m;
    }

    @Override
    public void onClick(View view) {

        Map<String, Object> map;
        final ViewHolder[] holder = {null};
        holder[0] = new ViewHolder();
        holder[0].check = view.findViewById(R.id.checkBoxParent);
        //holder[0].check.setPadding(0,0,0,0);

       /* TextView text = lvCat.findViewById(R.id.textItemSubCat);
        text.setPadding(0,0,0,0);
        CheckBox checkBox = lvCat.findViewById(R.id.checkBoxFilt);
        checkBox.setPadding(0,0,0,0);
        lvCat.setPadding(0,0,0,0);*/

        if (!holder[0].check.isChecked()) {
            holder[0].check.setChecked(true);

            for (int i = 0; i < data.size(); i++){
                map =  data.get(i);
                idArray[i] = (Long) map.get(idList);
                map.put(ckeckList, true);
            }
            check = true;
        } else {
            holder[0].check.setChecked(false);
            for (int i = 0; i < data.size(); i++){
                map =  data.get(i);
                idArray[i] = 0L;
                map.put(ckeckList, false);
            }
            check = false;
        }

       /*ArrayList<Map<String, Object>> datanew = new ArrayList<>();
        for (int i = 0; i < data.size(); i++){
            m_new =  data.get(i);
            m_new.put(ckeckList, check);
            datanew.add(m_new);
        }      */

        arrayAdapter.notifyDataSetChanged();
        lvCat.invalidateViews();
        lvCat.setAdapter(arrayAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.up_menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_filter_menu:

                model.array = idArray;
                for (int i = 0; i < model.array.length; i++){
                    Log.e("LOGMODEL", String.valueOf(model.array[i]));
                }
                finish();
                return true;
            case android.R.id.home:

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static class ViewHolder {
        CheckBox check;
    }
}
