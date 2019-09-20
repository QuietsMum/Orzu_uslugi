package orzu.org;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Fragment2 extends Fragment implements View.OnClickListener {

    String url = "https://orzu.org/tasks/new/techrepair/techrepairother?token=";
    DBHelper dbHelper;
    ArrayList<Map<String, Object>> data;
    ArrayList<Map<String, Object>> truedata;
    List<Literature> lit;
    List<chatItems> chatItems = new ArrayList<>();
    RecyclerView rv, rv_of_chat;
    AdapterDifferentLayout adapter;
    TextView btnNotif;
    TextView btnMessage;
    String messageNot;
    String idUserNot;
    ChatAdapter chatAdapter;
    LinearLayout customTabs;
    FrameLayout chat_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState) {
        final View view = inflater.inflate(R.layout.fragment_main_2, container, false);

        customTabs = view.findViewById(R.id.custom_tabs);
        ProgressBar progressBar = view.findViewById(R.id.progressBarMain2);
        progressBar.setVisibility(View.INVISIBLE);
        btnNotif = view.findViewById(R.id.notif_buttonleft);
        btnMessage = view.findViewById(R.id.notif_buttonright);
        btnNotif.setOnClickListener(this);
        btnMessage.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("orzunotif", null, null, null, null, null, null);
        c.moveToFirst();
        lit = new ArrayList<>();
        if (c != null) {
            // Loop through all Results
            do {
                int tokenColIndex = c.getColumnIndex("token");
                int idColIndex = c.getColumnIndex("id");
                int mesColIndex = c.getColumnIndex("message");
                OtklikITem item1 = new OtklikITem("Вся правда о кондиционерах","Максимально упрощайте ежедневные задачи для большей продуктивности рабочего дня");
                ChooseYouItem item2 = new ChooseYouItem("Nikita","Android develop","10000$","Making apps","13,09,2018 10:20");
                feedbackItem item3 = new feedbackItem(R.drawable.images_background_4, "Иван Иваныч", "2", "qwe", "Безопасная оплата картой и гарантия возврата денег. Компенсация в случае морального ущерба.");
                lit.add(item3);
                lit.add(item2);
                lit.add(item1);
                lit.add(item3);
                lit.add(item2);
                lit.add(item1);
                lit.add(item2);
                lit.add(item3);
                /*messageNot = c.getString(mesColIndex);
                idUserNot = c.getString(idColIndex);*/
            } while (c.moveToNext());
        }
        rv_of_chat = view.findViewById(R.id.rv_of_chat);
        rv = view.findViewById(R.id.rv_notif);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv_of_chat.setHasFixedSize(true);
        rv_of_chat.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv_of_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        chatItems.add(new chatItems("Johny", "Kak dela?", "10:06", "1", Common.bitmap));
        chatItems.add(new chatItems("KakOnoEst", "Kak dela?", "10:08", "1", Common.bitmap));
        chatItems.add(new chatItems("Lol", "Kak dela?", "10:10", "1", Common.bitmap));
        chatItems.add(new chatItems("Maga", "Kak dela?", "10:53", "1", Common.bitmap));
        chatItems.add(new chatItems("Sanek", "Kak dela?", "11:06", "1", Common.bitmap));
        chatItems.add(new chatItems("FiraBrat", "Kak dela?", "18:06", "1", Common.bitmap));
        chatAdapter = new ChatAdapter(getContext(), chatItems);
        //adapter = new AdapterDifferentLayout(getActivity(), lit);
        rv.setAdapter(adapter);
        rv_of_chat.setAdapter(chatAdapter);

        chatAdapter.setClickListener(new ChatAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Common.nameOfChat = chatItems.get(position).getName();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        return view;
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notif_buttonleft:
                btnNotif.setBackgroundResource(R.drawable.circle_button_left_solid);
                btnNotif.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                btnMessage.setBackgroundResource(R.drawable.circle_button_right);
                btnMessage.setTextColor(getResources().getColor(R.color.colorAccent));

                customTabs.setVisibility(View.VISIBLE);
                rv.setVisibility(View.VISIBLE);
                rv_of_chat.setVisibility(View.GONE);

                break;
            case R.id.notif_buttonright:
                btnNotif.setBackgroundResource(R.drawable.circle_button_left);
                btnNotif.setTextColor(getResources().getColor(R.color.colorAccent));
                btnMessage.setBackgroundResource(R.drawable.circle_button_right_solid);
                btnMessage.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));

                customTabs.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                rv_of_chat.setVisibility(View.VISIBLE);


                break;
        }
    }
}

