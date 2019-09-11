package orzu.org;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Map<String, Object>> maps;
    LayoutInflater lInflater;
    TextView a,b,c,d,e;

    public ListViewAdapter(Context context, ArrayList<Map<String, Object>> maps) {
        this.context = context;
        this.maps = maps;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int i) {
        return maps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.main_item, viewGroup, false);
        }

        a = v.findViewById(R.id.textView);
        b = v.findViewById(R.id.textView2);
        c = v.findViewById(R.id.textView8);
        d = v.findViewById(R.id.textView3);
        e = v.findViewById(R.id.textView4);

        a.setText(maps.get(i).get("Категория задачи").toString());
        b.setText(maps.get(i).get("Задание").toString());
        c.setText(maps.get(i).get("Сроки").toString());
        d.setText( maps.get(i).get("Цена").toString());
        e.setText(maps.get(i).get("За услугу").toString());


        return v;
    }
}
