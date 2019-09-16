package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CreateTaskTerm extends AppCompatActivity implements View.OnClickListener {

    TextView buttonCreate;
    public static Activity fa;
    TextView switch1;
    TextView switch2;
    TextView switch3;
    TextView text_examle1;
    TextView text_date1;
    TextView text_date1_2;
    TextView text_date2;
    LinearLayout lin_examlpe1_1;
    LinearLayout lin_examlpe1_2;
    LinearLayout lin_examlpe2;
    Spinner spiner;
    int count;
    int counterDef;
    Dialog dialog;
    SimpleDateFormat sdf;
    String selectedDate;
    DatePicker calendar;
    WindowManager.LayoutParams lp;
    GregorianCalendar calendarGeor;
    Locale myLocale = new Locale("ru", "RU");
    int dateType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_term);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        getSupportActionBar().setTitle("Создать задание");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        counterDef = 2;
        dateType = 2;
        switch1 = findViewById(R.id.createTerm_buttonleft);
        switch1.setOnClickListener(this);
        switch2 = findViewById(R.id.createTerm_buttoncenter);
        switch2.setOnClickListener(this);
        switch3 = findViewById(R.id.createTerm_buttonright);
        switch3.setOnClickListener(this);
        spiner = findViewById(R.id.spinner_term);
        spiner.setVisibility(View.INVISIBLE);
        text_examle1 = findViewById(R.id.createTerm_textisp);
        text_date1 = findViewById(R.id.term_text_from_date);
        text_date1_2 = findViewById(R.id.term_text_to_date);
        text_date2 = findViewById(R.id.term_text_one_date);

        lin_examlpe1_1 = findViewById(R.id.layout_term_text_from);
        lin_examlpe1_1.setOnClickListener(this);
        lin_examlpe1_1.setVisibility(View.VISIBLE);
        lin_examlpe1_2 = findViewById(R.id.layout_term_text_to);
        lin_examlpe1_2.setOnClickListener(this);
        lin_examlpe1_2.setVisibility(View.VISIBLE);
        lin_examlpe2 = findViewById(R.id.layout_term_onedate);
        lin_examlpe2.setOnClickListener(this);
        lin_examlpe2.setVisibility(View.INVISIBLE);
        text_examle1.setVisibility(View.INVISIBLE);

        lp = new WindowManager.LayoutParams();
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = getResources().getDisplayMetrics().heightPixels/3;


        buttonCreate = findViewById(R.id.createTerm);
        buttonCreate.setOnClickListener(this);
        fa = this;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.calendar_dialog);

        calendar = dialog.findViewById(R.id.calendarView);

        Calendar today = Calendar.getInstance();
        calendar.setCalendarViewShown(false);
        calendar.setSpinnersShown(true);
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


                if (count == 1) {
                    text_date1.setText(selectedDate);
                } else if (count == 2) {
                    text_date1_2.setText(selectedDate);
                } else if (count == 3) {
                    text_date2.setText(selectedDate);
                }
                dialog.dismiss();
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

        // 1. Точная дата 
        // 2. Указать период 
        // 3. Договорюсь с исполнителем


        switch (view.getId()) {
            case R.id.createTerm:
                if (lin_examlpe1_1.getVisibility() == View.VISIBLE) {
                    Log.wtf("as","asdsad");
                    if (text_date1.getText().length() > 8 && text_date2.getText().length() > 8) {
                        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        Log.wtf("as","asd");
                        if (counterDef == 1) {
                            dateType = 3;
                            editor.putString(Util.TASK_WORKWITH, "Дата по договоренности");
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.apply();
                        } else if (counterDef == 2) {
                            dateType = 2;
                            editor.putString(Util.TASK_CDATE, String.valueOf(text_date1.getText()));
                            editor.putString(Util.TASK_EDATE, String.valueOf(text_date1_2.getText()));
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.apply();
                        } else if (counterDef == 3) {
                            dateType = 1;
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.putString(Util.TASK_CDATEL, String.valueOf(text_date2.getText()));
                            editor.putString(Util.TASK_LEVEL_L, String.valueOf(spiner.getSelectedItemPosition()));
                            editor.apply();
                        }
                        Intent intent = new Intent(this, CreateTaskAmout.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    }
                } else if (spiner.getVisibility() == View.VISIBLE) {
                    if(spiner.getSelectedItem().toString().length()!=0&&text_date2.getText().length()>8) {
                        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        if (counterDef == 1) {
                            dateType = 3;
                            editor.putString(Util.TASK_WORKWITH, "Дата по договоренности");
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.apply();
                        } else if (counterDef == 2) {
                            dateType = 2;
                            editor.putString(Util.TASK_CDATE, String.valueOf(text_date1.getText()));
                            editor.putString(Util.TASK_EDATE, String.valueOf(text_date1_2.getText()));
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.apply();
                        } else if (counterDef == 3) {
                            dateType = 1;
                            editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                            editor.putString(Util.TASK_CDATEL, String.valueOf(text_date2.getText()));
                            editor.putString(Util.TASK_LEVEL_L, String.valueOf(spiner.getSelectedItemPosition()));
                            editor.apply();
                        }
                        Intent intent = new Intent(this, CreateTaskAmout.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    if (counterDef == 1) {
                        dateType = 3;
                        editor.putString(Util.TASK_WORKWITH, "Дата по договоренности");
                        editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                        editor.apply();
                    } else if (counterDef == 2) {
                        dateType = 2;
                        editor.putString(Util.TASK_CDATE, String.valueOf(text_date1.getText()));
                        editor.putString(Util.TASK_EDATE, String.valueOf(text_date1_2.getText()));
                        editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                        editor.apply();
                    } else if (counterDef == 3) {
                        dateType = 1;
                        editor.putString(Util.TASK_DATETYPE, String.valueOf(dateType));
                        editor.putString(Util.TASK_CDATEL, String.valueOf(text_date2.getText()));
                        editor.putString(Util.TASK_LEVEL_L, String.valueOf(spiner.getSelectedItemPosition()));
                        editor.apply();
                    }
                    Intent intent = new Intent(this, CreateTaskAmout.class);
                    startActivity(intent);
                }
                break;
            case R.id.createTerm_buttonleft:
                counterDef = 1;
                switch1.setBackgroundResource(R.drawable.circle_button_left_solid);
                switch1.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                switch2.setBackgroundResource(R.drawable.circle_button_center);
                switch2.setTextColor(getResources().getColor(R.color.colorAccent));
                switch3.setBackgroundResource(R.drawable.circle_button_right);
                switch3.setTextColor(getResources().getColor(R.color.colorAccent));
                spiner.setVisibility(View.INVISIBLE);
                text_examle1.setVisibility(View.VISIBLE);
                lin_examlpe1_1.setVisibility(View.INVISIBLE);
                lin_examlpe1_2.setVisibility(View.INVISIBLE);
                lin_examlpe2.setVisibility(View.INVISIBLE);
                break;
            case R.id.createTerm_buttoncenter:
                counterDef = 2;
                switch1.setBackgroundResource(R.drawable.circle_button_left);
                switch1.setTextColor(getResources().getColor(R.color.colorAccent));
                switch2.setBackgroundResource(R.drawable.circle_button_center_solid);
                switch2.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                switch3.setBackgroundResource(R.drawable.circle_button_right);
                switch3.setTextColor(getResources().getColor(R.color.colorAccent));
                spiner.setVisibility(View.INVISIBLE);
                text_examle1.setVisibility(View.INVISIBLE);
                lin_examlpe1_1.setVisibility(View.VISIBLE);
                lin_examlpe1_2.setVisibility(View.VISIBLE);
                lin_examlpe2.setVisibility(View.INVISIBLE);
                break;
            case R.id.createTerm_buttonright:
                counterDef = 3;
                switch1.setBackgroundResource(R.drawable.circle_button_left);
                switch1.setTextColor(getResources().getColor(R.color.colorAccent));
                switch2.setBackgroundResource(R.drawable.circle_button_center);
                switch2.setTextColor(getResources().getColor(R.color.colorAccent));
                switch3.setBackgroundResource(R.drawable.circle_button_right_solid);
                switch3.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                spiner.setVisibility(View.VISIBLE);
                text_examle1.setVisibility(View.INVISIBLE);
                lin_examlpe1_1.setVisibility(View.INVISIBLE);
                lin_examlpe1_2.setVisibility(View.INVISIBLE);
                lin_examlpe2.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_term_text_from:
                dialog.show();
                count = 1;
                break;
            case R.id.layout_term_text_to:
                dialog.show();
                count = 2;
                break;
            case R.id.layout_term_onedate:
                dialog.show();
                count = 3;
                break;
        }
    }
}
