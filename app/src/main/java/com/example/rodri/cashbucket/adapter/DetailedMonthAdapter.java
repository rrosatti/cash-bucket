package com.example.rodri.cashbucket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.model.DetailedMonth;
import com.example.rodri.cashbucket.util.Util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by rodri on 2/18/2017.
 */

public class DetailedMonthAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private HashMap<Integer, DetailedMonth> items;
    private static LayoutInflater inflater = null;
    private int[] groupPos;
    private Util util = new Util();
    private String[] months;
    private String[] cashMovementTypes;

    public DetailedMonthAdapter(Activity activity, LinkedHashMap<Integer, DetailedMonth> items) {
        this.activity = activity;
        this.items = items;
        this.groupPos = new int[items.size()];
        int i = 0;
        for (int key : items.keySet()) {
            groupPos[i++] = key;
        }
        months = activity.getResources().getStringArray(R.array.months);
        cashMovementTypes = activity.getResources().getStringArray(R.array.list_cash_movement_types);

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CashMovement getChild(int groupPosition, int childPosition) {
        System.out.println("group pos: " + groupPosition + " childPos: " + childPosition);
        System.out.println("GRoup: " + items.get(groupPosition+1));
        return items.get(groupPosition+1).getCashMovement(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public class ViewHolder {
        public TextView txtType;
        public TextView txtValue;
        public TextView txtDate;
        public TextView txtMonth;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();
        final CashMovement cashMovement = getChild(groupPosition, childPosition);
        if (convertView == null) {
            v = inflater.inflate(R.layout.custom_detailed_month_item, null);

            holder.txtType = (TextView) v.findViewById(R.id.customDMI_txtType);
            holder.txtValue = (TextView) v.findViewById(R.id.customDMI_txtValue);
            holder.txtDate = (TextView) v.findViewById(R.id.customDMI_txtDate);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.txtType.setText(cashMovementTypes[cashMovement.getType()-1]);
        holder.txtValue.setText(util.formatMoneyString(cashMovement.getValue(), "R$"));
        String date = cashMovement.getDay()+"/"+cashMovement.getMonth()+"/"+cashMovement.getYear();
        holder.txtDate.setText(date);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Testing: " + cashMovement.getType(), Toast.LENGTH_SHORT).show();
                showCustomDialog(cashMovement);
            }
        });

        return v;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition+1).getNumOfCashMovements();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition+1);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = convertView;
        if (convertView == null) {
            v = inflater.inflate(R.layout.custom_detailed_month_group, null);

            holder.txtMonth = (TextView) v.findViewById(R.id.customDMG_txtMonth);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.txtMonth.setText(months[groupPosition]);

        return v;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void showCustomDialog(CashMovement cashMovement) {

    }
}
