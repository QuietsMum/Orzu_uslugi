package orzu.org;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Fragment1 extends Fragment {
    /*private String[] categoryList = {"Категория задачи", "Категория задачи", "Категория задачи", "Категория задачи", "Категория задачи", "Категория задачи"};
    private String[] taskList = {"Починить телевизор", "Починить телевизор", "Починить телевизор", "Починить телевизор", "Починить телевизор", "Починить телевизор"};
    private String[] priceList = {"100 тенге", "100 тенге", "100 тенге", "100 тенге", "100 тенге", "100 тенге"};*/

    ArrayList<Map<String, Object>> data;
    RecyclerView rv;
    RVAdapter adapter;
       @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState){

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        ProgressBar progressBar = view.findViewById(R.id.progressBarMain);

        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);



        final String categoryList = "Категория задачи";
        final String taskList = "Задание";
        String idList = "ID";
        String catidList = "CatID";
        final String priceList= "Цена";
        final String servList= "За услугу";
        final String dateList= "Дата публицации";
        final String updateList= "Дата обновления";
        final String needList= "Сроки";
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];






        class AsyncOrzuTasks extends AsyncTask<String,String,ArrayList<Map<String, Object>>> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Map<String, Object>> doInBackground(String... strings)  {

                orzuEndpoint[0] = null;
                JsonReader[] jsonReader = new JsonReader[1];

                try {
                    orzuEndpoint[0] = new URL("https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_task&tasks=all");


                    myConnection[0] =
                            (HttpsURLConnection) orzuEndpoint[0].openConnection();
                    if (myConnection[0].getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection[0].getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");

                        jsonReader[0] = new JsonReader(responseBodyReader);
                    } else {
                       Toast.makeText(getActivity().getBaseContext(), "No Internet!",
                         Toast.LENGTH_LONG).show();
                    }

                    data = new ArrayList<>();
                    jsonReader[0].beginArray(); // Start processing the JSON object
                    while (jsonReader[0].hasNext()) { // Loop through all keys
                        Map<String, Object> m_data = new HashMap<>();
                        m_data = readMessage(jsonReader[0]);
                        if (!m_data.isEmpty()) {
                            data.add(m_data);
                        }
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

                progressBar.setVisibility(View.INVISIBLE);

                String[] from = { categoryList,  taskList, priceList, servList, dateList, updateList, needList};
                int[] to = { R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView6, R.id.textView7, R.id.textView8 };

               /* SimpleAdapter arrayAdapter = new SimpleAdapter(container.getContext(), data, R.layout.main_item, from, to);
                simpleList.setAdapter(arrayAdapter);
                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        Map<String, Object> map;
                        map = data.get(i);
                        intent.putExtra("id", map.get(idList).toString());
                        startActivity(intent);

                    }
                });*/
               adapter = new RVAdapter(getContext(),data);
               rv.setAdapter(adapter);
               RVAdapter.setSelect(new MainItemSelect() {
                   @Override
                   public void onItemSelectedListener(View view, int position) {

                       Intent intent = new Intent(getActivity(), WebViewActivity.class);
                       Map<String, Object> map;
                       map = data.get(position);
                       intent.putExtra("id", map.get(idList).toString());
                       startActivity(intent);

                   }

                   @Override
                   public void onClick(View view) {

                   }
               });
            }
        }


        AsyncOrzuTasks catTask = new AsyncOrzuTasks();
        catTask.execute();


        return view;
    }
         private Map<String, Object> readMessage(JsonReader reader) throws IOException {
            long id = 1;
            String text = null;
            String date = null;
            String update = null;
            String needfrom = null;
            String needto = null;
            String workwith = null;
            String catid = null;
            String subcut = null;
            String price = null;
            Map<String, Object> m = new HashMap<>();
            Map<String, Object> m_new = new HashMap<>();
             String categoryList = "Категория задачи";
             String taskList = "Задание";
             String idList = "ID";
             String catidList = "CatID";
             String priceList= "Цена";
             String servList= "За услугу";
             String dateList= "Дата публицации";
             String updateList= "Дата обновления";
             String needListdfrom= "Сроки";
            reader.beginObject();
            while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("id")) {
                        id = reader.nextLong();
                        m.put(idList, id);
                    } else if (name.equals("task")) {
                        text = reader.nextString();
                        m.put(taskList, text);
                        m.put(servList, "За услугу");
                    } else if (name.equals("cdate_l")) {
                        if (reader.peek() != JsonToken.NULL) {
                            date = reader.nextString();
                            m.put(needListdfrom, "До: " + date);
                        } else reader.skipValue();
                    } else if (name.equals("amount")) {
                        price = reader.nextString();
                        m.put(priceList, price);
                    } else if (name.equals("sub_cat_name")) {
                        subcut = reader.nextString();
                        m.put(categoryList, subcut);
                    } else if (name.equals("updated_at")) {
                        update = reader.nextString();
                        m.put(updateList, "Дата обновления: " + update);
                    } else if (name.equals("cdate")) {
                        if (reader.peek() != JsonToken.NULL) {
                            needfrom = reader.nextString();
                            m.put(needListdfrom, "Начало: " + needfrom);
                        } else reader.skipValue();
                    } else if (name.equals("edate")) {
                        if (reader.peek() != JsonToken.NULL) {
                            needto = reader.nextString();
                            m.put(needListdfrom, m.get(needListdfrom) + "  Конец: " + needto);
                        }  else reader.skipValue();
                    } else if (name.equals("work_with")) {
                        if (reader.peek() != JsonToken.NULL) {
                            workwith = reader.nextString();
                            m.put(needListdfrom, "Дата по договоренности");
                        }  else reader.skipValue();
                    } else if (name.equals("sub_cat_id")) {
                        catid = reader.nextString();
                        m.put(catidList, catid);
                    } else {
                        reader.skipValue();
                    }

                final SharedPreferences prefs =  getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
                /*SharedPreferences.Editor editor = prefs.edit();
                editor.putString("cat", "74");
                editor.apply();*/
                Log.e("asdgasdgasasdgasd", prefs.getString("cat", "all"));
                if (prefs.contains("cat")) {

                    if (prefs.getString("cat", "all").equals("all")){

                        m_new.put(idList, m.get(idList));
                        m_new.put(taskList, m.get(taskList));
                        m_new.put(categoryList, m.get(categoryList));
                        m_new.put(priceList, m.get(priceList));
                        m_new.put(servList, m.get(servList));
                        m_new.put(dateList, m.get(dateList));
                        m_new.put(updateList, m.get(updateList));
                        m_new.put(needListdfrom, m.get(needListdfrom));
                        m_new.put(catidList, m.get(catidList));

                    }  else {

                    if (prefs.getString("cat", "").equals(m.get(catidList))){

                            m_new.put(idList, m.get(idList));
                            m_new.put(taskList, m.get(taskList));
                            m_new.put(categoryList, m.get(categoryList));
                            m_new.put(priceList, m.get(priceList));
                            m_new.put(servList, m.get(servList));
                            m_new.put(dateList, m.get(dateList));
                            m_new.put(updateList, m.get(updateList));
                            m_new.put(needListdfrom, m.get(needListdfrom));
                            m_new.put(catidList, m.get(catidList));
                    }}

                }
            }

            reader.endObject();
            return m_new;
         }
}

