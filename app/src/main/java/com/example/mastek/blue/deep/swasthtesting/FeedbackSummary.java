package com.example.mastek.blue.deep.swasthtesting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackSummary extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView)findViewById(R.id.txtSummary);
        String summary = getIntent().getStringExtra("summary");
        textView.setText(summary);
        Toast.makeText(getApplicationContext(),"Summary..." + summary,Toast.LENGTH_LONG).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
