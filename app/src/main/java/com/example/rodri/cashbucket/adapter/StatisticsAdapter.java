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
import com.example.rodri.cashbucket.activity.StatisticsChartActivity;

import java.util.List;

/**
 * Created by rodri on 2/17/2017.
 */

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.MyViewHolder> {

    private Fragment fragment;
    private List<String> options;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOp;
        public int pos;

        public MyViewHolder(View view) {
            super(view);

            txtOp = (TextView) view.findViewById(R.id.listSimpleItem_txtItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSelectedOption(pos);
                }
            });

        }
    }

    public StatisticsAdapter(Fragment fragment, List<String> options) {
        this.fragment = fragment;
        this.options = options;
    }

    private void openSelectedOption(int pos) {
        switch (pos) {
            case 0: {
                Intent i = new Intent(fragment.getActivity(), StatisticsChartActivity.class);
                fragment.getActivity().startActivity(i);
                break;
            }
            case 1: {
                break;
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_simple_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String item = options.get(position);

        holder.pos = position;
        holder.txtOp.setText(item);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }



}
