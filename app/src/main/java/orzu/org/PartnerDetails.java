package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.fxn.utility.PermUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class PartnerDetails extends AppCompatActivity implements View.OnClickListener {


    CardView cardView;
    ImageView back;
    TextView partner_name, partner_desc, createNamePartner;
    LinearLayout pluslinearyimages;
    ImageView logoPartner;
    ArrayList<String> returnValue = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        }
        logoPartner.setImageURI(Uri.parse(returnValue.get(0)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
        setContentView(R.layout.activity_partner_details);

        cardView = findViewById(R.id.card_of_name_partner);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);

        partner_name = findViewById(R.id.partner_name);
        partner_desc = findViewById(R.id.partner_description);
        createNamePartner = findViewById(R.id.createNamePartner);
        pluslinearyimages = findViewById(R.id.pluslinearyimages);
        logoPartner = findViewById(R.id.logoPartner);

        //Custom Path For Image Storage


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
            editor.apply();
            Intent intent = new Intent(this, CreatePartnerSale.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}
