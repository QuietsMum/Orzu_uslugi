package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import orzu.org.ui.login.model;

public class FiltersActivity extends AppCompatActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, FiltersActivity.class);
        context.startActivity(intent);
    }
    String[] filtname1 = { "Категории",  "Город"};
    String[] filtname2 = { "Оплата картой", "Задания без предложений", "Бизнес-задания"};
    String[] filtres1 = { "Все категории",  "Алматы"};
    String[] filtres2 = { "Через сделку без риска", "Успей откликнутся", "Безналичная оплата"};
    int[] filtimg1 = {R.drawable.all_org,  R.drawable.point_org};
    int[] filtimg2 = {R.drawable.card_org, R.drawable.flash_org, R.drawable.work_org};
    int elementPos;
    String cat = "Cat";
    String res = "Res";
    String img = "Img";
    TextView textCity;
    Button button;
    ArrayList<Map<String, Object>> data1;
    ArrayList<Map<String, Object>> data2;
    SimpleAdapter arrayAdapter1;
    ListView lvMain1;
    ListView lvMain2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ActionBar toolbar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        toolbar.setTitle("Фильтры");
        lvMain1 = (ListView)findViewById(R.id.list_view_filters1);
        lvMain2 = (ListView)findViewById(R.id.list_view_filters2);

        /*ImageView lineColorCode = (ImageView) lvMain1.findViewById(R.id.nexttocatfilt);
        int color = Color.parseColor("#E2E2E2"); //The color u want
        lineColorCode.setColorFilter(color);*/

        button = (Button)findViewById(R.id.buttonDone);
        button.setOnClickListener(this);
        button.setVisibility(View.INVISIBLE);

        data1= new ArrayList<>();
        data2= new ArrayList<>();
        for (int i = 0; i < 2; i++){
            Map<String, Object> m1 = new HashMap<>();
            m1.put(cat,filtname1[i]);
            m1.put(res,filtres1[i]);
            m1.put(img,filtimg1[i]);
            data1.add(m1);
        }

        for (int i = 0; i < 3; i++){
            Map<String, Object> m2 = new HashMap<>();
            m2.put(cat,filtname2[i]);
            m2.put(res,filtres2[i]);
            m2.put(img,filtimg2[i]);
            data2.add(m2);
        }


        String[] from1 = { cat,  res, img};
        int[] to1 = { R.id.textView10, R.id.textView11, R.id.imageViewFilt };

        arrayAdapter1 = new SimpleAdapter(this, data1, R.layout.filters_item_2, from1, to1);
        lvMain1.setAdapter(arrayAdapter1);
        lvMain1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    Intent intent = new Intent(FiltersActivity.this, CategoryView.class);
                    startActivity(intent);
                } if (i == 1) {

                    Intent intent2 = new Intent(FiltersActivity.this, SubCategoryView2.class);
                    startActivityForResult(intent2, 1);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setVisibility(View.VISIBLE);
                    }
                }, 1200);

            }
        });

        String[] from2 = { cat,  res, img};
        int[] to2 = { R.id.textView10, R.id.textView11, R.id.imageViewFilt };

       SimpleAdapter arrayAdapter2 = new SimpleAdapter(this, data2, R.layout.filters_item, from2, to2);
        lvMain2.setAdapter(arrayAdapter2);
        lvMain2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Switch swit = view.findViewById(R.id.switch1);
                if (!swit.isChecked()) {
                    swit.setChecked(true);
                } else swit.setChecked(false);

               button.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result= data.getStringExtra("result");
                textCity = lvMain2.getAdapter().getView(elementPos, null, lvMain2).findViewById(R.id.textView11);
                textCity.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","changes");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.up_menu_filters_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_filter_menu_main:
                Long[] idArray = new Long[1];
                idArray[0] = 0L;
                model.array = idArray;
                button.setVisibility(View.VISIBLE);
                return true;
            case android.R.id.home:

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
