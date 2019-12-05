package orzu.org;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Fragment5 extends Fragment {

    RecyclerView bonus_recycler;
    List<Bonuses> bonuses = new ArrayList<>();
    CardView cardView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_5,container,false);

        bonus_recycler = v.findViewById(R.id.bonus_recycler);
        cardView = v.findViewById(R.id.card_of_partner);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);
        bonus_recycler.setHasFixedSize(true);
        bonus_recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        bonus_recycler.setLayoutManager(llm);
        bonuses.add(new Bonuses("Starbucks","Небольшое описание партнера символов на 75!","С нами с 01.01.2020",R.drawable.starbucks));
        bonuses.add(new Bonuses("Nike","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/UP1M/1517855940dsynj.jpg"));
        bonuses.add(new Bonuses("Спортмастер","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/r3Gt7/15166355196WkVV.png"));
        bonuses.add(new Bonuses("Халык Банк","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/QFFuT6/1522139793hgt2Q.png"));
        bonuses.add(new Bonuses("7 cups coffee","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/nr/1517906497r4kff.jpg"));
        bonuses.add(new Bonuses("Burger King","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/v5HyA/1516635909Hp3Gz.png"));
        bonuses.add(new Bonuses("Costa Coffee","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/AUnAM/1516033578vrfx4.png"));
        bonuses.add(new Bonuses("KFC","Небольшое описание партнера символов на 75!","С нами с 01.01.2020","http://mega.kz/media/shops/wu3Bq/1516636228dxrCG.png"));
        BonusAdapter adapter = new BonusAdapter(getContext(),bonuses);
        bonus_recycler.setAdapter(adapter);
        adapter.setClickListener(new BonusAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return v;
    }
}
