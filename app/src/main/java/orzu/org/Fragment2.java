package orzu.org;

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
import orzu.org.Notification.Literature;

public class Fragment2 extends Fragment implements View.OnClickListener {

    String url = "https://orzu.org/tasks/new/techrepair/techrepairother?token=";
    DBHelper dbHelper;
    ArrayList<Map<String, Object>> data;
    ArrayList<Map<String, Object>> truedata;
    List<Literature> lit;
    RecyclerView rv;
    AdapterDifferentLayout adapter;
    TextView btnNotif;
    TextView btnMessage;
    String messageNot;
    String idUserNot;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstantState){
        final View view = inflater.inflate(R.layout.fragment_main_2, container, false);


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

        if (c != null) {
            // Loop through all Results
            do {
                int tokenColIndex = c.getColumnIndex("token");
                int idColIndex = c.getColumnIndex("id");
                int mesColIndex = c.getColumnIndex("message");
                /*messageNot = c.getString(mesColIndex);
                idUserNot = c.getString(idColIndex);*/
            } while (c.moveToNext());
        }

        rv = view.findViewById(R.id.rv_notif);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        //adapter = new AdapterDifferentLayout(getActivity(), lit);
        rv.setAdapter(adapter);
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
                break;
            case R.id.notif_buttonright:
                btnNotif.setBackgroundResource(R.drawable.circle_button_left);
                btnNotif.setTextColor(getResources().getColor(R.color.colorAccent));
                btnMessage.setBackgroundResource(R.drawable.circle_button_right_solid);
                btnMessage.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                break;
        }
    }
}

