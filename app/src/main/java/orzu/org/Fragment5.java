package orzu.org;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class Fragment5 extends Fragment implements View.OnClickListener {

    RecyclerView bonus_recycler, bonus_recycler_qr;
    List<Bonuses> bonuses = new ArrayList<>();
    CardView cardView;
    TextView bonus_left, bonus_right;
    ImageView tri_left, tri_right, qr_code;
    ConstraintLayout bonus_left_const, bonus_right_const;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_5, container, false);

        bonus_recycler = v.findViewById(R.id.bonus_recycler);
        bonus_recycler_qr = v.findViewById(R.id.bonus_recycler_qr);
        cardView = v.findViewById(R.id.card_of_partner);
        bonus_left = v.findViewById(R.id.bonus_left);
        bonus_right = v.findViewById(R.id.bonus_right);
        tri_left = v.findViewById(R.id.bonus_tri_left);
        tri_right = v.findViewById(R.id.bonus_tri_right);
        qr_code = v.findViewById(R.id.qr_code);
        bonus_left_const = v.findViewById(R.id.bonus_left_const);
        bonus_right_const = v.findViewById(R.id.bonus_right_const);

        bonus_left.setOnClickListener(this);
        bonus_right.setOnClickListener(this);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);
        bonus_recycler.setHasFixedSize(true);
        bonus_recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        bonus_recycler.setLayoutManager(llm);
        bonus_recycler_qr.setHasFixedSize(true);
        bonus_recycler_qr.setNestedScrollingEnabled(false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        bonus_recycler_qr.setLayoutManager(llm2);

        bonuses.add(new Bonuses("Starbucks", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-20%", R.drawable.starbucks));
        bonuses.add(new Bonuses("Nike", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-10%", "http://mega.kz/media/shops/UP1M/1517855940dsynj.jpg"));
        bonuses.add(new Bonuses("Спортмастер", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-15%", "http://mega.kz/media/shops/r3Gt7/15166355196WkVV.png"));
        bonuses.add(new Bonuses("Халык Банк", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-10%", "http://mega.kz/media/shops/QFFuT6/1522139793hgt2Q.png"));
        bonuses.add(new Bonuses("7 cups coffee", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-20%", "http://mega.kz/media/shops/nr/1517906497r4kff.jpg"));
        bonuses.add(new Bonuses("Burger King", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-5%", "http://mega.kz/media/shops/v5HyA/1516635909Hp3Gz.png"));
        bonuses.add(new Bonuses("Costa Coffee", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-30%", "http://mega.kz/media/shops/AUnAM/1516033578vrfx4.png"));
        bonuses.add(new Bonuses("KFC", "Небольшое описание партнера символов на 75!", "С нами с 01.01.2020", "-10%", "http://mega.kz/media/shops/wu3Bq/1516636228dxrCG.png"));

        BonusAdapter adapter = new BonusAdapter(getContext(), bonuses);
        bonus_recycler.setAdapter(adapter);
        adapter.setClickListener(new BonusAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap("https://play.google.com/store/apps/details?id=orzu.org&referrer=" + Common.userId, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        qr_code.setImageBitmap(bitmap);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bonus_left:
                tri_right.setVisibility(View.INVISIBLE);
                tri_left.setVisibility(View.VISIBLE);
                bonus_left_const.setVisibility(View.VISIBLE);
                bonus_right_const.setVisibility(View.GONE);
                break;
            case R.id.bonus_right:
                tri_left.setVisibility(View.INVISIBLE);
                tri_right.setVisibility(View.VISIBLE);
                bonus_left_const.setVisibility(View.GONE);
                bonus_right_const.setVisibility(View.VISIBLE);
                break;
        }
    }
}
