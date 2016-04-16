package com.example.mastek.blue.deep.swasthtesting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class FeedbackAdapter extends BaseAdapter {
    public LayoutInflater mLayoutInflater;
    Feedback[] mFeedbackCollection;

    public FeedbackAdapter(FeedbackActivity feedbackActivity, Feedback[] feedbackCollection) {
        mLayoutInflater = LayoutInflater.from(feedbackActivity);
        mFeedbackCollection = feedbackCollection;
    }

    @Override
    public int getCount() {
        return mFeedbackCollection.length;
    }

    @Override
    public Object getItem(int position) {
        return mFeedbackCollection[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.feedback_layout, parent, false);
        }

        TextView feedbackQuestion = (TextView) convertView.findViewById(R.id.feedbackQuestion);
        feedbackQuestion.setText(null);
        String question = mFeedbackCollection[position].question;
        feedbackQuestion.setText(String.format("(%d.) %s", (position + 1), question));

        RadioButton option1 = (RadioButton) convertView.findViewById(R.id.option1);
        RadioButton option2 = (RadioButton) convertView.findViewById(R.id.option2);
        RadioButton option3 = (RadioButton) convertView.findViewById(R.id.option3);
        RadioButton option4 = (RadioButton) convertView.findViewById(R.id.option4);
        RadioButton option5 = (RadioButton) convertView.findViewById(R.id.option5);

        Feedback currentFeedback = mFeedbackCollection[position];

        if (currentFeedback.choice1.equals("-"))
            option1.setVisibility(View.GONE);
        else
            option1.setText(currentFeedback.choice1);

        if (currentFeedback.choice2.equals("-"))
            option2.setVisibility(View.GONE);
        else
            option2.setText(currentFeedback.choice2);

        if (currentFeedback.choice3.equals("-"))
            option3.setVisibility(View.GONE);
        else
            option3.setText(currentFeedback.choice3);

        if (currentFeedback.choice4.equals("-"))
            option4.setVisibility(View.GONE);
        else
            option4.setText(currentFeedback.choice4);

        if (currentFeedback.choice5.equals("-"))
            option5.setVisibility(View.GONE);
        else
            option5.setText(currentFeedback.choice5);

        return convertView;
    }
}
