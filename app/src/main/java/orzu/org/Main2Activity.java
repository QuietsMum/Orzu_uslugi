package orzu.org;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;
import jp.wasabeef.blurry.Blurry;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ArrayList<String> subsServer = new ArrayList<>();
    Fragment fragment = null;
    Class fragmentClass = null;
    JsonReader[] jsonReader;
    JSONObject obj;
    String mMessage;
    String mName;
    String image;
    String mFiName;
    String text;
    String idUser;
    DBHelper dbHelper;
    NavigationView navigationView;
    LinearLayout intercomBtn;
    ImageView img;
    RelativeLayout userviewBtn;
    TextView nav_user_name;
    String modelString;
    String city = "";
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private void setupBeams() {
        PushNotifications.start(getApplicationContext(), "e33cda0a-16d0-41cd-a5c9-8ae60b9b7042");
        PushNotifications.clearDeviceInterests();
        PushNotifications.addDeviceInterest("user_" + idUser);
        PushNotifications.getDeviceInterests();
        for (int i = 0; i < subsServer.size(); i++){
            PushNotifications.addDeviceInterest("cat_" + subsServer.get(i));
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idUser", "1231234");
        cv.put("message", "123123123");
        db.insert("orzunotif", null, cv);
       /* for (Map.Entry<String, String> entry : subsServer.entrySet()) {
            modelString = entry.getValue();
            PushNotifications.addDeviceInterest("debug-" + modelString.substring(modelString.indexOf(";") + 1));
            Log.e("ininin123123123", modelString.substring(modelString.indexOf(";") + 1));
        }*/
    }

    public void requestSubsServerMain() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);

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
                Main2Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(Main2Activity.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestSubsServerMain();
                                dialog.dismiss();
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        }, 500);
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                subsServer =  new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        Object value = jsonObject.get(key);
                        subsServer.add(key);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setupBeams();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i("MyActivity", "Payload was missing");
                } else {
                    Log.i("MyActivity", messagePayload);
                    // Now update the UI based on your message payload!
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorTextDark));

        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_back));
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setBottomRightCorner(CornerFamily.ROUNDED,160)
                        .build());

        SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("notif", false);
        editor.apply();

        try {
            getUserResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }



        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        Common.userId = idUser;
        Common.utoken = c.getString(tokenColIndex);
        c.close();

        requestSubsServerMain();

        ContentValues cv = new ContentValues();
        cv.put("id", "1");
        cv.put("name", "mako");
        cv.put("date", getCurrentTimeStamp());
        cv.put("their_text", "salam kalay");
        db.insert("orzuchat", null, cv);
        cv.put("id", "2");
        cv.put("name", "Nikita");
        cv.put("date", getCurrentTimeStamp());
        cv.put("my_text", "salam kalay");
        db.insert("orzuchat", null, cv);
        cv.put("id", "1");
        cv.put("name", "mako");
        cv.put("date", getCurrentTimeStamp());
        cv.put("my_text", "salam poidet");
        db.insert("orzuchat", null, cv);
        cv.put("id", "2");
        cv.put("name", "Nikita");
        cv.put("date", getCurrentTimeStamp());
        cv.put("their_text", "kalay makalay");
        db.insert("orzuchat", null, cv);
        db.close();
        dbHelper.close();

        Intercom.initialize(getApplication(), "android_sdk-805f0d44d62fbc8e72058b9c8eee61c94c43c874", "p479kps8");

        intercomBtn = findViewById(R.id.techsupp);
        intercomBtn.setOnClickListener(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.filter);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorTextDark));
        toggle.syncState();
        View header = navigationView.getHeaderView(0);
        img = header.findViewById(R.id.textView);
        try {
            getUserResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    public String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);

        if (prefs.getBoolean("openNotification", false)) {
            onNavigationItemSelected(navigationView.getMenu().getItem(3));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("openNotification", false);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.first) {
            fragmentClass = Fragment1.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            toolbar.setBackgroundColor(getResources().getColor(R.color.back_for_feed));
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorTextDark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryGrey));
            // Вставляем фрагмент, заменяя текущий фрагмент
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            // Выделяем выбранный пункт меню в шторке
            item.setChecked(true);
            // Выводим выбранный пункт в заголовке
            setTitle(item.getTitle());

        } else if (id == R.id.second) {
            Intent intent = new Intent(this, CreateTaskCategory.class);
            startActivity(intent);

        } else if (id == R.id.third) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.back_for_feed));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryGrey));
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorTextDark));
            fragmentClass = Fragment4.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Вставляем фрагмент, заменяя текущий фрагмент
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            // Выделяем выбранный пункт меню в шторке
            item.setChecked(true);
            // Выводим выбранный пункт в заголовке
            setTitle(item.getTitle());


        } else if (id == R.id.fourth) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBackgrndFrg));
            fragmentClass = Fragment2.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Вставляем фрагмент, заменяя текущий фрагмент
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            // Выделяем выбранный пункт меню в шторке
            item.setChecked(true);
            // Выводим выбранный пункт в заголовке
            setTitle(item.getTitle());

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment3");
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (data != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new Fragment1()).commit();
        }
    }

    public void getUserResponse() throws IOException {
        // api?appid=&opt=view_user&=user=id
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        Common.userId = idUser;
        Common.utoken = c.getString(tokenColIndex);
        c.close();
        db.close();

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_user&user=" + idUser;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Main2Activity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(Main2Activity.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    getUserResponse();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        }, 500);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    mName = jsonObject.getString("name");
                    mFiName = jsonObject.getString("fname");
                    image = jsonObject.getString("avatar");
                    city = jsonObject.getString("city");
                    if (mFiName.equals("null")) {
                        text = mName;
                    } else text = mName + "\n" + mFiName;





                    final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Util.TASK_USERNAME, mName);
                    if(!city.isEmpty()){
                        editor.putString("UserCityPref", city);
                        editor.apply();
                    }else{
                        Intent intent = new Intent(Main2Activity.this,RegistCity.class);
                        startActivity(intent);
                        finish();
                    }
                    editor.apply();
                    View hView = navigationView.getHeaderView(0);
                    nav_user_name = (TextView) hView.findViewById(R.id.textViewName);
                    userviewBtn = hView.findViewById(R.id.headerOfdrawer);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nav_user_name.setText(text);
                            Common.name = text;
                            Common.name_only = mName;
                            userviewBtn.setOnClickListener(Main2Activity.this);
                            Common.fragmentshimmer = true;
                            new DownloadImage().execute("https://orzu.org" + image);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.techsupp:
                Registration registration = Registration.create().withUserId(idUser);
                UserAttributes userAttributes = new UserAttributes.Builder()
                        .withName(mName)
                        .withUserId(idUser)
                        .build();
                Intercom.client().updateUser(userAttributes);
                Intercom.client().handlePushMessage();
                Intercom.client().registerIdentifiedUser(registration);
                Intercom.client().displayMessenger();
                Intercom.client().setBottomPadding(20);
                break;

            case R.id.headerOfdrawer:
                fragmentClass = Fragment3.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBackgrndFrg));
                // Вставляем фрагмент, заменяя текущий фрагмент
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment, "fragment3").commit();
                setTitle("");
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Util.TASK_USERID, idUser);
                editor.apply();
                break;

        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, "my_image.jpeg");
        }
    }

    ImageView nav_user;

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;

        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
            File file = getApplicationContext().getFileStreamPath("my_image.jpeg");
            if (file.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                View hView = navigationView.getHeaderView(0);
                nav_user = (ImageView) hView.findViewById(R.id.imageViewName);
                nav_user.setImageBitmap(myBitmap);
                Common.bitmap = myBitmap;
                Common.d = nav_user.getDrawable();


            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void changeImage() {
        nav_user.setImageDrawable(Common.d);
        nav_user_name.setText(Common.name);
    }




}
