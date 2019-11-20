package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.Objects;

import orzu.org.ui.login.model;

public class CoreActivity extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_core);

        Long[] idArray = new Long[1];
        idArray[0] = 0L;
        model.array = idArray;
        Common.subFilter = new HashMap<>();

        ImageView logo = (ImageView)findViewById(R.id.imageLogoNew);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final AnimatedVectorDrawableCompat anim = AnimatedVectorDrawableCompat.create(getApplication(), R.drawable.logo_animated);
                logo.setImageDrawable(anim);
                assert anim != null;
                anim.start();
            }
        }, 500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (c.getCount() > 0){
                    Intent intent = new Intent(CoreActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(CoreActivity.this, ViewpagerStart.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2200);
        c.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
