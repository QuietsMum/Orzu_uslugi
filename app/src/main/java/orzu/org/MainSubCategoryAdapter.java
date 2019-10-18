package orzu.org;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MainSubCategoryAdapter extends RecyclerView.Adapter<MainSubCategoryAdapter.MyViewHolder> {

    private static NameItemSelect sw;
    private List<category_model> maps;
    Context context;

    public MainSubCategoryAdapter(Context context, List<category_model> maps) {
        this.context = context;
        this.maps = maps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_subcategory_main, parent, false);
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

        MyViewHolder(View itemView) {
            super(itemView);
            a = itemView.findViewById(R.id.subcategory_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sw.onItemSelectedListener(view, getAdapterPosition());
        }
    }

    public static void setSelect(NameItemSelect select) {
        MainSubCategoryAdapter.sw = select;
    }
}