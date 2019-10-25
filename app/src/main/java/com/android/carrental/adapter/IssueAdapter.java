package com.android.carrental.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.carrental.R;
import com.android.carrental.help.HelpGuide;
import com.android.carrental.help.TripRefunds;
import com.android.carrental.payment.PaymentMethod;

import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mIssues;


    public IssueAdapter(Context mContext, List<String> mIssues) {
        this.mContext = mContext;
        this.mIssues = mIssues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.help_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.issue.setText(mIssues.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIssues.get(i).equals("Trip Issues and Refunds")) {
                    mContext.startActivity(new Intent(mContext, TripRefunds.class));
                } else if (mIssues.get(i).equals("Contact Customer Service")) {
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "6193987953")));
                } else if (mIssues.get(i).equals("Account and payment Options")) {
                    mContext.startActivity(new Intent(mContext, PaymentMethod.class));
                } else if (mIssues.get(i).equals("A guide to Rent a Car")) {
                    mContext.startActivity(new Intent(mContext, HelpGuide.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIssues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView issue;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            issue = (TextView) itemView.findViewById(R.id.help_issue);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

