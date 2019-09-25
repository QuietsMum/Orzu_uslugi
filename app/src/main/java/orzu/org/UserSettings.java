package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {

    LinearLayout buttonEdit;
    LinearLayout buttonLogout;
    LinearLayout button_change_password;
    LinearLayout btn_notif_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        getSupportActionBar().setTitle("Настройки");
        getSupportActionBar().setElevation(0);

        buttonEdit = findViewById(R.id.button_edit_profile);
        buttonLogout = findViewById(R.id.button_logout);
        button_change_password = findViewById(R.id.button_change_password);
        btn_notif_settings = findViewById(R.id.btn_notif_settings);
        buttonEdit.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
        button_change_password.setOnClickListener(this);
        btn_notif_settings.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit_profile:
                Intent intent = new Intent(this, UserEditProfile.class);
                startActivity(intent);
                break;
            case R.id.button_change_password:
                Intent intent2 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_notif_settings:
                Intent intent3 = new Intent(this, NotificationSettings.class);
                startActivity(intent3);
                break;
            case R.id.button_logout:
                DBHelper dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("delete from "+ "orzutable");
                Intent mStartActivity = new Intent(this, CoreActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
                break;
        }
    }
}
