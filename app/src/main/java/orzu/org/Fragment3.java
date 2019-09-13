package orzu.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import orzu.org.ui.login.model;

public class Fragment3 extends Fragment implements View.OnClickListener {

    String idUser;
    String text;
    String my;
    String mMessage;
    String mName;
    String mFiName;
    String mCount;
    String mCountReq;
    String mCity;
    String mBday;
    String mSex;
    String mNarr;
    TextView nameUser;
    TextView noReviews;
    TextView taskCountReq;
    TextView taskCount;
    TextView userCity;
    TextView userBday;
    TextView userSex;
    TextView userNarr;
    LinearLayout feedbackButtun;
    LinearLayout feedbackButtunAdd;

    TextView feedbackname1;
    TextView feedbackplus1;
    TextView feedbacknarr1;
    TextView feedbackcat1;
    ImageView feedbackimg1;
    ImageView feedbackimgUser1;

    TextView feedbackname2;
    TextView feedbackplus2;
    TextView feedbacknarr2;
    TextView feedbackcat2;
    ImageView feedbackimg2;
    ImageView feedbackimgUser2;

    View devider;
    ShimmerFrameLayout shim;
    View backblue;
    View backwhite;
    String mStatus;
    String image1;
    String image2;
    ImageView statusImg;
    String encodedString;
    ImageView imageViewName;
    ArrayList<String> returnValue = new ArrayList<>();

    DBHelper dbHelper;
    String tokenUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.fragment_main_3, container, false);

        final SharedPreferences prefs = getActivity().getSharedPreferences(" ", Context.MODE_PRIVATE);
        idUser = prefs.getString(Util.TASK_USERID, "");
        my = prefs.getString(Util.TASK_USERIDMY, "");

        dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        tokenUser = c.getString(tokenColIndex);
        c.moveToFirst();
        c.close();
        db.close();


        feedbackButtun = view.findViewById(R.id.linear_feed_click);
        feedbackButtun.setOnClickListener(this);

        nameUser = view.findViewById(R.id.name_fname);

        feedbackname1 = view.findViewById(R.id.userview_feedbacknamemy);
        feedbackplus1 = view.findViewById(R.id.userview_feedbackplusmy);
        feedbacknarr1 = view.findViewById(R.id.userview_feedbacknarrmy);
        feedbackcat1 = view.findViewById(R.id.userview_feedbackcatmy);
        feedbackimg1 = view.findViewById(R.id.userview_feedbackplus_imagemy);
        feedbackimgUser1 = view.findViewById(R.id.imageViewOtziv1my);
        feedbackname2 = view.findViewById(R.id.userview_feedbackname2my);
        feedbackplus2 = view.findViewById(R.id.userview_feedbackplus2my);
        feedbacknarr2 = view.findViewById(R.id.userview_feedbacknarr2my);
        feedbackcat2 = view.findViewById(R.id.userview_feedbackcat2my);
        feedbackimg2 = view.findViewById(R.id.userview_feedbackplus_image2my);
        feedbackimgUser2 = view.findViewById(R.id.imageViewOtziv2my);
        noReviews = view.findViewById(R.id.no_reviewsmy);
        devider = view.findViewById(R.id.divider_viewmy);
        backblue = view.findViewById(R.id.view_back_bluemy);
        backwhite = view.findViewById(R.id.view_back_whitemy);
        shim = (ShimmerFrameLayout) view.findViewById(R.id.userviewShimmerLayoutmy);
        shim.startShimmer();

        taskCountReq = view.findViewById(R.id.viewuser_task_count_reqmy);
        taskCount = view.findViewById(R.id.viewuser_task_countmy);
        userCity = view.findViewById(R.id.viewuser_citymy);
        userBday = view.findViewById(R.id.userview_bdaymy);
        userSex = view.findViewById(R.id.userview_sexmy);
        userNarr = view.findViewById(R.id.viewuser_narrativemy);
        statusImg = view.findViewById(R.id.imageViewStatusMy);

        imageViewName = view.findViewById(R.id.imageViewName);

        imageViewName.setImageBitmap(Common.bitmap);
        imageViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pix.start(getActivity(),Options.init().setRequestCode(100));
            }
        });

        try {
            getUserResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestFeedbackMy();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            imageViewName.setImageURI(Uri.parse(returnValue.get(0)));
            Log.wtf("asda","activityresult");
            Bitmap bitmap = ((BitmapDrawable) imageViewName.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();

            //Use your Base64 String as you wish
            encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            try {
                getEditAvatarResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getEditAvatarResponse() throws IOException {

        String url = "https://projectapi.pw/api/avatar";
        Log.e("userCreatedURL", url);
        OkHttpClient client = new OkHttpClient();
        File myFile = new File(Uri.parse(returnValue.get(0)).getPath());
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", myFile.getName(),
                        RequestBody.create(MediaType.parse("text/csv"), myFile))
                .addFormDataPart("userid", idUser)
                .addFormDataPart("utoken", tokenUser)
                .addFormDataPart("appid", "$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e("userCreatedAvatar", mMessage);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(),"Ваш профиль изменен", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    public void getUserResponse() throws IOException {
        // api?appid=&opt=view_user&=user=id

        String url = "https://orzu.org/api?appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&lang=ru&opt=view_user&user=" + idUser + "&param=more";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                mMessage = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(mMessage);
                    mName = jsonObject.getString("name");
                    mFiName = jsonObject.getString("fname");
                    mCount = jsonObject.getString("tasks");
                    mCountReq = jsonObject.getString("task_requests");
                    mCity = jsonObject.getString("city");
                    mBday = jsonObject.getString("birthday");
                    mSex = jsonObject.getString("sex");
                    mNarr = jsonObject.getString("about");
                    mStatus = jsonObject.getString("status");
                    if (mSex.equals("male")) {
                        mSex = "мужской";
                    } else mSex = "женский";
                    if (mFiName.equals("null")) {
                        text = mName;
                    } else text = mName + " " + mFiName;
                    if (mStatus.equals("false")) {
                        statusImg.setVisibility(View.INVISIBLE);
                    } else statusImg.setVisibility(View.VISIBLE);
                    ;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameUser.setText(text);
                            taskCountReq.setText(mCountReq);
                            taskCount.setText(mCount);
                            userCity.setText(mCity);
                            userBday.setText(mBday);
                            userSex.setText(mSex);
                            userNarr.setText(mNarr);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);
        Drawable drawable = menu.findItem(R.id.share_item).getIcon();
        Drawable drawable2 = menu.findItem(R.id.settings_item).getIcon();
        Drawable drawable3 = menu.findItem(R.id.item_subscribe).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        drawable2 = DrawableCompat.wrap(drawable2);
        drawable3 = DrawableCompat.wrap(drawable3);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.colorBackgrndFrg));
        DrawableCompat.setTint(drawable2, ContextCompat.getColor(getActivity(), R.color.colorBackgrndFrg));
        DrawableCompat.setTint(drawable3, ContextCompat.getColor(getActivity(), R.color.colorBackgrndFrg));
        menu.findItem(R.id.share_item).setIcon(drawable);
        menu.findItem(R.id.settings_item).setIcon(drawable2);
        menu.findItem(R.id.item_subscribe).setIcon(drawable3);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_item:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Привет, посмотри мой профиль на Orzu: https://orzu.org/profile/" + idUser);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.settings_item:
                Intent intent = new Intent(getActivity(), UserSettings.class);
                startActivity(intent);
                return true;
            case R.id.item_subscribe:
                Intent intent2 = new Intent(getActivity(), CategorySubscriptions.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Feedback.class);
        intent.putExtra("idUserFeedback", idUser);
        startActivity(intent);
    }

    public void requestFeedbackMy() {

        String url = "https://orzu.org/api?%20appid=$2y$12$esyosghhXSh6LxcX17N/suiqeJGJq/VQ9QkbqvImtE4JMWxz7WqYS&opt=reviews&act=view&userid=" + idUser + "&sort=all";
        OkHttpClient client = new OkHttpClient();
        final String[] name = new String[2];
        String plus;
        final String[] narr = new String[2];
        final String[] date = new String[2];
        int img;
        int dchar = 34;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.e("resultArrayFull", mMessage);

                if (mMessage.equals(Character.toString((char) dchar) + "No reviews yet" + Character.toString((char) dchar))) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            feedbackname1.setVisibility(View.GONE);
                            feedbacknarr1.setVisibility(View.GONE);
                            feedbackcat1.setVisibility(View.GONE);
                            feedbackplus1.setVisibility(View.GONE);
                            feedbackimg1.setVisibility(View.GONE);
                            feedbackimgUser1.setVisibility(View.GONE);
                            feedbackname2.setVisibility(View.GONE);
                            feedbacknarr2.setVisibility(View.GONE);
                            feedbackcat2.setVisibility(View.GONE);
                            feedbackplus2.setVisibility(View.GONE);
                            feedbackimg2.setVisibility(View.GONE);
                            feedbackimgUser2.setVisibility(View.GONE);
                            devider.setVisibility(View.GONE);
                            shim.setVisibility(View.INVISIBLE);
                            backblue.setVisibility(View.INVISIBLE);
                            backwhite.setVisibility(View.INVISIBLE);
                            noReviews.setVisibility(View.VISIBLE);
                        }
                    });

                } else {

                    try {
                        JSONArray jsonArray = new JSONArray(mMessage);
                        int bpSad = R.drawable.ic_sad;
                        int bpNorm = R.drawable.ic_neutral;
                        int bpHappy = R.drawable.ic_happy;

                        int lenght = jsonArray.length();
                        Log.e("lenghtArray", String.valueOf(lenght));
                        String feedName = "";
                        if (lenght > 1) {
                            lenght = 2;
                        } else if (lenght == 1) {
                            lenght = 1;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    feedbackname2.setVisibility(View.GONE);
                                    feedbacknarr2.setVisibility(View.GONE);
                                    feedbackcat2.setVisibility(View.GONE);
                                    feedbackplus2.setVisibility(View.GONE);
                                    feedbackimg2.setVisibility(View.GONE);
                                    feedbackimgUser2.setVisibility(View.GONE);
                                    devider.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            lenght = 0;
                        }
                        for (int i = 0; i < lenght; i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (i == 0) {
                                long like = jsonObject.getLong("like");
                                if (like == 0L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus1.setText("-1");
                                            feedbackimg1.setImageResource(bpSad);
                                        }
                                    });

                                } else if (like == 1L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus1.setText("0");
                                            feedbackimg1.setImageResource(bpNorm);
                                        }
                                    });

                                } else if (like == 2L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus1.setText("+1");
                                            feedbackimg1.setImageResource(bpHappy);
                                        }
                                    });

                                }
                                name[0] = jsonObject.getString("username");
                                narr[0] = jsonObject.getString("narrative");
                                date[0] = jsonObject.getString("datein");
                                image1 = jsonObject.getString("avatar");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        feedbackname1.setText(name[0]);
                                        feedbacknarr1.setText(narr[0]);
                                        feedbackcat1.setText(date[0]);
                                        Picasso.get().load("https://orzu.org"+image1).into(feedbackimgUser1);
                                    }
                                });


                            } else {
                                long like = jsonObject.getLong("like");
                                if (like == 0L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus2.setText("-1");
                                            feedbackimg2.setImageResource(bpSad);
                                        }
                                    });

                                } else if (like == 1L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus2.setText("0");
                                            feedbackimg2.setImageResource(bpNorm);
                                        }
                                    });

                                } else if (like == 2L) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            feedbackplus2.setText("+1");
                                            feedbackimg2.setImageResource(bpHappy);
                                        }
                                    });

                                }
                                name[1] = jsonObject.getString("username");
                                narr[1] = jsonObject.getString("narrative");
                                date[1] = jsonObject.getString("datein");
                                image2 = jsonObject.getString("avatar");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        feedbackname2.setText(name[1]);
                                        feedbacknarr2.setText(narr[1]);
                                        feedbackcat2.setText(date[1]);
                                        Picasso.get().load("https://orzu.org"+image2).into(feedbackimgUser2);
                                    }
                                });
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    shim.setVisibility(View.INVISIBLE);
                    backblue.setVisibility(View.INVISIBLE);
                    backwhite.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}

