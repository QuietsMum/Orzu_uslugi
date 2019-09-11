package orzu.org;

import android.app.Application;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class IntercomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        getWindow().setBackgroundDrawableResource(R.color.colorAccent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}