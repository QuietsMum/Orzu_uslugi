package orzu.org.Notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import orzu.org.R;


public class AdapterDifferentLayout extends RecyclerView.Adapter {

    private List<Literature> logos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AdapterDifferentLayout(Context context, List<Literature> logos) {
        this.mInflater = LayoutInflater.from(context);
        this.logos = logos;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Literature.TYPE_BOOK:
                view = mInflater.inflate(R.layout.main_item, parent, false);
                return new ViewHolder(view);
            case Literature.TYPE_MAGAZINE:
                view = mInflater.inflate(R.layout.feedback_item, parent, false);
                return new ViewHolder2(view);
            case Literature.TYPE_NEWSPAPER:
                view = mInflater.inflate(R.layout.thrd, parent, false);
                return new ViewHolder3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Literature.TYPE_BOOK:
                ((ViewHolder) holder).bindView(position);
                break;
            case Literature.TYPE_MAGAZINE:
                ((ViewHolder2) holder).bindView(position);
                break;
            case Literature.TYPE_NEWSPAPER:
                ((ViewHolder3) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return logos.get(position).getType();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return logos.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView header, desc, price, usluga, data;

        ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.textView);
            desc = itemView.findViewById(R.id.textView2);
            price = itemView.findViewById(R.id.textView3);
            usluga = itemView.findViewById(R.id.textView4);
            data = itemView.findViewById(R.id.textView8);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            ChooseYouItem nik = (ChooseYouItem)logos.get(position);
            Log.wtf("adapterqw",nik.getHeader());
            header.setText(nik.getHeader());
            desc.setText(nik.getDesc());
            price.setText(nik.getPrice());
            usluga.setText(nik.getUsluga());
            data.setText(nik.getData());
            // bind data to the views
            // textView.setText()...
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewOtziv;
        TextView feedback_name, narrFeedback, countFeedback;

        ViewHolder2(View itemView) {
            super(itemView);
            imageViewOtziv = itemView.findViewById(R.id.imageViewOtziv);
            feedback_name = itemView.findViewById(R.id.feedback_name);
            narrFeedback = itemView.findViewById(R.id.narrFeedback);
            countFeedback = itemView.findViewById(R.id.countFeedback);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            feedbackItem feed = (feedbackItem) logos.get(position);
            imageViewOtziv.setImageResource(feed.getImage());
            feedback_name.setText(feed.getName());
            narrFeedback.setText(feed.getFeed_desc());
            countFeedback.setText(feed.getCount());
            // bind data to the views
            // textView.setText()...
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView kondicc, desc;

        ViewHolder3(View itemView) {
            super(itemView);
            kondicc = itemView.findViewById(R.id.kondic);
            desc = itemView.findViewById(R.id.desc);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            OtklikITem kondic = (OtklikITem) logos.get(position);
            kondicc.setText(kondic.getKondic());
            desc.setText(kondic.getDescrip_kondic());
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}