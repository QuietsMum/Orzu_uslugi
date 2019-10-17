package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddSuggest extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout backButton;
    ConstraintLayout addButton;
    EditText editNarr;
    EditText editAmout;
    String idUser;
    String tokenUser;
    String nameUser;
    String idTask;
    int amout;
    TextView username;
    ImageView person_logo_sugg2,suggest_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.back_for_feed));
        setContentView(R.layout.activity_add_suggest);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        tokenUser = c.getString(tokenColIndex);
        c.close();
        db.close();
        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        nameUser = prefs.getString(Util.TASK_USERNAME, "");
        Log.e("Username", nameUser);
        Log.e("Userid", idUser);

        suggest_back = findViewById(R.id.suggest_back);
        suggest_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        idTask = getIntent().getExtras().getString("idTaskSuggest");
        Log.e("Taskid", idTask);

        backButton = findViewById(R.id.button_back_sugg);
        backButton.setOnClickListener(this);
        addButton = findViewById(R.id.button_add_sugg);
        addButton.setOnClickListener(this);


        editNarr = findViewById(R.id.edit_task_sugg);
        editAmout = findViewById(R.id.edit_task_sugg_amout);

        username = findViewById(R.id.name_sugg);
        username.setText(nameUser);

        person_logo_sugg2 = findViewById(R.id.person_logo_sugg2);
        person_logo_sugg2.setImageBitmap(Common.bitmap);

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


    public void sendSuggest() throws IOException {

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=task_requests" +
                "&act=input" +
                "&task_id=" + idTask +
                "&narrative=" + editNarr.getText() +
                "&userid=" + idUser +
                "&amount=" + editAmout.getText() +
                "&utoken=" + tokenUser;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AddSuggest.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(AddSuggest.this, android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    sendSuggest();
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

                String mMessage = response.body().string();
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back_sugg:
                finish();
                break;

            case R.id.button_add_sugg:
                try {
                    sendSuggest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
