package orzu.org;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment5 extends Fragment implements View.OnClickListener {

    private List<Bonuses> bonuses = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    CardView cardView;
    private ImageView tri_left;
    private ImageView tri_right;
    private ConstraintLayout bonus_left_const, bonus_right_const;
    private String ImagePath;
    private Uri URI;
    Menu menuOfBonus;
    String idUser;
    MenuInflater inflater;
    MenuItem menuItem;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_5, container, false);

        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        c.moveToFirst();
        c.close();
        db.close();
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

        bonuses.add(new Bonuses("Starbucks",  "-20%", R.drawable.starbucks));
        bonuses.add(new Bonuses("Nike", "-10%", "http://mega.kz/media/shops/UP1M/1517855940dsynj.jpg"));
        bonuses.add(new Bonuses("Спортмастер","-15%", "http://mega.kz/media/shops/r3Gt7/15166355196WkVV.png"));
        bonuses.add(new Bonuses("Халык Банк", "-10%", "http://mega.kz/media/shops/QFFuT6/1522139793hgt2Q.png"));
        bonuses.add(new Bonuses("7 cups coffee", "-20%", "http://mega.kz/media/shops/nr/1517906497r4kff.jpg"));
        bonuses.add(new Bonuses("Burger King",  "-5%", "http://mega.kz/media/shops/v5HyA/1516635909Hp3Gz.png"));
        bonuses.add(new Bonuses("Costa Coffee",  "-30%", "http://mega.kz/media/shops/AUnAM/1516033578vrfx4.png"));
        bonuses.add(new Bonuses("KFC",  "-10%", "http://mega.kz/media/shops/wu3Bq/1516636228dxrCG.png"));

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
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        ImagePath = MediaStore.Images.Media.insertImage(
                                getActivity().getContentResolver(),
                                finalOverlay,
                                "OrzuQrCode",
                                "Qr код для получения бонусов"
                        );
                        URI = Uri.parse(ImagePath);
                        Toast.makeText(getActivity(), "Картинка успешно сохранена", Toast.LENGTH_LONG).show();
                    } else {
                        requestPermission(); // Code for permission
                    }
                } else {
                    ImagePath = MediaStore.Images.Media.insertImage(
                            getActivity().getContentResolver(),
                            finalOverlay,
                            "OrzuQrCode",
                            "Qr код для получения бонусов"
                    );
                    URI = Uri.parse(ImagePath);
                    Toast.makeText(getActivity(), "Картинка успешно сохранена", Toast.LENGTH_LONG).show();
                }

            }
        });
        try {
            getUserResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Разрешено", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Запрещено", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static Bitmap overlayBitmap(Bitmap overlay) {
        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType, Object> hints;

        hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        //create qr code matrix
        writer = new QRCodeWriter();
        try {
            matrix = writer.encode("https://play.google.com/store/apps/details?id=orzu.org&referrer=" + Common.userId,
                    BarcodeFormat.QR_CODE,
                    400,
                    400,
                    hints);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap image = toBitmapColour(matrix, R.color.colorAccentLight);
        int height = image.getHeight();
        int width = image.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, image.getConfig());

        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(image, new Matrix(), null);
        Bitmap resizeLogo = Bitmap.createScaledBitmap(overlay, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) / 2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;

        canvas.drawBitmap(resizeLogo, centreX, centreY, null);

        return combined;
    }


    public static Bitmap toBitmapColour(BitMatrix matrix, int colour) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? colour : Color.WHITE);
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
        SpannableString s = new SpannableString("");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        menu.findItem(R.id.new_coin).setTitle(s);
        this.menuOfBonus = menu;
        this.menuItem = menu.findItem(R.id.new_coin);
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
    private void getUserResponse() throws IOException {
        String url = "https://projectapi.pw/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_user&user=" + idUser + "&param=more";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    public void run() {
                        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Material_Light_NoActionBar);
                        dialog.setContentView(R.layout.dialog_no_internet);
                        Button dialogButton = (Button) dialog.findViewById(R.id.buttonInter);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    getUserResponse();
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
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               String mMessage = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(mMessage);

                    Common.wallet = jsonObject.getString("wallet");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpannableString s = new SpannableString(Common.wallet+" Ni");
                            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
                            menuItem.setTitle(s);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
