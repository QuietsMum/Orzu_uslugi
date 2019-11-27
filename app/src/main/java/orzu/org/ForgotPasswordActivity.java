package orzu.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static orzu.org.MessageReceiver.extra;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phon;
    EditText phonCount;
    FloatingActionButton button;
    String mMessage;
    String mPhone;
    String code;
    ProgressBar progressBar;
    CountryCodePicker ccp;
    BroadcastReceiver otp;
    EditText input;
    DBHelper dbHelper;
    CardView cardview;
    EditText pass;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;

    private FirebaseAuth fbAuth;
    private String phoneVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_forgot_password);
        fbAuth = FirebaseAuth.getInstance();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_REQUEST);
            }

        }
        pass = (EditText) findViewById(R.id.editTextPass_forgot);
        cardview = findViewById(R.id.card_of_registr_forgot);
        cardview.setBackgroundResource(R.drawable.shape_card_topcorners);
        IntentFilter filter = new IntentFilter();
        filter.addAction("service.to.activity.transfer");
        otp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!extra.equals("")) {

                    String substr = extra.substring(extra.indexOf(" ") + 1);
                    input.setText(substr);
                    extra = "";

                }
            }
        };
        registerReceiver(otp, filter);

        progressBar = findViewById(R.id.progressBarLogin_reg_forgot);
        progressBar.setVisibility(View.INVISIBLE);
        phon = (EditText) findViewById(R.id.editTextPhone_reg_forgot);
        phonCount = (EditText) findViewById(R.id.editTextPhoneCountry_forgot);
        button =  findViewById(R.id.button_phone_login_forgot);

        ccp = (CountryCodePicker) findViewById(R.id.ccp_reg_forgot);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                code = ccp.getSelectedCountryCodeWithPlus();
                phonCount.setText(code);
            }
        });

        code = ccp.getSelectedCountryCodeWithPlus();
        phonCount.setText(code);
        button.setOnClickListener(this);
    }

    private static final int MY_REQUEST = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Напишите код вручную", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View view) {


        if (phon.getText().length() != 0&pass.getText().length()!=0) {
            mPhone = phonCount.getText() + phon.getText().toString();
            try {
                checkPhone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }

    }

    public void changePassoword() throws IOException {
        // api?appid=&opt=register_user&phone=&password=&name=
        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=user_param&act=forget_password&phone="+mPhone+"&password="+pass.getText();


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mMessage = e.getMessage();
                assert mMessage != null;
                if (mMessage.equals("noAuth")) {
                    Toast.makeText(getApplicationContext(), "No registered user!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                } else {
                    Dialog dialog = new Dialog(ForgotPasswordActivity.this, android.R.style.Theme_Material_Light_NoActionBar);
                    dialog.setContentView(R.layout.dialog_no_internet);
                    Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                changePassoword();
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                mMessage = Objects.requireNonNull(response.body()).string();
                if(mMessage.equals("\"password changed\"")){
                    Intent intent = new Intent(ForgotPasswordActivity.this,PhoneLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Password is not changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void checkPhone() throws IOException {
        // api?appid=&opt=register_user&phone=&password=&name=
        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=user_param&act=check_phone&phone="
                + mPhone;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mMessage = e.getMessage();
                if (Objects.equals(mMessage, "noAuth")) {
                    Toast.makeText(getApplicationContext(), "No registered user!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                } else {
                    Dialog dialog = new Dialog(ForgotPasswordActivity.this, android.R.style.Theme_Material_Light_NoActionBar);
                    dialog.setContentView(R.layout.dialog_no_internet);
                    Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                checkPhone();
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                mMessage = Objects.requireNonNull(response.body()).string();

                if (mMessage.equals("\"phone exists\"")) {
                    ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    sendCode();


                    ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            final Dialog dialog = new Dialog(ForgotPasswordActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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
                        }
                    });


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
                ForgotPasswordActivity.this,               // Activity (for callback binding)
                verificationCallbacks);

    }

    private void setUpVerificatonCallbacks() {
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            @NotNull PhoneAuthCredential credential) {

                        signInWithPhoneAuthCredential(credential);
                        Toast.makeText(ForgotPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NotNull FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded

                        }
                    }

                    @Override
                    public void onCodeSent(@NotNull String verificationId,
                                           @NotNull PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;

                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            try {
                                changePassoword();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            task.getException();// The verification code entered was invalid
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
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