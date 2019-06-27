package orzu.org;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>{

    ArrayList<Map<String, Object>> maps;
    Context context;
    static MainItemSelect select;
    public RVAdapter(Context context,ArrayList<Map<String, Object>> maps) {
        this.context = context;
        this.maps = maps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.a.setText(maps.get(position).get("Категория задачи").toString());
        holder.b.setText(maps.get(position).get("Задание").toString());
        holder.c.setText(maps.get(position).get("Сроки").toString());
        holder.d.setText( maps.get(position).get("Цена").toString());
        holder.e.setText(maps.get(position).get("За услугу").toString());
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView a,b,c,d,e;

        MyViewHolder(View itemView) {
            super(itemView);
            a = itemView.findViewById(R.id.textView);
            b = itemView.findViewById(R.id.textView2);
            c = itemView.findViewById(R.id.textView8);
            d = itemView.findViewById(R.id.textView3);
            e = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            select.onItemSelectedListener(view, getAdapterPosition());
        }
    }

    public static void setSelect(MainItemSelect select) {
        RVAdapter.select = select;
    }
}