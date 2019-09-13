package orzu.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orzu.org.ui.login.LoginActivity;

import static orzu.org.MessageReceiver.extra;

public class LoginActivity2 extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    EditText phon;
    EditText phonCount;
    EditText pass;
    String sms;
    Button button;
    Button button2;
    String mMessage;
    String mPhone;
    String mName;
    String mPassword;
    String mStatus;
    String mToken;
    Long mID;
    String code;
    ProgressBar progressBar;
    JSONObject obj;
    CountryCodePicker ccp;
    BroadcastReceiver otp;
    EditText input;
    DBHelper dbHelper;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login2);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackLight)));
        dbHelper = new DBHelper(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_REQUEST);
            }

        }


        IntentFilter filter = new IntentFilter();
        filter.addAction("service.to.activity.transfer");
        otp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!extra.equals("")) {
                    Log.e("smsmessage", extra);
                    String substr = extra.substring(extra.indexOf(" ") + 1);
                    input.setText(substr);
                    extra = "";

                    final SharedPreferences prefs = getSharedPreferences("", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Username", name.getText().toString());
                    editor.apply();

                }
            }
        };
        registerReceiver(otp, filter);

        progressBar = findViewById(R.id.progressBarLogin_reg);
        progressBar.setVisibility(View.INVISIBLE);
        name = (EditText) findViewById(R.id.editTextNameUser);
        phon = (EditText) findViewById(R.id.editTextPhone_reg);
        phonCount = (EditText) findViewById(R.id.editTextPhoneCountry);
        pass = (EditText) findViewById(R.id.editTextPass);
        button = (Button) findViewById(R.id.button_phone_login);

        float dip = 16f;
        Resources r = Resources.getSystem();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        ccp = (CountryCodePicker) findViewById(R.id.ccp_reg);
        //ccp.registerPhoneNumberTextView(phone);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = ccp.getSelectedCountryCodeWithPlus();
                //phon.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(getApplicationContext(), code), null, null, null);
                //phon.setCompoundDrawablePadding((int) (code.length()*px));
                phonCount.setText(code);
            }
        });
        code = ccp.getSelectedCountryCodeWithPlus();
        //phon.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(getApplicationContext(), code), null, null, null);
        //phon.setCompoundDrawablePadding((int) (code.length()*px));
        phonCount.setText(code);
        button.setOnClickListener(this);
    }

    private static final int MY_REQUEST = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Напишите код вручную", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (name.getText().length() != 0&&phon.getText().length()!=0&&pass.getText().length()!=0) {
            progressBar.setVisibility(View.VISIBLE);

        /*Intent intent = new Intent();
        String sms_body = intent.getExtras().getString("sms_body");
        sms = sms_body;*/


            mPhone = phon.getText().toString();
            mName = name.getText().toString();
            mPassword = pass.getText().toString();
            try {
                getHttpResponse();


                final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.alert_dialog);

                // set the custom dialog components - text, image and button
                input = dialog.findViewById(R.id.editTextSms);


                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            getSMSResponse();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("id", mID);
                            cv.put("token", mToken);
                            cv.put("name", mName);
                            db.insert("orzutable", null, cv);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }

    }

    public void getHttpResponse() throws IOException {
        // api?appid=&opt=register_user&phone=&password=&name=
        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=register_user&phone="
                + ccp.getFullNumberWithPlus() + mPhone
                + "&password=" + mPassword
                + "&name=" + mName;
        Log.e("failure Response URL", url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                if (mMessage.equals("noAuth")) {
                    Toast.makeText(getApplicationContext(), "No registered user!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();
                final char dm = (char) 34;

                Log.e("response", mMessage);

                try {
                    obj = new JSONObject(mMessage);
                    mStatus = obj.getString("auth_status");
                    mToken = obj.getString("_token");
                    mID = obj.getLong("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getSMSResponse() throws IOException {
        // api?appid=&opt=register_user&phone=&password=&name=
        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=check_sms&phone= "
                + ccp.getFullNumberWithPlus() + mPhone
                + "&code=" + input.getText();
        Log.e("failure Response URL", url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                if (mMessage.equals("noAuth")) {
                    Toast.makeText(getApplicationContext(), "No registered user!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();
                final char dm = (char) 34;

                Log.e("response", mMessage);

                try {
                    obj = new JSONObject(mMessage);
                    mStatus = obj.getString("check");

                    if (obj.getString("check").equals("yes")) {
                        Intent intent = new Intent(getApplicationContext(), ViewpagerStart.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

//        register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otp, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(otp);
    }


}
