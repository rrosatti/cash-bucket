package com.example.rodri.cashbucket.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodri.cashbucket.R;
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

        public MyViewHolder(View v) {
            super(v);

            displaySettingsOp = (TextView) v.findViewById(R.id.listItemSettings_txtSettingsOp);
        }
    }

    public SettingsAdapter(Activity activity, List<String> settingsOp, Fragment fragment) {
        this.activity = activity;
        this.settingsOp = settingsOp;
        this.fragment = fragment;
        this.dataSource = new MyDataSource(activity);

        for (int i=0; i<settingsOp.size(); i++) {
            System.out.println(settingsOp.get(i));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_settings, parent, false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String settings = settingsOp.get(position);

        holder.displaySettingsOp.setText(settings);
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
