package com.android.carrental.help;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.carrental.R;
import com.android.carrental.adapter.IssueAdapter;

import java.util.ArrayList;
import java.util.List;

public class Help extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private IssueAdapter mAdapter;
    List<String> mIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setTitle("Help");
        mIssues = new ArrayList<>();
        mIssues.add("Trip Issues and Refunds");
        mIssues.add("Account and payment Options");
        mIssues.add("A guide to Rent a Car");
        mIssues.add("Contact Customer Service");
        mRecyclerView = (RecyclerView) findViewById(R.id.IssuesDisplay);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new IssueAdapter(this, mIssues);
        mRecyclerView.setAdapter(mAdapter);
    }
}
