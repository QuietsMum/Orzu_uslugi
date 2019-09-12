package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import orzu.org.ui.login.model;

public class CategoryView extends AppCompatActivity{

    ArrayList<Map<String, Object>> data;
    Dialog dialog;
    ProgressBar progressBar;
    public static void start(Context context) {
        Intent intent = new Intent(context, CategoryView.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        toolbar.setTitle("Фильтры");
        progressBar = findViewById(R.id.progressBarCat);



        // ArrayList<Map<String, Object>> data = null;
        /*String[] catname1 = { "Холодильники и морозильные камеры",  "Стиральные и сушильные машины", "Посудомоечные машины", "Электрические плиты и панели",
                "Газовые плиты","Духовые шкафы", "Вытяжки", "Климатическая техника",
                "Водонагреватели, бойлеры, котлы, колонки", "Швейные машины", "Пылесосы и очистители", "Утюги и уход за одеждой", "Кофемашины",
                "СВЧ печи", "Мелкая кухонная техника", "Уход за телом и здоровьем", "Строительная и садовая техника", "Что-то другое"};*/

        String cat = "Категории";
        ListView lvCat = (ListView)findViewById(R.id.list_cat);
        data = new ArrayList<>();
        /*for (int i = 0; i < catname1.length; i++){
            Map<String, Object> m1 = new HashMap<>();
            m1.put(cat,catname1[i]);
            data.add(m1);
        }
        String[] from1 = { cat};
        int[] to1 = { R.id.textItemSubCat};
        SimpleAdapter arrayAdapter1 = new SimpleAdapter(this, data, R.layout.sub_cat_item, from1, to1);
        lvCat.setAdapter(arrayAdapter1);

        lvCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //CategoryView.start(view.getContext());
            }
        });*/

        final String categoryList = "Категория задачи";
        String taskList = "Категория";
        String idList = "ID";
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];


        class AsyncOrzuTasks extends AsyncTask<String,String,ArrayList<Map<String, Object>>> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Map<String, Object>> doInBackground(String... strings)  {
                // ArrayList<Map<String, Object>> data = null;
                orzuEndpoint[0] = null;
                JsonReader[] jsonReader = new JsonReader[1];

                try {
                    orzuEndpoint[0] = new URL("https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_parent");


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
                    CategoryView.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog = new Dialog(CategoryView.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                            dialog.setContentView(R.layout.dialog_no_internet);
                            Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AsyncOrzuTasks taskBack = new AsyncOrzuTasks();
                                    taskBack.execute();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(ArrayList<Map<String, Object>> result) {
                super.onPostExecute(result);


                String[] from = { taskList};
                int[] to = { R.id.textItemCat};

                SimpleAdapter arrayAdapter = new SimpleAdapter(getBaseContext(), data, R.layout.cat_item, from, to);
                lvCat.setAdapter(arrayAdapter);
                lvCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplication(), SubCategoryView.class);
                        Map<String, Object> map;
                        map = data.get(i);
                        intent.putExtra("id", map.get(idList).toString());
                        intent.putExtra("name", map.get(taskList).toString());
                        startActivity(intent);
                        finish();
                    }
                });

                progressBar.setVisibility(View.INVISIBLE);
            }
        }


        AsyncOrzuTasks catTask = new AsyncOrzuTasks();
        catTask.execute();

    }

    private Map<String, Object> readMessage(JsonReader reader) throws IOException {
        long id = 1;
        String text = null;
        Map<String, Object> m = new HashMap<>();
        String taskList = "Категория";
        String idList = "ID";
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
                m.put(idList, id);
            } else if (name.equals("name")) {
                text = reader.nextString();
                m.put(taskList, text);
            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return m;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
