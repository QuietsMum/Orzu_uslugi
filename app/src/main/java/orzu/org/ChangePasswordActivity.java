package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar bar;
    EditText oldPwd, newPwd, newPwdRe;
    FloatingActionButton ok;
    ImageView back;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryOrangeTop));
        setContentView(R.layout.activity_change_password);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
//        getSupportActionBar().setTitle("Изменить пароль");
//        getSupportActionBar().setElevation(0);

        bar = findViewById(R.id.progressBarLogin_reg);
        back = findViewById(R.id.change_back);
        cardView = findViewById(R.id.constrlog2);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);
        oldPwd = findViewById(R.id.old_pwd_edit);
        newPwd = findViewById(R.id.new_pwd_edit);
        newPwdRe = findViewById(R.id.new_pwd_edit_rewrite);
        ok = findViewById(R.id.button_change_pwd);
        ok.setOnClickListener(this);
        back.setOnClickListener(this);

        bar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_back:
                finish();
                break;
            case R.id.button_change_pwd:
                bar.setVisibility(View.VISIBLE);
                if (!newPwd.getText().toString().equals(newPwdRe.getText().toString())) {
                    Toast.makeText(this, "Пароли не совпадают "+newPwd.getText()+" "+newPwdRe.getText(), Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.GONE);
                }
                break;
        }
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
