package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistCity extends AppCompatActivity {

    String[] cities;
    ArrayAdapter<String> adapter;
    Spinner spin;
    TextView regisCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_regist_city);
        regisCity = findViewById(R.id.regisCity);
        spin = findViewById(R.id.user_set_place);
        requestCity();
        regisCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getEditCity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestCity() {
        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=getOther&get=cities";
        OkHttpClient client = new OkHttpClient();
        Log.e("result", "enterFunction");
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegistCity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(RegistCity.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestCity();
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
                Log.e("resultArrayFull", mMessage);
                int i;
                try {
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int lenght = jsonArray.length();
                    cities = new String[lenght];
                    for (i = 0; i < lenght; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cities[i] = (jsonObject.getString("name"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<String>(RegistCity.this,
                                    android.R.layout.simple_spinner_dropdown_item, cities);
                            spin.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getEditCity() throws IOException {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        int nameIndex = c.getColumnIndex("name");
        String idUser = c.getString(idColIndex);
        String tokenUser = c.getString(tokenColIndex);
        String name = c.getString(nameIndex);
        c.moveToFirst();
        c.close();
        db.close();

        Log.wtf("asdasd", name + " " + idUser);

        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS" +
                "&opt=user_param" +
                "&act=edit_city" +
                "&userid=" + idUser +
                "&utoken=" + tokenUser +
                "&name=" + name +
                "&city=" + spin.getSelectedItem().toString();
        Log.e("userCreatedURL", url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegistCity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(RegistCity.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    getEditCity();
                                } catch (IOException e) {
                                    e.printStackTrace();
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

                String mMessage = response.body().string();
                Log.e("userCreated", mMessage);
                Intent intent = new Intent(RegistCity.this,Main2Activity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
