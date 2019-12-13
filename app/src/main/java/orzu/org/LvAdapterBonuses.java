package orzu.org;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LvAdapterBonuses extends BaseAdapter {
    Context context;
    List<BonusList> list;
    LayoutInflater inflater;

    public LvAdapterBonuses(Context context, List<BonusList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.from(context).inflate(R.layout.item_bonuses_qr,null);
        TextView text = v.findViewById(R.id.text);
        TextView wallet = v.findViewById(R.id.wallet_cost);
        TextView date_bonus = v.findViewById(R.id.date_bonus);
        text.setText(list.get(i).getReason());
        if(list.get(i).getReason().equals("регистрация друга")) {
            wallet.setText("+"+list.get(i).getValue());
        }else{
            wallet.setText("-"+list.get(i).getValue());
        }
        if(i!=0){
            if(!list.get(i-1).date.equals(list.get(i).getDate())){
                date_bonus.setText(list.get(i).getDate());
                date_bonus.setVisibility(View.VISIBLE);
            }
        }else{
            date_bonus.setText(list.get(i).getDate());
            date_bonus.setVisibility(View.VISIBLE);
        }
        return v;
    }
}
