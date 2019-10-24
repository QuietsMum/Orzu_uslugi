package orzu.org;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import orzu.org.Notification.AdapterDifferentLayout;
import orzu.org.Notification.ChooseYouItem;
import orzu.org.Notification.Literature;
import orzu.org.Notification.OtklikITem;
import orzu.org.Notification.feedbackItem;
import orzu.org.chat.ChatAdapter;
import orzu.org.chat.chatItems;

public class Fragment2 extends Fragment {

    String url = "https://orzu.org/tasks/new/techrepair/techrepairother?token=";
    DBHelper dbHelper;
    ArrayList<Map<String, Object>> data;
    List<Literature> lit;
    RecyclerView rv;
    AdapterDifferentLayout adapter;
    String messageNot;
    String idUserNot;
    SQLiteDatabase db;
    ImageView no_task;
    TextView no_task_text;
    String idUser;
    CardView cardView;
    BottomNavigationView navigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.fragment_main_2, container, false);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzutable", null, null, null, null, null, null);
        c.moveToFirst();
        int idColIndex = c.getColumnIndex("id");
        int tokenColIndex = c.getColumnIndex("token");
        idUser = c.getString(idColIndex);
        c.close();
        db.close();
        cardView = view.findViewById(R.id.card_of_notif);
        cardView.setBackgroundResource(R.drawable.shape_card_topcorners);
        navigationView = view.findViewById(R.id.navigation);
        ProgressBar progressBar = view.findViewById(R.id.progressBarMain2);
        progressBar.setVisibility(View.INVISIBLE);

        no_task = view.findViewById(R.id.imageNoTask);
        no_task_text = view.findViewById(R.id.textViewNoTask);
        rv = view.findViewById(R.id.rv_notif);
        getNotif();
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        adapter = new AdapterDifferentLayout(getActivity(), lit);
        rv.setAdapter(adapter);

        adapter.setClickListener(new AdapterDifferentLayout.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.e("type", String.valueOf(adapter.getItemViewType(position)));

                if (adapter.getItemViewType(position) == 103) {
                    Intent intent = new Intent(getActivity(), ItemSubsNews.class);
                    startActivity(intent);
                } else if (adapter.getItemViewType(position) == 101) {
                    Intent intent = new Intent(getActivity(), TaskViewMain.class);
                    intent.putExtra("id", "13");
                    intent.putExtra("opt", "view");
                    intent.putExtra("mytask", "my");
                    startActivity(intent);
                } else if (adapter.getItemViewType(position) == 102) {
                    Intent intent = new Intent(getActivity(), Feedback.class);
                    intent.putExtra("idUserFeedback", idUser);
                    startActivity(intent);
                }


            }
        });



        return view;
    }


    private void getNotif() {
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzunotif", null, null, null, null, null, null);
        c.moveToFirst();
        lit = new ArrayList<>();
        if (c != null) {
            // Loop through all Results
            do {
                try {
                    int tokenColIndex = c.getColumnIndex("token");
                    int idColIndex = c.getColumnIndex("idUser");
                    int mesColIndex = c.getColumnIndex("message");
                    OtklikITem item1 = new OtklikITem("Вся правда о кондиционерах", "Максимально упрощайте ежедневные задачи для большей продуктивности рабочего дня");
                    ChooseYouItem item2 = new ChooseYouItem("Nikita", "Android develop", "10000$", "Making apps", "13,09,2018 10:20");
                    feedbackItem item3 = new feedbackItem(R.drawable.images_background_4, "Иван Иваныч", "2", "qwe", "Безопасная оплата картой и гарантия возврата денег. Компенсация в случае морального ущерба.");
                    messageNot = c.getString(mesColIndex);
                    idUserNot = c.getString(idColIndex);
                    feedbackItem item4 = new feedbackItem(R.drawable.images_background_4, idUserNot, "2", "qwe", messageNot);
                    lit.add(item3);
                    lit.add(item2);
                    lit.add(item1);
                    lit.add(item3);
                    lit.add(item2);
                    lit.add(item1);
                    lit.add(item2);
                    lit.add(item3);
                    lit.add(item4);
                    no_task.setVisibility(View.INVISIBLE);
                    no_task_text.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Log.wtf("sadas", "asdasddasdas");
                    no_task.setVisibility(View.VISIBLE);
                    no_task_text.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            } while (c.moveToNext());
        }
        adapter = new AdapterDifferentLayout(getActivity(), lit);
        adapter.notifyDataSetChanged();
        c.close();
        dbHelper.close();
        db.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notif_menu, menu);

        Drawable drawable = menu.findItem(R.id.settings_item_notif).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getActivity(), R.color.colorBackgrndFrg));
        menu.findItem(R.id.settings_item_notif).setIcon(drawable);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_item_notif:
                Intent intent = new Intent(getActivity(), NotificationSettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

