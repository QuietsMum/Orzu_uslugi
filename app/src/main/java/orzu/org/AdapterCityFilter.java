package orzu.org;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterCityFilter extends RecyclerView.Adapter<AdapterCityFilter.MyViewHolder>{
    List<String> maps;
    Context context;
    private ItemClickListener mClickListener;
    AdapterCityFilter(Context context, List<String> maps) {
        this.context = context;
        this.maps = maps;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.city_item, parent,false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.header.setText(maps.get(position));

    }
    @Override
    public int getItemCount() {
        return maps.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header;

        MyViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.city_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
