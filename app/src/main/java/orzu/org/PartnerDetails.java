package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.fxn.utility.PermUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartnerDetails extends AppCompatActivity implements View.OnClickListener {


    CardView cardView;
    ImageView back;
    TextView partner_name, partner_desc, createNamePartner;
    LinearLayout pluslinearyimages;
    ImageView logoPartner;
    ArrayList<String> returnValue = new ArrayList<>();
    Spinner cat_spin;
    Spinner subcat_spin;
    public static Activity fa;
    List<category_model> subcategories = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        }
        if (returnValue.size() != 0) {
            logoPartner.setImageURI(Uri.parse(returnValue.get(0)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
        setContentView(R.layout.activity_partner_details);

        fa = this;

        cardView = findViewById(R.id.card_of_name_partner);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners2);

        partner_name = findViewById(R.id.partner_name);
        partner_desc = findViewById(R.id.partner_description);
        createNamePartner = findViewById(R.id.createNamePartner);
        pluslinearyimages = findViewById(R.id.pluslinearyimages);
        logoPartner = findViewById(R.id.logoPartner);
        cat_spin = findViewById(R.id.category_partner);
        subcat_spin = findViewById(R.id.subcategory_partner);
        //Custom Path For Image Storage

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartnerDetails.this,
                android.R.layout.simple_spinner_dropdown_item, Main2Activity.category);
        cat_spin.setAdapter(adapter);
        cat_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSubCategories(Main2Activity.categories.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();
        getSubCategories(Main2Activity.categories.get(0).getId());

        pluslinearyimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pluslinearyimages.setVisibility(View.GONE);
                logoPartner.setVisibility(View.VISIBLE);
                Options options = Options.init()
                        .setRequestCode(100)                                                 //Request code for activity results
                        .setCount(6)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
                        .setPreSelectedUrls(returnValue)                                     //Pre selected Image Urls
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
                        .setPath("/orzu/images");
                Pix.start(PartnerDetails.this, options);
            }
        });

        logoPartner.setOnClickListener(new View.OnClickListener() {
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
                Pix.start(PartnerDetails.this, options);
            }
        });

        createNamePartner.setOnClickListener(this);

        back = findViewById(R.id.create_name_back);
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
        partner_name.startAnimation(animation);
        partner_desc.startAnimation(animation);
        createNamePartner.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                createNamePartner.setVisibility(View.VISIBLE);
                Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoom_in);
                createNamePartner.startAnimation(animZoomIn);
            }
        }, animation.getDuration());
    }

    @Override
    public void onClick(View view) {
        if (partner_name.getText().length() != 0 && partner_desc.getText().length() != 0) {
            final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Partner_Name", String.valueOf(partner_name.getText()));
            editor.putString("Partner_Desc", String.valueOf(partner_desc.getText()));
            editor.putString("Partner_City", "Алматы");
            editor.putString("Partner_SubCat", subcategories.get(subcat_spin.getSelectedItemPosition()).getId());
            if (returnValue.size() != 0) {
                editor.putString("Partner_Logo", String.valueOf(returnValue.get(0)));
            }
            editor.apply();
            Intent intent = new Intent(this, CreatePartnerSale.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSubCategories(String id) {
        subcategories.clear();
        String requestUrl = "https://projectapi.pw/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_cat&cat_id=only_subcat&id=" + id;
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartnerDetails.this,
                            android.R.layout.simple_spinner_dropdown_item, cities);
                    subcat_spin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Dialog dialog = new Dialog(PartnerDetails.this, android.R.style.Theme_Material_Light_NoActionBar);
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
        Volley.newRequestQueue(Objects.requireNonNull(PartnerDetails.this)).add(stringRequest);
    }
}
