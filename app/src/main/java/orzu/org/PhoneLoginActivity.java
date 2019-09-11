package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phone;
    EditText phoneCount;
    TextView phoneCountName;
    EditText password;
    TextView regist;
    Button button;
    ProgressBar progressBar;
    String mMessage;
    String mPassword;
    Long mID;
    String mStatus;
    String mToken;
    String code;
    String codeName;
    JSONObject obj;
    CountryCodePicker ccp;
    DBHelper dbHelper;
    LinearLayout linLay;
    static Boolean firsttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_phone_login);
        dbHelper = new DBHelper(this);
        firsttime = false;
        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);
        phone = (EditText)findViewById(R.id.editTextPhone);
        phoneCount = (EditText)findViewById(R.id.editTextPhoneCountry);
       // phoneCountName = (TextView) findViewById(R.id.ccpText);
        password = (EditText)findViewById(R.id.editTextPassword);
        button = (Button)findViewById(R.id.button_phone_login);
        regist = findViewById(R.id.button_phone_regist);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        linLay = findViewById(R.id.linearidccp);
        //ccp.registerPhoneNumberTextView(phone);

        float dip = 16f;
        Resources r = Resources.getSystem();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        //ccp.
        linLay.setOnClickListener(this);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = ccp.getSelectedCountryCodeWithPlus();
                //codeName = ccp.getSelectedCountryName();
                /*phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(getApplicationContext(), code), null, null, null);
                phone.setCompoundDrawablePadding((int) (code.length()*px)); setText(text.toUpperCase()) */
                phoneCount.setText(code);
               // phoneCountName.setText(codeName);
            }
        });

        code = ccp.getSelectedCountryCodeWithPlus();
        //codeName = ccp.getSelectedCountryName();
        /*phone.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(getApplicationContext(), code), null, null, null);
        phone.setCompoundDrawablePadding((int) (code.length()*px));*/
        phoneCount.setText(code);
       // phoneCountName.setText(codeName);


        button.setOnClickListener(this);
        regist.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.button_phone_login):
                progressBar.setVisibility(View.VISIBLE);
                mMessage = phone.getText().toString();
                mPassword = password.getText().toString();
                try {
                    getHttpResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case (R.id.button_phone_regist):
                Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                startActivity(intent);
                finish();
                break;

            case (R.id.linearidccp):
                Log.e("linear", String.valueOf(ccp.isShown()));
                //performClick(ccp);

                break;
        }


    }

    public void getHttpResponse() throws IOException {
       // Log.e("Country phone", ccp.getFullNumberWithPlus());
        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=user_auth&phone=" + ccp.getFullNumberWithPlus() + phone.getText() + "&password=" + mPassword;

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
                if (mMessage.equals("noAuth")){
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

                    if (mMessage.equals(dm + "noAuth" + dm)) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                        startActivity(intent);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();

                    } else {
                        try {
                        obj = new JSONObject(mMessage);
                        mStatus = obj.getString("auth_status");
                        mToken = obj.getString("_token");
                        mID = obj.getLong("id");

                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("token", mToken);
                            cv.put("id", mID);
                            db.insert("orzutable", null, cv);

                           /* final SharedPreferences prefs = getSharedPreferences("", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("Username", true);
                            editor.apply();*/


                        if (obj.getString("auth_status").equals("yes")) {
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(intent);
                            firsttime = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                            finish();
                        }
                    } catch (JSONException e) {
                      e.printStackTrace(); }
                }

            }
        });
    }

    public static boolean performClick(View view) {
        return view.isEnabled() && view.isClickable() && view.performClick();
    }


}
