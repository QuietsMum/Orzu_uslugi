package orzu.org;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Objects;

public class PortfolioActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
        setContentView(R.layout.activity_portfolio);

        backAct = findViewById(R.id.image_back_partner);
        backAct.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
