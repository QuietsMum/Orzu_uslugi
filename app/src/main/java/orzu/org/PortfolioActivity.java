package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PortfolioActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backAct;
    CardView cardView;
    RecyclerView rv;
    List<Bonuses> list = new ArrayList<>();
    TextView ok_portfolio,name,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryPurpleTop));
        setContentView(R.layout.activity_portfolio);


        list.add(new Bonuses("Cappuccino", "-10%"));
        list.add(new Bonuses("Espresso","-10%"));
        list.add(new Bonuses("Long Black","-10%"));
        list.add(new Bonuses("Macchiato","-10%"));
        list.add(new Bonuses("Latte","-10%"));
        list.add(new Bonuses("Флэт уайт","-10%"));
        list.add(new Bonuses("Кофе с молоком","-10%"));
        list.add(new Bonuses("Piccolo Latte","-10%"));
        rv = findViewById(R.id.rv_portfolio);
        ok_portfolio = findViewById(R.id.ok_portfolio);
        ok_portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        AdapterPartnerBonuses adapter = new AdapterPartnerBonuses(this,list);
        cardView = findViewById(R.id.card_of_portfolio);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners2);
        backAct = findViewById(R.id.create_amount_back);
        backAct.setOnClickListener(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
