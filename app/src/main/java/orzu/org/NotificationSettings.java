package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class NotificationSettings extends AppCompatActivity {

    Switch switch_notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        getSupportActionBar().setTitle("Настройки уведомления");
        getSupportActionBar().setElevation(0);

        switch_notif = findViewById(R.id.switch_notif);

        if(prefs.getBoolean("enableNotifiaction",false)) {
            switch_notif.setChecked(false);
        }else{
            switch_notif.setChecked(true);
        }

        SharedPreferences.Editor editor = prefs.edit();

        switch_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editor.putBoolean("enableNotifiaction", false);
                    editor.apply();
                }else{
                    editor.putBoolean("enableNotifiaction", true);
                    editor.apply();
                }
            }
        });
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
}
