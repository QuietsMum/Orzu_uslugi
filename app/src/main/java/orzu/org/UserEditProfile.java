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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserEditProfile extends AppCompatActivity implements View.OnClickListener {

    EditText userName;
    EditText userFname;
    EditText userNarr;
    TextView userDate;
    Spinner userCity;
    ConstraintLayout datePicker;
    ConstraintLayout buttonEdit;
    RadioButton radioMale;
    RadioButton radioFemale;
    RadioButton radioButton;
    RadioGroup radioGroup;
    String gender;
    Dialog dialog;
    String selectedDate;
    DatePicker calendar;
    Locale myLocale = new Locale("ru","RU");
    SimpleDateFormat sdf;
    DBHelper dbHelper;
    Date date;

    String idUser;
    String text;
    String mMessage;
    String mName;
    String mFiName;
    String mCount;
    String mCountReq;
    String mCity;
    String mBday;
    String mSex;
    String mNarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        getSupportActionBar().setTitle("Профиль");
        getSupportActionBar().setElevation(0);



        userName = findViewById(R.id.user_edit_name);
        userFname = findViewById(R.id.user_edit_fname);
        userNarr = findViewById(R.id.useri_edit_about);
        userCity = findViewById(R.id.user_edit_place);
        userDate = findViewById(R.id.birth_textview);
        datePicker = findViewById(R.id.user_edit_birth);
        buttonEdit = findViewById(R.id.edit_user_btn);
        radioMale = findViewById(R.id.male);
        radioFemale = findViewById(R.id.female);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.male);
        radioMale.setActivated(true);
        buttonEdit.setOnClickListener(this);
        datePicker.setOnClickListener(this);


        dialog = new Dialog (this);
        dialog.setContentView(R.layout.calendar_dialog_spinner);

        calendar = dialog.findViewById(R.id.calendarView);

        Calendar today = Calendar.getInstance();
        calendar.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                        sdf = new SimpleDateFormat("dd.MM.yyyy", myLocale);
                        selectedDate = sdf.format(new Date(year - 1900, monthOfYear, dayOfMonth));

                    }
                });
        TextView dialogButton = dialog.findViewById(R.id.button_choose);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userDate.setText(selectedDate);

                dialog.dismiss();
            }
        });

        try {
            getUserForEdit();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        switch(view.getId()){
            case R.id.user_edit_birth:
                dialog.show();
                break;

            case R.id.edit_user_btn:
                try {
                    getEditResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
    }

    public void getEditResponse() throws IOException {
        int day  = calendar.getDayOfMonth();
        int monthNumber = calendar.getMonth() + 1;
        int year = calendar.getYear();

        radioButton = findViewById(R.id.male);
        if(radioButton.isChecked())
        {
            gender = "male";
        }
        else
        {
            gender = "female";
        }
        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS" +
                "&opt=user_param" +
                "&act=edit" +
                "&userid=" + idUser +
                "&name=" + userName.getText() +
                "&fname=" + userFname.getText() +
                "&city=" + userCity.getSelectedItem().toString() +
                "&about=" + userNarr.getText() +
                "&gender=" + gender +
                "&bday=" + day +
                "&bmonth=" + monthNumber +
                "&byear=" + year;

        Log.e("created response", url);

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

                UserEditProfile.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserEditProfile.this, mMessage, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void getUserForEdit() throws IOException {

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int tokenColIndex = c.getColumnIndex("id");
        idUser = c.getString(tokenColIndex);
        c.moveToFirst();
        c.close();
        db.close();

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_user&user=" + idUser + "&param=more";
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

                Log.e("Full Info User", mMessage);

                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    mName = jsonObject.getString("name");
                    mFiName = jsonObject.getString("fname");
                    mCount = jsonObject.getString("tasks");
                    mCountReq = jsonObject.getString("task_requests");
                    mCity = jsonObject.getString("city");
                    mBday = jsonObject.getString("birthday");
                    mSex = jsonObject.getString("sex");
                    mNarr = jsonObject.getString("about");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userName.setText(mName);
                            userFname.setText(mFiName);
                            switch (mCity) {
                                case "Алматы":
                                    userCity.setSelection(3);
                                    break;
                                case "Кулоб":
                                    userCity.setSelection(2);
                                    break;
                                case "Худжанд":
                                    userCity.setSelection(1);
                                    break;
                                case "Душанбе":
                                    userCity.setSelection(0);
                                    break;
                            }
                            userNarr.setText(mNarr);
                            userDate.setText(mBday);
                            SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
                            Date datesimple = new Date();
                            Calendar calendarnew = new GregorianCalendar();

                            try {
                                datesimple = fmt.parse(mBday);
                                Log.e("datesimple", String.valueOf(datesimple));
                                if (datesimple != null) {
                                    calendarnew.setTime(datesimple);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                                calendar.updateDate(calendarnew.get(Calendar.YEAR), calendarnew.get(Calendar.MONTH), calendarnew.get(Calendar.DAY_OF_MONTH));
                                Log.e("updateDate", String.valueOf(calendarnew.get(Calendar.YEAR)) + " " + String.valueOf(calendarnew.get(Calendar.MONTH)) + " " + String.valueOf(calendarnew.get(Calendar.DAY_OF_MONTH)));



                            if (mSex.equals("male")){
                                radioGroup.check(R.id.male);
                            } else  radioGroup.check(R.id.female);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace(); }

            }
        });
    }

}
