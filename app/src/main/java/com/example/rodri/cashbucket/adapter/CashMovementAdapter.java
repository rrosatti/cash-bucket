package com.example.rodri.cashbucket.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.database.MyDataSource;
import com.example.rodri.cashbucket.model.CashMovement;
import com.example.rodri.cashbucket.util.Util;

import java.util.List;

/**
 * Created by rodri on 1/6/2017.
 */

public class CashMovementAdapter extends RecyclerView.Adapter<CashMovementAdapter.MyViewHolder> {

    private Activity activity;
    private List<CashMovement> cashMovements;
    private MyDataSource dataSource;
    private TypedArray images;
    private Util util = new Util();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView displayValue;
        public ImageView displayIcon;
        public TextView displayDate;
        public TextView displayDesc;
        public long cashMovementId;

        public MyViewHolder(View v) {
            super(v);

            displayValue = (TextView) v.findViewById(R.id.listItemCashMovement_txtValue);
            displayIcon = (ImageView) v.findViewById(R.id.listItemCashMovement_imgIcon);
            displayDate = (TextView) v.findViewById(R.id.listItemCashMovement_txtDate);
            displayDesc = (TextView) v.findViewById(R.id.listItemCashMovement_txtDesc);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showAlertDialog(cashMovementId);
                    return true;
                }
            });

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

        holder.cashMovementId = cashMovement.getId();

        if (cashMovement.getDay() < 10) {
            sDay = "0" + sDay;
        }
        if (cashMovement.getMonth() < 10) {
            sMonth = "0" + sMonth;
        }

        String date = sDay+"/"+sMonth+"/"+sYear;

        //String value = "R$ " + String.valueOf(cashMovement.getValue());
        String sValue = util.formatMoneyString(cashMovement.getValue(), "R$");

        holder.displayValue.setText(sValue);
        holder.displayDate.setText(date);
        holder.displayIcon.setImageResource(images.getResourceId(cashMovement.getType()-1, -1));
        holder.displayDesc.setText(cashMovement.getDesc());
    }

    @Override
    public int getItemCount() {
        return cashMovements.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void showAlertDialog(final long cashMovementId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_delete_cash_movement);

        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCashMovement(cashMovementId);
            }
        });

        builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteCashMovement(long cashMovementId) {
        try {
            dataSource.open();

            boolean deleted = dataSource.deleteCashMovement(cashMovementId);

            if (deleted) {
                Toast.makeText(activity, R.string.toast_refresh_page, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, R.string.toast_cash_movement_not_removed, Toast.LENGTH_SHORT).show();
            }

            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource.close();
        }
    }
}
