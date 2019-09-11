package orzu.org;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ArrayAdapterSubs extends RecyclerView.Adapter<ArrayAdapterSubs.MyViewHolder>{

    static NameItemSelect select;
    List<String> maps;
    Context context;

    public ArrayAdapterSubs(Context context,List<String> maps) {
        this.context = context;
        this.maps = maps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_view, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.a.setText(maps.get(position));
        holder.a.setActivated(true);

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RadioButton a;

        MyViewHolder(View itemView) {
            super(itemView);
            a = itemView.findViewById(R.id.radioButnSub);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            select.onItemSelectedListener(view, getAdapterPosition());
        }
    }

    public static void setSelect(NameItemSelect select) {
        ArrayAdapterMy.select = select;
    }
}