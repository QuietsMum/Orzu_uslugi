package orzu.org;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import orzu.org.ui.login.model;

@SuppressWarnings("unchecked")
public class AdapterRespandableLV extends BaseExpandableListAdapter {

    public ArrayList<String> groupItem;
    public ArrayList<SubItem> tempChild;
    public ArrayList<Object> Childtem = new ArrayList<Object>();
    public LayoutInflater minflater;
    public Activity activity;
    ArrayList<String> subsServer = model.arraySubs;

    public AdapterRespandableLV(ArrayList<String> grList, ArrayList<Object> childItem) {
        groupItem = grList;
        this.Childtem = childItem;
    }


    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((ArrayList<SubItem>) Childtem.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    CheckBox text = null;


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        tempChild = (ArrayList<SubItem>) Childtem.get(groupPosition);

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.item_subcategory_view, null);
        }
        text = convertView.findViewById(R.id.radioButnSub);
        text.setText(tempChild.get(childPosition).getName());

        text.setChecked(tempChild.get(childPosition).getCheck());

        /*text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.isChecked()){
                    text.setChecked(false);
                }else{
                    text.setChecked(true);
                }
                Log.wtf("adapter",childPosition+" "+groupPosition);
            }
        });*/
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<SubItem>) Childtem.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItem.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.item_category_view, null);
        }
        TextView textView = convertView.findViewById(R.id.textViewCatSub);
        textView.setText(groupItem.get(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}