package orzu.org;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import orzu.org.ui.login.model;

public class Fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Map<String, Object>> data;
    ArrayList<Map<String, Object>> truedata;
    RecyclerView rv;
    RecyclerView category_rv;
    RecyclerView subcategory_rv;
    MainCategoryAdapter adapter_category;
    MainSubCategoryAdapter adapter_subcategory;
    RVAdapter adapter;
    Map<String, Object> m = new HashMap<>();
    Map<String, Object> m_new = new HashMap<>();
    Map<String, Object> m_new_2 = new HashMap<>();
    Map<String, Object> m_ref = new HashMap<>();
    ArrayList<Map<String, Object>> filtered = new ArrayList<>();
    final String categoryList = "Категория задачи";
    final String taskList = "Задание";
    String idList = "ID";
    String catidList = "CatID";
    final String priceList = "Цена";
    final String servList = "Валюта";
    final String dateList = "Дата публицации";
    final String cityList = "Город";
    final String needList = "Сроки";
    String needListdfrom = "Сроки";
    int count;
    int countItem;
    boolean countPager = true;
    SwipeRefreshLayout swipeLayout;
    AsyncOrzuTasksMain catTask;
    AsyncOrzuTasksMainRefresh catTaskRef;
    Boolean track = true;
    Boolean noTasks = true;

    AsyncOrzuTasksFind catTaskFind;

    ImageView imagenotask;
    TextView textnotask;
    TextView create_task_main;
    Dialog dialog;
    int status;
    ShimmerFrameLayout shim;
    NestedScrollView nestshimmer;
    CardView cardMain;
    EditText editFind;
    String edittextFind;

    List<category_model> categories = new ArrayList<>();
    List<category_model> subcategories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        nestshimmer = view.findViewById(R.id.nestshimmer);

        imagenotask = view.findViewById(R.id.imageNoTask);
        textnotask = view.findViewById(R.id.textViewNoTask);
        category_rv = view.findViewById(R.id.category_main_rv);
        subcategory_rv = view.findViewById(R.id.subcategory_main_rv);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_sub
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        category_rv.setLayoutManager(layoutManager);
        subcategory_rv.setLayoutManager(layoutManager_sub);

        create_task_main = view.findViewById(R.id.create_task_main);
        editFind = view.findViewById(R.id.editFind);
        editFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edittextFind = String.valueOf(editFind.getText());
                catTaskFind = new AsyncOrzuTasksFind();
                catTaskFind.execute();
            }
        });

        shim = (ShimmerFrameLayout) view.findViewById(R.id.parentShimmerLayout);
        shim.startShimmer();
        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.nestedScrollView);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryLight),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));
        truedata = new ArrayList<>();
        count = 1;
        countItem = 1;
        catTask = new AsyncOrzuTasksMain();
        catTask.execute();

        create_task_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateTaskCategory.class);
                startActivity(intent);
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    swipeLayout.setEnabled(llm.findFirstCompletelyVisibleItemPosition() == 0 || adapter.getItemCount() == 0);
                    if (countPager) {
                        catTask = new AsyncOrzuTasksMain();
                        catTask.execute();
                    }

                }
            }
        });
        categories.add(new category_model("0", "Все категорий", "0"));
        getCategories();
        getSubCategories("1");
        adapter_category = new MainCategoryAdapter(getContext(), categories);
        category_rv.setAdapter(adapter_category);
        adapter_subcategory = new MainSubCategoryAdapter(getContext(), subcategories);
        subcategory_rv.setAdapter(adapter_subcategory);
        MainSubCategoryAdapter.setSelect(new NameItemSelect() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                filterByCategory(subcategories.get(position).getName());

            }

            @Override
            public void onClick(View view) {

            }
        });
        MainCategoryAdapter.setSelect(new NameItemSelect() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                if(position==0){
                    adapter = new RVAdapter(getContext(), truedata);
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    getSubCategories(categories.get(position).getId());
                }
                adapter_category.changeColor(position);
            }

            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }


    void filterByCategory(String category) {
        filtered.clear();
        if (category.length() != 0) {
            if(truedata.size()!=0) {
                for (int i = 0; i < truedata.size(); i++) {
                    if (truedata.get(i).get("Категория задачи").toString().equals(category)) {
                        Log.e("ФильтрыФтльтры", truedata.get(i).get("Категория задачи").toString() + "  " + category);
                        filtered.add(truedata.get(i));
                    }
                }
                adapter = new RVAdapter(getContext(), filtered);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        if(filtered.size()!=0){
            imagenotask.setVisibility(View.INVISIBLE);
            textnotask.setVisibility(View.INVISIBLE);
        }else{
            imagenotask.setVisibility(View.VISIBLE);
            textnotask.setVisibility(View.VISIBLE);
        }
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
        String priceVal = null;
        Map<String, Object> m = new HashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    if (reader.peek() != JsonToken.NULL) {
                        id = reader.nextLong();
                        m.put(idList, id);
                    } else reader.skipValue();
                    break;
                case "task":
                    if (reader.peek() != JsonToken.NULL) {
                        text = reader.nextString();
                        m.put(taskList, text);
                    } else reader.skipValue();
                    break;
                case "current":
                    if (reader.peek() != JsonToken.NULL) {
                        text = reader.nextString();
                        m.put(servList, text);
                    } else reader.skipValue();
                    break;
                case "cdate_l":
                    if (reader.peek() != JsonToken.NULL) {
                        date = reader.nextString();
                        m.put(needListdfrom, "До: " + date);
                    } else reader.skipValue();
                    break;
                case "amount":
                    if (reader.peek() != JsonToken.NULL) {
                        price = reader.nextString();
                        if (price.equals("Предложите цену")) {
                            m.put(priceList, price);
                        } else m.put(priceList, price);
                    } else reader.skipValue();
                    break;
                case "sub_cat_name":
                    if (reader.peek() != JsonToken.NULL) {
                        subcut = reader.nextString();
                        m.put(categoryList, subcut);
                    } else reader.skipValue();
                    break;
                case "city":
                    if (reader.peek() != JsonToken.NULL) {
                        update = reader.nextString();
                        m.put(cityList, update);
                    } else reader.skipValue();
                    break;
                case "cdate":
                    if (reader.peek() != JsonToken.NULL) {
                        needfrom = reader.nextString();
                        m.put(needListdfrom, "Начало: " + needfrom);
                    } else reader.skipValue();
                    break;
                case "edate":
                    if (reader.peek() != JsonToken.NULL) {
                        needto = reader.nextString();
                        m.put(needListdfrom, m.get(needListdfrom) + "  Конец: " + needto);
                    } else reader.skipValue();
                    break;
                case "work_with":
                    if (reader.peek() != JsonToken.NULL) {
                        reader.nextString();
                        m.put(needListdfrom, "Дата по договоренности");
                    } else reader.skipValue();
                    break;
                case "sub_cat_id":
                    if (reader.peek() != JsonToken.NULL) {
                        catid = reader.nextString();
                        m.put(catidList, catid);
                    } else reader.skipValue();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return m;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.up_menu, menu);

        Drawable drawable = menu.findItem(R.id.new_game).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.colorTextDark));

        menu.findItem(R.id.new_game).setIcon(drawable);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_game) {
            Intent intent = new Intent(getActivity(), FiltersActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AsyncOrzuTasksMain extends AsyncTask<String, String, ArrayList<Map<String, Object>>> {

        String result;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imagenotask.setVisibility(View.INVISIBLE);
            textnotask.setVisibility(View.INVISIBLE);
        }


        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... strings) {

            URL orzuEndpoint = null;
            JsonReader jsonReader = null;
            HttpsURLConnection myConnection = null;
            HttpsURLConnection myConnectiontrack = null;
            final char dm = (char) 34;

            try {
                orzuEndpoint = new URL("https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_task&tasks=all&status=open&page=" + count);
                myConnectiontrack =
                        (HttpsURLConnection) orzuEndpoint.openConnection();
                if (myConnectiontrack.getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnectiontrack.getInputStream();
                    Scanner s = new Scanner(responseBody).useDelimiter("\\A");
                    result = s.hasNext() ? s.next() : "";
                    Log.e("RESULT", result);
                }
                myConnection =
                        (HttpsURLConnection) orzuEndpoint.openConnection();
                if (myConnection.getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    jsonReader = new JsonReader(responseBodyReader);
                }

                status = myConnection.getResponseCode();


                myConnection.setInstanceFollowRedirects(true);
                data = new ArrayList<>();

                if (result.equals(dm + "Not tasks yet" + dm)) {
                    track = false;
                    catTask.cancel(true);
                } else {
                    jsonReader.beginArray(); // Start processing the JSON object
                    while (jsonReader.hasNext()) { // Loop through all keys

                        countItem++;
                        if (countItem == 6) {
                            countPager = true;
                            countItem = 1;
                            count++;
                        } else countPager = false;

                        m = new HashMap<>();
                        m_new = new HashMap<>();
                        m_new_2 = new HashMap<>();
                        m = readMessage(jsonReader);
                        Long[] savedList = model.array;

                        Long det = 0L;

                        if (savedList != null) {
                            for (int i = 0; i < savedList.length; i++) {
                                if (savedList[i] != null && savedList[i] != 0) {
                                    det = savedList[i];
                                }
                            }
                        }

                        if (det == 0L) {
                            if (Common.city.length() != 0) {
                                if (Common.city.equals(m.get(cityList))) {
                                    m_new.put(idList, m.get(idList));
                                    m_new.put(taskList, m.get(taskList));
                                    m_new.put(categoryList, m.get(categoryList));
                                    m_new.put(priceList, m.get(priceList));
                                    m_new.put(servList, m.get(servList));
                                    m_new.put(dateList, m.get(dateList));
                                    m_new.put(cityList, m.get(cityList));
                                    m_new.put(needListdfrom, m.get(needListdfrom));
                                    m_new.put(catidList, m.get(catidList));
                                }
                            } else {
                                m_new.put(idList, m.get(idList));
                                m_new.put(taskList, m.get(taskList));
                                m_new.put(categoryList, m.get(categoryList));
                                m_new.put(priceList, m.get(priceList));
                                m_new.put(servList, m.get(servList));
                                m_new.put(dateList, m.get(dateList));
                                m_new.put(cityList, m.get(cityList));
                                m_new.put(needListdfrom, m.get(needListdfrom));
                                m_new.put(catidList, m.get(catidList));
                            }

                        } else {
                            for (int i = 0; i < savedList.length; i++) {

                                if (savedList[i] != null) {
                                    if (savedList[i].toString().equals(m.get(catidList).toString())) {
                                        if (Common.city.length() != 0) {
                                            if (Common.city.equals(m.get(cityList))) {
                                                m_new.put(idList, m.get(idList));
                                                m_new.put(taskList, m.get(taskList));
                                                m_new.put(categoryList, m.get(categoryList));
                                                m_new.put(priceList, m.get(priceList));
                                                m_new.put(servList, m.get(servList));
                                                m_new.put(dateList, m.get(dateList));
                                                m_new.put(cityList, m.get(cityList));
                                                m_new.put(needListdfrom, m.get(needListdfrom));
                                                m_new.put(catidList, m.get(catidList));
                                            }
                                        } else {
                                            m_new.put(idList, m.get(idList));
                                            m_new.put(taskList, m.get(taskList));
                                            m_new.put(categoryList, m.get(categoryList));
                                            m_new.put(priceList, m.get(priceList));
                                            m_new.put(servList, m.get(servList));
                                            m_new.put(dateList, m.get(dateList));
                                            m_new.put(cityList, m.get(cityList));
                                            m_new.put(needListdfrom, m.get(needListdfrom));
                                            m_new.put(catidList, m.get(catidList));
                                        }
                                    }
                                }
                            }
                        }

                        if (!m_new.isEmpty()) {
                            data.add(m_new);

                        }

                    }
                    truedata.addAll(data);
                    jsonReader.endArray();
                    jsonReader.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if (status != 200) {
                    Fragment1.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            imagenotask.setVisibility(View.VISIBLE);
                            textnotask.setVisibility(View.VISIBLE);
                            dialog = new Dialog(Fragment1.this.getActivity(), android.R.style.Theme_Material_Light_NoActionBar);
                            dialog.setContentView(R.layout.dialog_no_internet);
                            Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    catTask = new AsyncOrzuTasksMain();
                                    catTask.execute();
                                    dialog.dismiss();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            }, 500);


                            catTask.cancel(true);
                        }
                    });
                }
                e.printStackTrace();
            }
            myConnection.disconnect();
            return null;
        }

        protected void onPostExecute(ArrayList<Map<String, Object>> result) {
            super.onPostExecute(result);
            adapter = new RVAdapter(getContext(), truedata);
            rv.setAdapter(adapter);

            RVAdapter.setSelect(new MainItemSelect() {
                @Override
                public void onItemSelectedListener(View view, int position) {

                    Intent intent = new Intent(getActivity(), TaskViewMain.class);
                    Map<String, Object> map;
                    map = truedata.get(position);
                    intent.putExtra("id", map.get(idList).toString());
                    intent.putExtra("opt", "view");
                    intent.putExtra("mytask", "not");
                    startActivity(intent);

                }

                @Override
                public void onClick(View view) {

                }
            });
            noTasks = false;

            if (adapter.getItemCount() == 0) {
                noTasks = true;
                imagenotask.setVisibility(View.VISIBLE);
                textnotask.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        catTaskRef = new AsyncOrzuTasksMainRefresh();
        catTaskRef.execute();
    }

    class AsyncOrzuTasksMainRefresh extends AsyncTask<String, String, ArrayList<Map<String, Object>>> {
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imagenotask.setVisibility(View.INVISIBLE);
            textnotask.setVisibility(View.INVISIBLE);

        }

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... strings) {

            orzuEndpoint[0] = null;
            JsonReader[] jsonReader = new JsonReader[1];

            try {

                orzuEndpoint[0] = new URL("https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_task&tasks=all&status=open&page=0");

                myConnection[0] =
                        (HttpsURLConnection) orzuEndpoint[0].openConnection();
                if (myConnection[0].getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection[0].getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    jsonReader[0] = new JsonReader(responseBodyReader);
                    Scanner s = new Scanner(responseBody).useDelimiter("\\A");
                    result = s.hasNext() ? s.next() : "";
                    if (result.equals("\"Not tasks yet\"")) {
                        catTask.cancel(true);
                    }

                }

                status = myConnection[0].getResponseCode();


                myConnection[0].setInstanceFollowRedirects(true);
                data = new ArrayList<>();
                if (jsonReader[0] == null) {
                    track = false;
                } else {
                    m_new_2 = new HashMap<>();
                    jsonReader[0].beginArray();
                    jsonReader[0].beginObject();
                    jsonReader[0].nextName();
                    Long id = jsonReader[0].nextLong();
                    Long[] savedList = model.array;
                    Long det = 0L;

                    if (savedList != null) {
                        for (int i = 0; i < savedList.length; i++) {
                            if (savedList[i] != null && savedList[i] != 0) {
                                det = savedList[i];
                            }
                        }
                    }

                    if (det == 0L) {
                        m_new_2.put(idList, id);

                    } else {
                        for (int i = 0; i < savedList.length; i++) {

                            if (savedList[i] != null) {
                                if (savedList[i].toString().equals(m.get(catidList).toString())) {
                                    m_new_2.put(idList, id);

                                }
                            }
                        }
                    }

                    jsonReader[0].close();


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if (status != 200) {
                    Fragment1.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            imagenotask.setVisibility(View.VISIBLE);
                            textnotask.setVisibility(View.VISIBLE);
                            dialog = new Dialog(Fragment1.this.getActivity(), android.R.style.Theme_Material_Light_NoActionBar);
                            dialog.setContentView(R.layout.dialog_no_internet);
                            Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    catTaskRef = new AsyncOrzuTasksMainRefresh();
                                    catTaskRef.execute();
                                    dialog.dismiss();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            }, 500);


                            catTaskRef.cancel(true);
                        }
                    });
                }
                e.printStackTrace();
            }
            myConnection[0].disconnect();
            return null;
        }

        protected void onPostExecute(ArrayList<Map<String, Object>> result) {
            super.onPostExecute(result);
            if (truedata.size() != 0) {
                m_ref = truedata.get(0);
                Long idold = (Long) m_ref.get(idList);
                Long idnew = (Long) m_new_2.get(idList);
                if (!idold.equals(idnew) || noTasks) {
                    count = 1;
                    imagenotask.setVisibility(View.INVISIBLE);
                    textnotask.setVisibility(View.INVISIBLE);
                    truedata = new ArrayList<>();
                    data = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    catTask = new AsyncOrzuTasksMain();
                    catTask.execute();
                }
            } else {
                imagenotask.setVisibility(View.VISIBLE);
                textnotask.setVisibility(View.VISIBLE);
            }


        }
    }


    class AsyncOrzuTasksFind extends AsyncTask<String, String, ArrayList<Map<String, Object>>> {
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imagenotask.setVisibility(View.INVISIBLE);
            textnotask.setVisibility(View.INVISIBLE);
            truedata = new ArrayList<>();
            adapter.notifyDataSetChanged();
        }


        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... strings) {

            orzuEndpoint[0] = null;
            JsonReader[] jsonReader = new JsonReader[1];

            try {
                orzuEndpoint[0] = new URL("https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=view_task&tasks=all&search=" + edittextFind);
                myConnection[0] =
                        (HttpsURLConnection) orzuEndpoint[0].openConnection();
                if (myConnection[0].getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection[0].getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    jsonReader[0] = new JsonReader(responseBodyReader);
                }

                status = myConnection[0].getResponseCode();


                myConnection[0].setInstanceFollowRedirects(true);
                data = new ArrayList<>();
                if (jsonReader[0] == null) {
                    track = false;
                } else {
                    jsonReader[0].beginArray(); // Start processing the JSON object
                    while (jsonReader[0].hasNext()) { // Loop through all keys

//                        countItem++;
//                        if (countItem == 6){
//                            countPager = true;
//                            countItem=1;
//                            count++;
//                        } else countPager = false;

                        m = new HashMap<>();
                        m_new = new HashMap<>();
                        m_new_2 = new HashMap<>();
                        m = readMessage(jsonReader[0]);


                        m_new.put(idList, m.get(idList));
                        m_new.put(taskList, m.get(taskList));
                        m_new.put(categoryList, m.get(categoryList));
                        m_new.put(priceList, m.get(priceList));
                        m_new.put(servList, m.get(servList));
                        m_new.put(dateList, m.get(dateList));
                        m_new.put(cityList, m.get(cityList));
                        m_new.put(needListdfrom, m.get(needListdfrom));
                        m_new.put(catidList, m.get(catidList));

                        if (!m_new.isEmpty()) {
                            data.add(m_new);

                        }

                    }
                    truedata.addAll(data);
                    jsonReader[0].endArray();


                    jsonReader[0].close();


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                if (status != 200) {
                    Fragment1.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            imagenotask.setVisibility(View.VISIBLE);
                            textnotask.setVisibility(View.VISIBLE);
                            dialog = new Dialog(Fragment1.this.getActivity(), android.R.style.Theme_Material_Light_NoActionBar);
                            dialog.setContentView(R.layout.dialog_no_internet);
                            Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    catTask = new AsyncOrzuTasksMain();
                                    catTask.execute();
                                    dialog.dismiss();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            }, 500);


                            catTaskFind.cancel(true);
                        }
                    });
                }
                e.printStackTrace();
            }
            myConnection[0].disconnect();
            return null;
        }

        protected void onPostExecute(ArrayList<Map<String, Object>> result) {
            super.onPostExecute(result);

            adapter = new RVAdapter(getContext(), truedata);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            RVAdapter.setSelect(new MainItemSelect() {
                @Override
                public void onItemSelectedListener(View view, int position) {

                    Intent intent = new Intent(getActivity(), TaskViewMain.class);
                    Map<String, Object> map;
                    map = truedata.get(position);
                    intent.putExtra("id", map.get(idList).toString());
                    intent.putExtra("opt", "view");
                    intent.putExtra("mytask", "not");
                    startActivity(intent);

                }

                @Override
                public void onClick(View view) {

                }
            });

            if (adapter.getItemCount() == 0) {
                noTasks = true;
                imagenotask.setVisibility(View.VISIBLE);
                textnotask.setVisibility(View.VISIBLE);
            }

        }
    }

    private void getCategories() {

        String requestUrl = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_parent";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);

                    for (int i = 0; i < j.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
                            categories.add(new category_model(object.getString("id"), object.getString("name"), object.getString("parent_id")));
                            adapter_category = new MainCategoryAdapter(getContext(), categories);
                            category_rv.setAdapter(adapter_category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
            }
        });
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(stringRequest);
    }

    private void getSubCategories(String id) {
        subcategories.clear();
        String requestUrl = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_subcat&id=" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);

                    for (int i = 0; i < j.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
                            subcategories.add(new category_model(object.getString("id"), object.getString("name"), object.getString("parent_id")));
                            adapter_subcategory = new MainSubCategoryAdapter(getContext(), subcategories);
                            subcategory_rv.setAdapter(adapter_subcategory);
                            adapter_category.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    shim.setVisibility(View.INVISIBLE);
                    nestshimmer.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
            }
        });
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(stringRequest);
    }


}

