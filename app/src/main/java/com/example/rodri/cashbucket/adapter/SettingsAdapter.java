package com.example.rodri.cashbucket.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
import com.example.rodri.cashbucket.activity.ManageAccountActivity;
import com.example.rodri.cashbucket.activity.ManageBankAccountActivity;
import com.example.rodri.cashbucket.activity.ManageNotificationsActivity;
import com.example.rodri.cashbucket.activity.ManageWalletActivity;
import com.example.rodri.cashbucket.database.MyDataSource;

import java.util.List;

/**
 * Created by rodri on 1/8/2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private Activity activity;
    private Fragment fragment;
    private List<String> settingsOp;
    private MyDataSource dataSource;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView displaySettingsOp;
        public int settingsId;

        public MyViewHolder(View v) {
            super(v);

            displaySettingsOp = (TextView) v.findViewById(R.id.listSimpleItem_txtItem);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (settingsId) {
                        case 0: {
                            Intent i = new Intent(activity, ManageAccountActivity.class);
                            fragment.startActivity(i);
                            break;
                        }
                        case 1: {
                            Intent i = new Intent(activity, ManageBankAccountActivity.class);
                            fragment.startActivity(i);
                            break;
                        }
                        case 2: {
                            Intent i = new Intent(activity, ManageWalletActivity.class);
                            fragment.startActivity(i);
                            break;
                        }
                        case 3: {
                            Intent i = new Intent(activity, ManageNotificationsActivity.class);
                            fragment.startActivity(i);
                            break;
                        }
                    }
                }
            });
        }
    }

    public SettingsAdapter(Activity activity, List<String> settingsOp, Fragment fragment) {
        this.activity = activity;
        this.settingsOp = settingsOp;
        this.fragment = fragment;
        this.dataSource = new MyDataSource(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_simple_item, parent, false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String settings = settingsOp.get(position);

        holder.displaySettingsOp.setText(settings);
        holder.settingsId = position;
    }

    @Override
    public int getItemCount() {
        return settingsOp.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
