package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

public class CoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_core);

        final SharedPreferences prefs =  getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cat", "all");
        editor.apply();

        ImageView logo = (ImageView)findViewById(R.id.imageLogo);
        ObjectAnimator anim = ObjectAnimator.ofFloat(logo, "translationY", -750f);
        anim.setDuration(500);
        anim.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CoreActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
