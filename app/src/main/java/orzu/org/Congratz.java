package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Congratz extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_congratz);


        ImageView feer1 = (ImageView)findViewById(R.id.imageFeerNew);


        TextView btn = findViewById(R.id.createCongratz);
        btn.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final AnimatedVectorDrawableCompat anim = AnimatedVectorDrawableCompat.create(getApplication(), R.drawable.congratz);
                feer1.setImageDrawable(anim);
                anim.start();
            }
        }, 500);


    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
