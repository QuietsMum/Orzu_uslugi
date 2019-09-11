package orzu.org;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateTaskAmout extends AppCompatActivity implements View.OnClickListener {

    TextView buttonCreate;
    TextView buttonCreateLeft;
    TextView buttonCreateRight;
    TextView texthisprice;
    TextInputLayout amoutEdit;
    TextInputEditText amoutEditPrice;
    int counter;
    int pricetype;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        setContentView(R.layout.activity_create_task_amout);
        counter = 1;
        pricetype = 1;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        amoutEdit = findViewById(R.id.amout_text_input);
        amoutEditPrice = findViewById(R.id.editCreateAmout);
        texthisprice = findViewById(R.id.texthisprice);
        texthisprice.setVisibility(View.INVISIBLE);
        buttonCreate = findViewById(R.id.createAmout);
        buttonCreateLeft = findViewById(R.id.createAmout_buttonleft);
        buttonCreateRight = findViewById(R.id.createAmout_buttonright);
        buttonCreate.setOnClickListener(this);
        buttonCreateLeft.setOnClickListener(this);
        buttonCreateRight.setOnClickListener(this);
        fa = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (view.getId()) {
            case R.id.createAmout:
                if (counter == 1) {
                    pricetype = 1;
                    editor.putString(Util.TASK_PPRICE, String.valueOf(amoutEditPrice.getText()));
                    editor.putString(Util.TASK_PPRICE_TYPE, String.valueOf(pricetype));
                    editor.apply();
                } else if (counter == 2) {
                    pricetype = 2;
                    editor.putString(Util.TASK_PPRICE, "Предложите цену");
                    editor.putString(Util.TASK_PPRICE_TYPE, String.valueOf(pricetype));
                    editor.apply();
                }
                Intent intent = new Intent(this, CreateTaskDetail.class);
                startActivity(intent);
                break;

            case R.id.createAmout_buttonleft:
                counter = 1;
                texthisprice.setVisibility(View.INVISIBLE);
                amoutEdit.setVisibility(View.VISIBLE);
                buttonCreateLeft.setBackgroundResource(R.drawable.circle_button_left_solid);
                buttonCreateLeft.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                buttonCreateRight.setBackgroundResource(R.drawable.circle_button_right);
                buttonCreateRight.setTextColor(getResources().getColor(R.color.colorAccent));
                break;

            case R.id.createAmout_buttonright:
                counter = 2;
                texthisprice.setVisibility(View.VISIBLE);
                amoutEdit.setVisibility(View.INVISIBLE);
                buttonCreateLeft.setBackgroundResource(R.drawable.circle_button_left);
                buttonCreateLeft.setTextColor(getResources().getColor(R.color.colorAccent));
                buttonCreateRight.setBackgroundResource(R.drawable.circle_button_right_solid);
                buttonCreateRight.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                break;
        }
    }
}
