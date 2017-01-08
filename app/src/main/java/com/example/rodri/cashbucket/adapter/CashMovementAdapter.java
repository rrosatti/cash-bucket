package com.example.rodri.cashbucket.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;

import java.util.List;

/**
 * Created by rodri on 1/6/2017.
 */

public class CashMovementAdapter extends RecyclerView.Adapter<CashMovementAdapter.MyViewHolder> {

    private Activity activity;
    private List<CashMovement> cashMovements;
    private Fragment fragment;
    private MyDataSource dataSource;
    private TypedArray images;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView displayValue;
        public ImageView displayIcon;
        public TextView displayDate;

        public MyViewHolder(View v) {
            super(v);

            displayValue = (TextView) v.findViewById(R.id.listItemCashMovement_txtValue);
            displayIcon = (ImageView) v.findViewById(R.id.listItemCashMovement_imgIcon);
            displayDate = (TextView) v.findViewById(R.id.listItemCashMovement_txtDate);

        }
    }

    public CashMovementAdapter(Activity activity, List<CashMovement> cashMovements) {
        this.activity = activity;
        this.cashMovements = cashMovements;
        this.dataSource = new MyDataSource(activity);
        this.images = activity.getResources().obtainTypedArray(R.array.cash_movement_images);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cash_movement, parent, false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CashMovement cashMovement = cashMovements.get(position);

        String sDay = String.valueOf(cashMovement.getDay());
        String sMonth = String.valueOf(cashMovement.getMonth());
        String sYear = String.valueOf(cashMovement.getYear());

        if (cashMovement.getDay() < 10) {
            sDay = "0" + sDay;
        }
        if (cashMovement.getMonth() < 10) {
            sMonth = "0" + sMonth;
        }

        String date = sDay+"/"+sMonth+"/"+sYear;

        String value = "R$ " + String.valueOf(cashMovement.getValue());

        holder.displayValue.setText(value);
        holder.displayDate.setText(date);
        holder.displayIcon.setImageResource(images.getResourceId(cashMovement.getType()-1, -1));
    }

    @Override
    public int getItemCount() {
        return cashMovements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
