package orzu.org;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment5 extends Fragment implements View.OnClickListener {

    private List<Bonuses> bonuses = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    CardView cardView;
    private ImageView tri_left;
    private ImageView tri_right;
    private ConstraintLayout bonus_left_const, bonus_right_const;
    private String ImagePath;
    private Uri URI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_5, container, false);

        RecyclerView bonus_recycler = v.findViewById(R.id.bonus_recycler);
        ListView bonus_recycler_qr = v.findViewById(R.id.bonus_recycler_qr);
        cardView = v.findViewById(R.id.card_of_partner);
        TextView bonus_left = v.findViewById(R.id.bonus_left);
        TextView bonus_right = v.findViewById(R.id.bonus_right);
        tri_left = v.findViewById(R.id.bonus_tri_left);
        tri_right = v.findViewById(R.id.bonus_tri_right);
        ImageView qr_code = v.findViewById(R.id.qr_code);
        bonus_left_const = v.findViewById(R.id.bonus_left_const);
        bonus_right_const = v.findViewById(R.id.bonus_right_const);
        TextView export = v.findViewById(R.id.export);

        bonus_left.setOnClickListener(this);
        bonus_right.setOnClickListener(this);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);
        bonus_recycler.setHasFixedSize(true);
        bonus_recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        bonus_recycler.setLayoutManager(llm);
        bonus_recycler_qr.setNestedScrollingEnabled(false);

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
                Intent intent = new Intent(getActivity(), PortfolioActivity.class);
                Map<String, Object> map;
                intent.putExtra("idpartner", "12");
                startActivity(intent);
            }
        });

        texts.add("Вы получили 1000 Ni");
        texts.add("Вы получили 1000 Ni");
        texts.add("Вы получили 1000 Ni");
        texts.add("Вы получили 1000 Ni");

        LvAdapterBonuses lvAdapter = new LvAdapterBonuses(getContext(), texts);
        bonus_recycler_qr.setAdapter(lvAdapter);

        Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logo_in_desktop);

        Bitmap finalOverlay = overlayBitmap(overlay);
        qr_code.setImageBitmap(finalOverlay);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePath = MediaStore.Images.Media.insertImage(
                        getActivity().getContentResolver(),
                        finalOverlay,
                        "OrzuQrCode",
                        "Qr код для получения бонусов"
                );
                URI = Uri.parse(ImagePath);
                Toast.makeText(getActivity(), "Картинка успешно сохранена", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
    public static Bitmap overlayBitmap(Bitmap overlay) {
        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType,  Object> hints;

        hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        //create qr code matrix
        writer = new  QRCodeWriter();
        try {
            matrix = writer.encode("https://play.google.com/store/apps/details?id=orzu.org&referrer="+ Common.userId,
                    BarcodeFormat.QR_CODE,
                    400,
                    400,
                    hints);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap image = toBitmapColour(matrix,R.color.colorAccentLight);
        int height = image.getHeight();
        int width = image.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, image.getConfig());

        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(image, new Matrix(), null);
        Bitmap resizeLogo = Bitmap.createScaledBitmap(overlay, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth  - resizeLogo.getWidth()) /2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) /2 ;

        canvas.drawBitmap(resizeLogo, centreX, centreY, null);

        return combined;
    }


    public static Bitmap toBitmapColour(BitMatrix matrix, int colour){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? colour : Color.WHITE);
            }
        }
        return bmp;
    }

    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {
        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) / 2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.bonus_menu, menu);
        menu.findItem(R.id.new_coin);
        SpannableString s = new SpannableString("1000 Ni");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        menu.findItem(R.id.new_coin).setTitle(s);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_coin) {

            return true;
        }
        return super.onOptionsItemSelected(item);
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
