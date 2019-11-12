package orzu.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.CountDownTimer;
import android.os.Handler;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    FloatingActionButton button;
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
    CardView cardview;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    private String phoneVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login2);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackLight)));
        dbHelper = new DBHelper(this);
        fbAuth = FirebaseAuth.getInstance();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_REQUEST);
            }

        }

        cardview = findViewById(R.id.card_of_registr);
        cardview.setBackgroundResource(R.drawable.shape_card_topcorners);
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
        button =  findViewById(R.id.button_phone_login);

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
        View arv = view;
        if (name.getText().length() != 0 && phon.getText().length() != 0 && pass.getText().length() != 0) {
            progressBar.setVisibility(View.VISIBLE);

        /*Intent intent = new Intent();
        String sms_body = intent.getExtras().getString("sms_body");
        sms = sms_body;*/


            mPhone = phonCount.getText() + phon.getText().toString();
            Log.wtf("phone", mPhone);
            mName = name.getText().toString();
            mPassword = pass.getText().toString();
            sendCode();


            final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.alert_dialog);

            // set the custom dialog components - text, image and button
            input = dialog.findViewById(R.id.editTextSms);


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the cust
            TextView textView = dialog.findViewById(R.id.count);
            TextView btn = dialog.findViewById(R.id.btn);
            CountDownTimer timer = new CountDownTimer(60000, 1000) {

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    textView.setText("" + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    textView.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.VISIBLE);

                }
            }.start();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    btn.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    timer.start();
                    Log.wtf("sadaasd", "asdsadas");

                }
            });
            timer.start();
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verifyCode();


                    dialog.dismiss();
                }
            });

            dialog.show();


        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }

    }

    public void getHttpResponse() throws IOException {
        // api?appid=&opt=register_user&phone=&password=&name=
        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=register_user&phone="
                + mPhone
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
                } else {
                    Dialog dialog = new Dialog(LoginActivity2.this, android.R.style.Theme_Material_Light_NoActionBar);
                    dialog.setContentView(R.layout.dialog_no_internet);
                    Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                getHttpResponse();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();
                final char dm = (char) 34;
                Log.e("response", mMessage);
                if(mMessage.equals("\"This user alreday registered\"")){
                    Toast.makeText(getApplicationContext(), "No registered user!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), PhoneLoginActivity.class);
                    startActivity(intent);
                }
                try {
                    obj = new JSONObject(mMessage);
                    mStatus = obj.getString("auth_status");
                    mToken = obj.getString("_token");
                    mID = obj.getLong("id");
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("id", mID);
                    cv.put("token", mToken);
                    cv.put("name", mName);
                    db.insert("orzutable", null, cv);
                    db.close();
                    dbHelper.close();
                    Intent intent = new Intent(getApplicationContext(), RegistCity.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
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
                } else {
                    LoginActivity2.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Dialog dialog = new Dialog(LoginActivity2.this, android.R.style.Theme_Material_Light_NoActionBar);
                            dialog.setContentView(R.layout.dialog_no_internet);
                            Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        getSMSResponse();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            }, 500);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }

            @Override
            public void onResponse(Call call, Response
                    response) throws IOException {

                mMessage = response.body().string();
                final char dm = (char) 34;

                Log.e("response", mMessage);

                try {
                    obj = new JSONObject(mMessage);
                    mStatus = obj.getString("check");

                    if (obj.getString("check").equals("yes")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void sendCode() {


        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        signInWithPhoneAuthCredential(credential);
                        Toast.makeText(LoginActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d("asdsd", "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d("asdsd", "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            try {
                                getHttpResponse();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
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

    public void verifyCode() {

        String code = input.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


}