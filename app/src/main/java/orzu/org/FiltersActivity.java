package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    String cat = "Cat";
    String res = "Res";
    String img = "Img";
    ArrayList<Map<String, Object>> data1;
    ArrayList<Map<String, Object>> data2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Фильтры");
        ListView lvMain1 = (ListView)findViewById(R.id.list_view_filters1);
        ListView lvMain2 = (ListView)findViewById(R.id.list_view_filters2);

        Button button = (Button)findViewById(R.id.buttonDone);
        button.setOnClickListener(this);

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

        SimpleAdapter arrayAdapter1 = new SimpleAdapter(this, data1, R.layout.filters_item_2, from1, to1);
        lvMain1.setAdapter(arrayAdapter1);
        lvMain1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(FiltersActivity.this, CategoryView.class);
                    startActivity(intent);
                } else {
                    /*Intent intent = new Intent(FiltersActivity.this, SubCategoryView2.class);
                    startActivity(intent);*/
                }
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
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","changes");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
