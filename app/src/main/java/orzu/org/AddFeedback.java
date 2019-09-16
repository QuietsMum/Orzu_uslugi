package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddFeedback extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout backButton;
    ConstraintLayout addButton;
    ImageView sadImage;
    ImageView natImage;
    ImageView hapImage;
    ImageView person_logo_sugg2;
    EditText editNarr;
    String idUser;
    String tokenUser;
    String idUserTo;
    int like;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        setContentView(R.layout.activity_add_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Добавить отзыв");

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
        like = 1;
        idUserTo = getIntent().getExtras().getString("idUserFeedbackto");

        backButton = findViewById(R.id.button_back_feed);
        backButton.setOnClickListener(this);
        addButton = findViewById(R.id.button_add_feed);
        addButton.setOnClickListener(this);

        sadImage = findViewById(R.id.id_sad);
        sadImage.setAlpha(0.5f);
        sadImage.setOnClickListener(this);
        natImage = findViewById(R.id.id_neutral);
        natImage.setAlpha(1f);
        natImage.setOnClickListener(this);
        hapImage = findViewById(R.id.id_happy);
        hapImage.setOnClickListener(this);
        hapImage.setAlpha(0.5f);

        editNarr = findViewById(R.id.edit_task_feedback);
        username = findViewById(R.id.name_surname);
        username.setText(getIntent().getExtras().getString("nameUserFeedbackto"));

        person_logo_sugg2 = findViewById(R.id.person_logo2);
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


    public void sendFeedback() throws IOException {

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=reviews" +
                "&act=input" +
                "&userid=" + idUser +
                "&narrative=" + editNarr.getText() +
                "&like_user_id=" + idUserTo +
                "&like=" + like +
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
            case R.id.button_back_feed:
                finish();
                break;
            case R.id.id_sad:
                sadImage.setAlpha(1f);
                natImage.setAlpha(0.5f);
                hapImage.setAlpha(0.5f);
                like = 0;
                break;
            case R.id.id_neutral:
                sadImage.setAlpha(0.5f);
                natImage.setAlpha(1f);
                hapImage.setAlpha(0.5f);
                like = 1;
                break;
            case R.id.id_happy:
                sadImage.setAlpha(0.5f);
                natImage.setAlpha(0.5f);
                hapImage.setAlpha(1f);
                like = 2;
                break;
            case R.id.button_add_feed:
                try {
                    sendFeedback();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
