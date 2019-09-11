package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateTaskName extends AppCompatActivity implements View.OnClickListener {

    TextView buttonCreate;
    EditText editName;
    public static Activity fa;
    RecyclerView suggestResultView;
    ArrayAdapterMy resultAdapter;
    List<String> suggestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        setContentView(R.layout.activity_create_task_name);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        editName = findViewById(R.id.editCreateName);
        buttonCreate = findViewById(R.id.createName);
        buttonCreate.setOnClickListener(this);
        fa = this;

        suggestResultView = findViewById(R.id.suggest_result_name);
        suggestResultView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        suggestResultView.setLayoutManager(layoutManager);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapterMy(this, suggestResult);
        suggestResultView.setAdapter(resultAdapter);


        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editName.getText().length()!=0){
                    Log.e("result", "enter");
                    requestSuggest();
                }
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

    @Override
    public void onClick(View view) {
        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Util.TASK_NAME, String.valueOf(editName.getText()));
        editor.apply();
        Intent intent = new Intent(this, CreateTaskPlace.class);
        startActivity(intent);
    }

    public void requestSuggest(){

        String url = "https://orzu.org/tasks/taskajaxupload?find=" + editName.getText();
        OkHttpClient client = new OkHttpClient();
        Log.e("result", "enterFunction");
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

                suggestResult.clear();

                try {
                    JSONArray jsonArray = new JSONArray(mMessage);
                    int length = jsonArray.length();

                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            suggestResult.add(jsonArray.getString(i));
                            Log.e("resultArray", suggestResult.get(i));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                suggestResultView.getRecycledViewPool().clear();
                                resultAdapter.notifyDataSetChanged();
                                suggestResultView.setVisibility(View.VISIBLE);
                            }
                        });


                        ArrayAdapterMy.setSelect(new NameItemSelect() {
                            @Override
                            public void onItemSelectedListener(View view, int position) {

                                editName.setText(suggestResult.get(position));
                                editName.setSelection(editName.getText().length());

                            }

                            @Override
                            public void onClick(View view) {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace(); }
            }
        });
    }
}
