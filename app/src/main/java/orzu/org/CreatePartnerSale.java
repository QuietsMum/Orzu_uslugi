package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreatePartnerSale extends AppCompatActivity implements View.OnClickListener {
    CardView cardView;
    ImageView back;
    TextView sale_name, sale_desc, createNameSale;
    LinearLayout pluslinearyimages;
    ImageView logoSale;
    ArrayList<String> returnValue = new ArrayList<>();
    Spinner category_sale, subcategory_sale;
    List<category_model> subcategories = new ArrayList<>();

    public static Activity fa;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        }
        if (returnValue.size() != 0) {
            logoSale.setImageURI(Uri.parse(returnValue.get(0)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
        setContentView(R.layout.activity_create_partner_sale);

        cardView = findViewById(R.id.card_of_name_sale);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners2);
        fa = this;
        subcategory_sale = findViewById(R.id.subcategory_sale);
        category_sale = findViewById(R.id.category_sale);

        logoSale = findViewById(R.id.logoSale);
        sale_name = findViewById(R.id.sale_name);
        sale_desc = findViewById(R.id.sale_description);
        createNameSale = findViewById(R.id.createNameSale);
        pluslinearyimages = findViewById(R.id.pluslinearyimages);

        pluslinearyimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pluslinearyimages.setVisibility(View.GONE);
                logoSale.setVisibility(View.VISIBLE);
                Options options = Options.init()
                        .setRequestCode(100)                                                 //Request code for activity results
                        .setCount(6)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
                        .setPreSelectedUrls(returnValue)                                     //Pre selected Image Urls
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
                        .setPath("/orzu/images");
                Pix.start(CreatePartnerSale.this, options);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePartnerSale.this,
                android.R.layout.simple_spinner_dropdown_item, Main2Activity.category);
        category_sale.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        category_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSubCategories(Main2Activity.categories.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getSubCategories(Main2Activity.categories.get(0).getId());
        logoSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options options = Options.init()
                        .setRequestCode(100)                                                 //Request code for activity results
                        .setCount(6)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                               //Front Facing camera on start
                        .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
                        .setPreSelectedUrls(returnValue)                                     //Pre selected Image Urls
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
                        .setPath("/orzu/images");
                Pix.start(CreatePartnerSale.this, options);
            }
        });
        createNameSale.setOnClickListener(this);

        back = findViewById(R.id.sale_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                1500.0f, 0.0f);
        animation.setDuration(500);
        //animation.setFillAfter(true);
        cardView.startAnimation(animation);
        sale_name.startAnimation(animation);
        sale_desc.startAnimation(animation);
        createNameSale.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                createNameSale.setVisibility(View.VISIBLE);
                Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoom_in);
                createNameSale.startAnimation(animZoomIn);
            }
        }, animation.getDuration());
    }

    @Override
    public void onClick(View view) {
        if (sale_name.getText().length() != 0 && sale_desc.getText().length() != 0) {
            final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Sale_Name", String.valueOf(sale_name.getText()));
            editor.putString("Sale_Desc", String.valueOf(sale_desc.getText()));
            editor.putString("Sale_Cat", String.valueOf(Main2Activity.categories.get(category_sale.getSelectedItemPosition()).getId()));
            editor.putString("Sale_SubCat", String.valueOf(subcategories.get(subcategory_sale.getSelectedItemPosition()).getId()));
            if (returnValue.size() != 0) {
                editor.putString("Sale_Logo", String.valueOf(returnValue.get(0)));
            }
            editor.apply();
            Intent intent = new Intent(this, LastPageSale.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSubCategories(String id) {
        subcategories.clear();
        String requestUrl = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_subcat&id=" + id;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, requestUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    String[] cities = new String[j.length()];
                    for (int i = 0; i < j.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
                            subcategories.add(new category_model(object.getString("id"), object.getString("name"), object.getString("parent_id")));
                            cities[i] = object.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePartnerSale.this,
                            android.R.layout.simple_spinner_dropdown_item, cities);
                    subcategory_sale.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Dialog dialog = new Dialog(CreatePartnerSale.this, android.R.style.Theme_Material_Light_NoActionBar);
                dialog.setContentView(R.layout.dialog_no_internet);
                Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSubCategories(id);
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
        Volley.newRequestQueue(Objects.requireNonNull(CreatePartnerSale.this)).add(stringRequest);
    }
}
