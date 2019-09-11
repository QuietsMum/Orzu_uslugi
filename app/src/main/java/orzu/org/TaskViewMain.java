package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import io.intercom.com.google.gson.JsonArray;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orzu.org.ui.login.model;

public class TaskViewMain extends AppCompatActivity implements View.OnClickListener {

    String id = "";
    String opt= "";
    String myTask= "";
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m = new HashMap<>();
    Map<String, Object> m_new = new HashMap<>();
    final String categoryList = "Категория задачи";
    final String taskList = "Задание";
    String narrList = "Описание";
    String idList = "ID";
    String useridList = "IDUser";
    String catidList = "CatID";
    String adress = "Adress";
    String param = "Param";
    final String priceList= "Цена";
    final String priceList2= "Валюта";
    final String createList= "Создано";
    final String servList= "За услугу";
    final String dateList= "Дата публицации";
    final String cityList= "Город";
    final String needList= "Сроки";
    String needListdfrom= "Сроки";
    ProgressBar progressBar;
    AsyncOrzuTask task;
    TextView taskName;
    TextView taskDate;
    TextView taskAdr;
    TextView taskBeg;
    TextView taskAmt;
    TextView taskAmtOnce;
    TextView taskAmtTo;
    TextView taskNarr;
    TextView taskOpen;
    TextView nav_user;
    TextView sad;
    TextView nat;
    TextView hap;
    ImageView taskMaket;
    ImageView taskMaketBack;
    View taskMaketView;
    String mMessage;
    String mName;
    String image;
    String mFiName;
    String text;
    CardView carddis;
    ShimmerFrameLayout shim;
    TextView buttonGettask;
    TextView buttonGettaskShim;
    LinearLayout viewUserInfo;

    String name = "";
    String idUser = "";
    String nameUser = "";
    String catid = "";
    String amout = "";
    String amoutVal = "";
    String cdate = "";
    String edate = "";
    String cdate_l = "";
    String level_l = "";
    String work_with = "";
    String city = "";
    String address = "";
    String narrative = "";
    String location = "123";
    String locationVal = "123";
    String date = "";

    ImageView imageViewName;

    int datetype;
    int placetype;
    int pricetype;

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        setContentView(R.layout.activity_task_view_main);
        Intent intent = getIntent();
        //id = new ;
        if (intent != null)
        {
            id = "" + intent.getStringExtra("id");
            opt = intent.getStringExtra("opt");
            myTask = intent.getStringExtra("mytask");
        }
        getSupportActionBar().setTitle("Задание №" + id);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        shim = (ShimmerFrameLayout) findViewById(R.id.parentShimmerLayoutViewTask);
        shim.startShimmer();

        buttonGettask = findViewById(R.id.button_gettask);
        buttonGettaskShim = findViewById(R.id.button_gettask_shim);
        buttonGettask.setOnClickListener(this);

        viewUserInfo = findViewById(R.id.view_user_info);
        viewUserInfo.setOnClickListener(this);

        taskName = findViewById(R.id.taskName);
        sad = findViewById(R.id.textSad);
        nat = findViewById(R.id.textNatural);
        hap = findViewById(R.id.textHappy);

        taskAdr = findViewById(R.id.taskAdr);
        taskBeg = findViewById(R.id.taskDate);
        taskAmt = findViewById(R.id.taskPrice);
        taskAmtOnce = findViewById(R.id.taskPriceOnce);
        taskAmtTo = findViewById(R.id.taskMoney);
        taskNarr = findViewById(R.id.taskNarr);
        taskOpen = findViewById(R.id.taskOpen);
        nav_user = findViewById(R.id.taskCreator);
        //taskMaket = findViewById(R.id.maket_task);
        taskMaketBack = findViewById(R.id.maket_task_white);
        taskMaketView = findViewById(R.id.maket_view);
        imageViewName = findViewById(R.id.imageViewName);
        //carddis = findViewById(R.id.carddis);
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        //taskMaket.startAnimation(animation); //to start animation

        if(myTask.equals("my")){

            buttonGettask.setText("Посмотреть отклики");
            buttonGettaskShim.setText("Посмотреть отклики");
            buttonGettask.setBackgroundColor(getResources().getColor(R.color.colorBackMyTask));
            buttonGettaskShim.setBackgroundColor(getResources().getColor(R.color.colorBackMyTask));
        }

        if (opt.equals("view")){
            task = new AsyncOrzuTask();
            task.execute();
        } else {
            // DATE TYPE
            // 1 - в любое время 
            // 2 - утром (до 12)
            // 3 - днем (с 12 до 17)
            // 4 - вечером (с 17 до 22)
            // 5 - ночью (после 22)
            getSupportActionBar().setTitle("Создать задание");
            buttonGettask.setText("Опубликовать");
            taskMaketView.setVisibility(View.INVISIBLE);
            taskMaketBack.setVisibility(View.INVISIBLE);
            shim.setVisibility(View.INVISIBLE);

            final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);

            datetype = Integer.parseInt(prefs.getString(Util.TASK_DATETYPE, ""));
            placetype = Integer.parseInt(prefs.getString(Util.TASK_PLACETYPE, ""));
            pricetype = Integer.parseInt(prefs.getString(Util.TASK_PPRICE_TYPE, ""));

            name = prefs.getString(Util.TASK_NAME, "");
            nameUser = prefs.getString(Util.TASK_USERNAME, "");
            address = prefs.getString(Util.TASK_PLACE, "");
            cdate = prefs.getString(Util.TASK_CDATE, "");
            edate = prefs.getString(Util.TASK_EDATE, "");
            cdate_l = prefs.getString(Util.TASK_CDATEL, "");
            level_l = prefs.getString(Util.TASK_LEVEL_L, "");
            amout = prefs.getString(Util.TASK_PPRICE, "");
            catid = prefs.getString(Util.CAT_ID, "");
            narrative = prefs.getString(Util.TASK_ANNAT, "");

            if (datetype == 2){
                date = "С: " + cdate + " До: " + edate;
            } if (datetype == 1) {
                date = cdate_l + " " + level_l;
            } if (datetype == 3) {
                date = "Дата по договоренности";
            }

            Log.e("VALUEs", name + " " + catid + " " + address + " " + cdate + " " + edate + " " + amout + " " + narrative + " " + nameUser);
            String outputPattern = "EEE, d MMMM yyyy HH:mm:ss";
            Locale myLocale = new Locale("ru","RU");
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, myLocale);
            Date currentTime = Calendar.getInstance().getTime();
            taskName.setText(name);
            taskNarr.setText(narrative);
            taskAmt.setText(amout);
            taskAdr.setText(address);
            taskBeg.setText(date);
            taskOpen.setText(outputFormat.format(currentTime));
            nav_user.setText(nameUser);
            imageViewName.setImageBitmap(Common.bitmap);
            if (taskAmt.equals("Предложите цену")){
                taskAmtOnce.setVisibility(View.INVISIBLE);
            }
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_gettask:
                if (!opt.equals("view")){

                    try {
                        getCreateResponse();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else  if(myTask.equals("my")) {
                    Intent intent = new Intent(this, FeedbackTask.class);
                    Common.taskId = id;
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, AddSuggest.class);
                    intent.putExtra("idTaskSuggest", id);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.view_user_info:
                Intent intent = new Intent(getApplication(), UserView.class);
                intent.putExtra("idhis", m.get(useridList).toString());
                startActivity(intent);
                break;

        }

    }


    class AsyncOrzuTask extends AsyncTask<String,String, ArrayList<Map<String, Object>>> {
        final HttpsURLConnection[] myConnection = new HttpsURLConnection[1];
        final URL[] orzuEndpoint = new URL[1];


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... strings)  {
            m = new HashMap<>();
            m_new = new HashMap<>();

            m.put(priceList, "Предложите цену");
            m.put(priceList2, "");
            m.put(adress, "Удаленно");
            m.put(needListdfrom, "Дата по договоренности");
            m.put(narrList, "");

            orzuEndpoint[0] = null;
            JsonReader[] jsonReader = new JsonReader[1];
            try {
                orzuEndpoint[0] = new URL("https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_task&tasks=" + id);
                myConnection[0] =
                        (HttpsURLConnection) orzuEndpoint[0].openConnection();
                if (myConnection[0].getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection[0].getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    jsonReader[0] = new JsonReader(responseBodyReader);

                }

                jsonReader[0].beginArray();
                while (jsonReader[0].hasNext()) {
                    jsonReader[0].beginArray(); // Start processing the JSON object
                    while (jsonReader[0].hasNext()) { // Loop through all keys

                        m = readMessage(jsonReader[0]);

                        m_new.put(idList, m.get(idList));
                        m_new.put(taskList, m.get(taskList));
                        m_new.put(narrList, m.get(narrList));
                        m_new.put(priceList, m.get(priceList));
                        m_new.put(adress, m.get(adress));
                        m_new.put(createList, m.get(createList));
                        m_new.put(priceList2, m.get(priceList2));
                        m_new.put(needListdfrom, m.get(needListdfrom));
                        //  m_new.put(catidList, m.get(catidList));
                    }
                    jsonReader[0].endArray();
                }   jsonReader[0].endArray();

                    jsonReader[0].close();
                    myConnection[0].disconnect();


            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

            try {
                getUserResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(ArrayList<Map<String, Object>> result) {
            super.onPostExecute(result);
            //progressBar.setVisibility(View.INVISIBLE);

            taskName.setText(m_new.get(taskList).toString());
            taskNarr.setText(m_new.get(narrList).toString());
            taskAmt.setText(m_new.get(priceList).toString());
            taskAmtOnce.setText(m_new.get(priceList2).toString());
            taskAdr.setText(m_new.get(adress).toString());
            taskBeg.setText(m_new.get(needListdfrom).toString());
            taskOpen.setText(m_new.get(createList).toString());

            if (m_new.get(priceList).toString().equals("Предложите цену")){
                taskAmtOnce.setVisibility(View.INVISIBLE);
            }

        }
    }

    private Map<String, Object> readMessage(JsonReader reader) throws IOException {
        long id = 1;
        long userid = 1;
        String text = null;
        String jreader = null;
        String narr = null;
        String adr = null;
        String date = null;
        String update = null;
        String needfrom = null;
        String needto = null;
        String workwith = null;
        String creat = null;
        long catid = 1;
        String subcut = null;
        String price = null;
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
                    case "cat_id":
                        if (reader.peek() != JsonToken.NULL) {
                            catid = reader.nextLong();
                            m.put(catidList, catid);
                        } else reader.skipValue();
                        break;
                    case "created_at":
                        if (reader.peek() != JsonToken.NULL) {
                            creat = reader.nextString();
                            m.put(createList, parseDateToddMMyyyy(creat));
                        } else reader.skipValue();
                        break;
                    case "narrative":
                        if (reader.peek() != JsonToken.NULL) {
                            narr = reader.nextString();
                            m.put(narrList, narr);
                        } else reader.skipValue();
                        break;
                    case "user_id":
                        if (reader.peek() != JsonToken.NULL) {
                            userid = reader.nextLong();
                            m.put(useridList, userid);
                        } else reader.skipValue();
                        break;

                    case "param":
                        param = reader.nextString();
                        if (reader.peek() != JsonToken.NULL) {
                            switch (param) {
                                case "address":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(adress, adr);
                                    break;
                                case "remote":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(adress, "Удалённо");
                                    break;
                               case "current":
                                    reader.nextName();
                                   if (reader.peek() != JsonToken.NULL) {
                                       adr = " " + reader.nextString();
                                       m.put(priceList2, adr);
                                   } else {
                                       m.put(priceList2, " орзуcoin");
                                       reader.skipValue();
                                   }

                                    break;
                                case "amout":
                                    reader.nextName();
                                    price = reader.nextString();
                                    m.put(priceList, price);
                                    break;
                                case "no_amount":
                                    reader.nextName();
                                    price = reader.nextString();
                                    m.put(priceList, "Предложите цену");
                                    break;
                                case "cdate_l":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(needListdfrom, adr);
                                    break;
                                case "cdate":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(needListdfrom, adr);
                                    break;
                                case "edate":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(needListdfrom, "С: " + adr +  "  До: " + m.get(needListdfrom));
                                    break;
                                case "city":
                                    reader.nextName();
                                    if (reader.peek() != JsonToken.NULL) {
                                        adr = reader.nextString();
                                        m.put(cityList, adr);
                                    } else {
                                        m.put(cityList, "Неизвестно");
                                        reader.skipValue();
                                    }

                                    break;
                                case "work_with":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put(needListdfrom, "Дата по договоренности");
                                    break;
                                case "level_l":
                                    reader.nextName();
                                    adr = reader.nextString();
                                    m.put("level_l", adr);
                                    break;
                            }
                        } else reader.skipValue();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }  reader.endObject();
            return m;

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

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "EEE, d MMMM yyyy HH:mm:ss";
        String outputPatternTime = "HH:mm";
        Locale myLocale = new Locale("ru","RU");
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, myLocale);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, myLocale);
        SimpleDateFormat outputFormatTime = new SimpleDateFormat(outputPatternTime, myLocale);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int day = 0;
        String strday = null;
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            strday = (String) android.text.format.DateFormat.format("dd", date);
            day = Integer.parseInt(strday);
            str = "в " + outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (today == day){
            str = "сегодня в " + outputFormatTime.format(date);;
        } else if(today == (day+1)){
            str = "вчера в " + outputFormatTime.format(date);
        }
        return str;
    }

    public void getUserResponse() throws IOException {
        // api?appid=&opt=view_user&=user=id

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_user&user=" + m.get(useridList);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    mName = jsonObject.getString("name");
                    image = jsonObject.getString("avatar");
                    mFiName = jsonObject.getString("fname");
                    String sadnum = String.valueOf(jsonObject.getLong("sad"));
                    String natnum = String.valueOf(jsonObject.getLong("neutral"));
                    String hapnum = String.valueOf(jsonObject.getLong("happy"));
                    if (mFiName.equals("null")){
                        text = mName;
                    } else text = mName + " " + mFiName;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nav_user.setText(text);
                            sad.setText(sadnum);
                            nat.setText(natnum);
                            hap.setText(hapnum);
                            Picasso.get().load("https://orzu.org"+image).into(imageViewName);
                        }
                    });

                    /*taskMaket.clearAnimation();
                    taskMaket.setVisibility(View.INVISIBLE);
                    carddis.setVisibility(View.INVISIBLE);*/


                    taskMaketView.setVisibility(View.INVISIBLE);
                    taskMaketBack.setVisibility(View.INVISIBLE);
                    shim.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace(); }

            }
        });
    }

    public void getCreateResponse() throws IOException {
        // api?appid=&opt=view_user&=user=id
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int tokenColIndex = c.getColumnIndex("id");
        idUser = c.getString(tokenColIndex);
        c.moveToFirst();
        c.close();
        db.close();

        // DATE TYPE
        // 1. Точная дата 
        // 2. Указать период 
        // 3. Договорюсь с исполнителем
        //   SPINNER NUMBER
        //   1 - в любое время 
        //   2 - утром (до 12)
        //   3 - днем (с 12 до 17)
        //   4 - вечером (с 17 до 22)
        //   5 - ночью (после 22)
        // PLACE TYPE
        // 1. Указать место 
        // 2. Удаленно
        // PRICE TYPE
        // 1. Указать цену самому 
        // 2. Исполнитель предложит цену

        String urlLocation = "";
        String urlTerm = "";
        String urlPrice = "";
        if (datetype == 2){
            urlTerm = "&date=period" + "&periodA=" + cdate + "&periodB=" + edate;
        } if (datetype == 1) {
            urlTerm = "&date=exact" + "&exactD=" + cdate_l + "&exactT=" + level_l;
        } if (datetype == 3) {
            urlTerm = "&date=wtasker";
        }
        if (placetype == 1){
            urlLocation = "&location=indicate" + "&locationVal=" + address;
        } if (placetype == 2) {
            urlLocation = "&location=remote";
        }
        if (pricetype == 1){
            urlPrice = "&price=indicate&priceVal=" + amout;
        } if (pricetype == 2) {
            urlPrice = "&price=wtasker";
        }

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS" +
                "&opt=input_task" +
                "&task=" + name +
                "&catid=" + catid +
                "&narrative=" + narrative +
                "&userid=" + idUser +
                 urlTerm  +
                 urlLocation +
                 urlPrice;
        OkHttpClient client = new OkHttpClient();
        Log.e("create url", url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                final char dm = (char) 34;
                if (mMessage.equals(dm + "Task created" + dm)){
                    Intent intent = new Intent(TaskViewMain.this, Congratz.class);
                    startActivity(intent);
                    finish();
                    CreateTaskDetail.fa.finish();
                    CreateTaskAmout.fa.finish();
                    CreateTaskTerm.fa.finish();
                    CreateTaskPlace.fa.finish();
                    CreateTaskName.fa.finish();
                    CreateTaskCategory.fa.finish();
                    CreateTaskSubCategory.fa.finish();
                }

                Log.e("created response", mMessage);

            }
        });
    }

}
