package orzu.org;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder> {

    private static NameItemSelect sw;
    private List<category_model> maps;
    Context context;

    public MainCategoryAdapter(Context context, List<category_model> maps) {
        this.context = context;
        this.maps = maps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_main, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.a.setText(maps.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView a;
        LinearLayout linear_for_category;

        MyViewHolder(View itemView) {
            super(itemView);
            a = itemView.findViewById(R.id.category_name);
            linear_for_category = itemView.findViewById(R.id.linear_for_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sw.onItemSelectedListener(view, getAdapterPosition());
        }
        void colors(int pos){

        }
    }

    public static void setSelect(NameItemSelect select) {
        MainCategoryAdapter.sw = select;
    }
}