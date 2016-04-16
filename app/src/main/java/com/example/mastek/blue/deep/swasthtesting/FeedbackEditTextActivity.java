package com.example.mastek.blue.deep.swasthtesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.TreeMap;

public class FeedbackEditTextActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText queryEditText;
    public static final String SERVER_ADDRESS = "http://swasth-india.esy.es/swasth/insert_mobile_feedback.php";

    //private Button cancelButton;
    private Button sendButton;
    private int score;
    User user;
    Map<String, String> sortedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = new User(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_edit_text);

        score = getIntent().getIntExtra("score", 0);
        sortedMap = new TreeMap<>(user.getFeedback());

        queryEditText = (EditText) findViewById(R.id.queryEditText);
        //cancelButton = (Button) findViewById(R.id.cancelButton);
        sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(this);
        //cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Button Clicked 0", Toast.LENGTH_LONG).show();
        switch (v.getId()) {
            case R.id.sendButton:
                sortedMap.put("card_no", String.valueOf(user.getCardNumber()));

                Toast.makeText(getApplicationContext(),"Button Clicked 1", Toast.LENGTH_LONG).show();
                String message = queryEditText.getText().toString();
                if (!message.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Button Clicked 2", Toast.LENGTH_LONG).show();
                    sortedMap.put("score",Integer.toString(score));
                    sortedMap.put("feedback",message);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_ADDRESS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           user.updateCredits(response);

                            Log.d("TEST","Edit Text Response..." + response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TEST","Error..." + error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return sortedMap;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }
                else {
                    Toast.makeText(FeedbackEditTextActivity.this, "Please enter feedback!", Toast.LENGTH_SHORT).show();
                }

                break;

        }
        Intent i = new Intent(FeedbackEditTextActivity.this,Dashboard.class);
        startActivity(i);
        finish();
    }
}
