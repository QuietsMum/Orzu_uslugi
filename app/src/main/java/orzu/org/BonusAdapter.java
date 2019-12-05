package orzu.org;


import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.MyViewHolder>{
    List<Bonuses> maps;
    Context context;
    private ItemClickListener mClickListener;
    BonusAdapter(Context context, List<Bonuses> maps) {
        this.context = context;
        this.maps = maps;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bonus_item, parent,false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(maps.get(position).getName());
        holder.description.setText(maps.get(position).getDescription());
        holder.date.setText(maps.get(position).getDate());
        if(maps.get(position).getLogo()!=null){
            Picasso.get().load(maps.get(position).getLogo()).into(holder.logo);
        }else{
            holder.logo.setImageResource(maps.get(position).getLogos());
        }
        holder.percent.setTextColor(Color.parseColor("#fe8c00"));
        holder.percent.setText(maps.get(position).getPercentage());
        TextPaint paint = holder.percent.getPaint();
        float width = paint.measureText(maps.get(position).getPercentage());
        Shader textShader = new LinearGradient(0, 0, width, holder.percent.getTextSize(),
                new int[]{
                        Color.parseColor("#fe8c00"),
                        Color.parseColor("#f83600"),
                }, null, Shader.TileMode.CLAMP);
        holder.percent.getPaint().setShader(textShader);



    }
    @Override
    public int getItemCount() {
        return maps.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,description,date, percent;
        ImageView logo;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_of_partner);
            description = itemView.findViewById(R.id.description_of_partner);
            date = itemView.findViewById(R.id.date_of_partner);
            logo = itemView.findViewById(R.id.logo_of_partner);
            percent = itemView.findViewById(R.id.percent);

            percent= itemView.findViewById(R.id.percentage);



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
