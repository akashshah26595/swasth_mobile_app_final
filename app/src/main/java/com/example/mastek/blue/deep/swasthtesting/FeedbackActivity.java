package com.example.mastek.blue.deep.swasthtesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static final String SERVER_ADDRESS = "http://swasth-india.esy.es/swasth/insert_mobile_feedback.php";
    public static int credits = 0;
    SharedPreferences sharedPreferences;
    private int pos = 0;
    private boolean flag = false;
    private TextView progressText;
    private LinearLayout feedbackScrollViewLayout;
    private FeedbackAdapter feedbackAdapter;
    private Button previousButton;
    private RadioGroup optionsRadioGroup;
    private Map<String, String> hashMap;
    private ScrollView mainScrollView;
    private User user;
    private int CARD_NO;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        user = new User(getApplicationContext());
        progressText = (TextView) findViewById(R.id.progress_text);

        Button nextButton = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButton);
        //   mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);


        CARD_NO = user.getCardNumber();
        Bundle bundle = getIntent().getExtras();
        Parcelable[] parcelable = bundle.getParcelableArray("feedback");
        Feedback[] feedback = new Feedback[parcelable.length];
        System.arraycopy(parcelable, 0, feedback, 0, parcelable.length);
        //Log.i("TEST ", feedback[0].question);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        feedbackScrollViewLayout = (LinearLayout) findViewById(R.id.feedbackScrollViewLayout);
        feedbackAdapter = new FeedbackAdapter(this, feedback);

        feedbackScrollViewLayout.addView(feedbackAdapter.getView(pos, null, feedbackScrollViewLayout));
        previousButton.setVisibility(View.GONE);

        progressText.setText(String.format("%d of %d", (pos + 1), feedbackAdapter.getCount()));
        hashMap = new HashMap<>(feedback.length);
        optionsRadioGroup = (RadioGroup) findViewById(R.id.optionsRadioGroup);
        optionsRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

//        mainScrollView.smoothScrollTo(0, 0);
//        mainScrollView.pageScroll(View.FOCUS_UP);
//
//        // Wait until my scrollView is ready
//        mainScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Ready, move up
//                mainScrollView.fullScroll(View.FOCUS_UP);
//                mainScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });

        switch (v.getId()) {
            case R.id.nextButton:
                nextQuestion();
                break;
            case R.id.previousButton:
                previousQuestion();
                break;
        }
    }


    private void nextQuestion() {
        if (flag) {
            pos++;
            previousButton.setVisibility(View.VISIBLE);
            progressText.setText(String.format("%d / %d", (pos + 1), feedbackAdapter.getCount()));
            if (pos >= feedbackAdapter.getCount()) {
                progressText.setText(String.format("%d / %d", feedbackAdapter.getCount(), feedbackAdapter.getCount()));


                final Map<String, String> sortedMap = new TreeMap<>(hashMap);

                //Toast.makeText(this, "Map is: " + sortedMap, Toast.LENGTH_LONG).show();
                pos = feedbackAdapter.getCount() - 1;

                // Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.toast_thankyou), Toast.LENGTH_SHORT).show();

                //finish();
                Log.d("TEST","Score.."  + score);
                Log.d("TEST","Question Count" +feedbackAdapter.getCount());
                if (score >= feedbackAdapter.getCount() * 3) {
                    sortedMap.put("feedback", "No comments");
                    sortedMap.put("card_no", String.valueOf(CARD_NO));
                    sortedMap.put("score", Integer.toString(score));

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("TEST", "Feedback Response......." + response);
                            if (!response.contains("Error")) {
                                user.updateCredits(response);
                                Log.i("TEST", "User Class Credits:" + user.getCredits());
                                Log.i("mits", "Score inside response" + score);

                                String temp = getResources().getString(R.string.toast_thankyou);
                                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();

                            } else {
                                Log.i("TEST", "Error:" + response);
                            }
                        }
                    }
                , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Feedback Error......." + error, Toast.LENGTH_LONG).show();
                        Log.i("TEST", "Feedback Error......." + error);

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return sortedMap;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

                Intent intent = new Intent(FeedbackActivity.this, Dashboard.class);
                intent.putExtra("credits", user.getCredits());
                startActivity(intent);
                finish();
            }
            else{
                    Intent i = new Intent(FeedbackActivity.this, FeedbackEditTextActivity.class);
                    user.storeFeedback(sortedMap);
                    i.putExtra("score",score);
                    startActivity(i);
                }
            } else {
                feedbackScrollViewLayout.removeViewAt(0);
                feedbackScrollViewLayout.addView(feedbackAdapter.getView(pos, null, feedbackScrollViewLayout));
                saveRadioState();
                if (!hashMap.containsKey(Integer.toString(pos + 1))) {
                    flag = false;
                }
            }
        } else {
            String temp = getResources().getString(R.string.toast_select_choice);
            Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
        }
    }


    private void previousQuestion() {
        pos--;

        if (pos == 0)
            previousButton.setVisibility(View.GONE);
        progressText.setText(String.format("%d / %d", (pos + 1), feedbackAdapter.getCount()));
        if (pos < 0) {
            progressText.setText(String.format("%d / %d", pos + 1, feedbackAdapter.getCount()));
            pos = 0;
        } else {
            feedbackScrollViewLayout.removeViewAt(0);
            feedbackScrollViewLayout.addView(feedbackAdapter.getView(pos, null, feedbackScrollViewLayout));

            saveRadioState();
            flag = true;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        flag = true;
        switch (checkedId) {
            case R.id.option1:
                hashMap.put(Integer.toString(pos + 1), "1");
                score += 5;
                break;
            case R.id.option2:
                hashMap.put(Integer.toString(pos + 1), "2");
                score += 4;
                break;
            case R.id.option3:
                hashMap.put(Integer.toString(pos + 1), "3");
                score += 2;
                break;
            case R.id.option4:
                hashMap.put(Integer.toString(pos + 1), "4");
                score += 1;
                break;
            case R.id.option5:
                hashMap.put(Integer.toString(pos + 1), "5");
                score += 3;
                break;
        }
    }

    private void saveRadioState() {
        //loads saved value of radio button and makes it accept change in radio value
        optionsRadioGroup = (RadioGroup) findViewById(R.id.optionsRadioGroup);

        String val = hashMap.get(Integer.toString(pos + 1));
        if (val != null) {
            if (Integer.parseInt(val) == 1) {
                score -= 5;
                optionsRadioGroup.check(R.id.option1);
            } else if (Integer.parseInt(val) == 2) {
                score -= 4;
                optionsRadioGroup.check(R.id.option2);
            } else if (Integer.parseInt(val) == 3) {
                score -= 2;
                optionsRadioGroup.check(R.id.option3);
            } else if (Integer.parseInt(val) == 4) {
                score -= 1;
                optionsRadioGroup.check(R.id.option4);
            } else {
                score -= 3;
                optionsRadioGroup.check(R.id.option5);
            }
        }
        optionsRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        pos--;
        if (pos < 0) {
            finish();
        } else if (pos >= 0) {
            if (pos == 0)
                previousButton.setVisibility(View.GONE);

            progressText.setText(String.format("%d / %d", (pos + 1), feedbackAdapter.getCount()));
            feedbackScrollViewLayout.removeViewAt(0);
            feedbackScrollViewLayout.addView(feedbackAdapter.getView(pos, null, feedbackScrollViewLayout));
            saveRadioState();
            flag = true;
        }
    }
}