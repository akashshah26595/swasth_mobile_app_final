package com.example.mastek.blue.deep.swasthtesting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akash on 21/3/16.
 */
public class User {
    public static final String SP_NAME = "userDetails";
    SharedPreferences sharedPreferences;
    private String credits;
    //    private int credits;
    private int card_number;
    private String name;

    public User(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
    }

    public void addUserDetails(String credits, int card_number, String name) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString("credits", credits);
//        spEditor.putInt("credits", credits);
        spEditor.putInt("card_number", card_number);
        spEditor.putString("name", name);
        spEditor.apply();
    }

    public String getCredits() {
        credits = sharedPreferences.getString("credits", "");
        return credits;
    }

    public int getCardNumber() {
        card_number = sharedPreferences.getInt("card_number", 0);
        return card_number;
    }

    public String getName() {
        name = sharedPreferences.getString("name", "");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateCredits(String amount) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.remove("credits");
        spEditor.apply();
        //credits = credits + 5;
        spEditor.putString("credits", amount);
//        spEditor.putInt("credits", amount);
        spEditor.apply();
    }
    public void storeFeedback(Map<String,String> hashMap){
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String hashMapString = gson.toJson(hashMap);
        spEditor.putString("hashString", hashMapString).apply();
    }
    public Map<String,String> getFeedback(){
        Gson gson = new Gson();
        String storedHashMapString = sharedPreferences.getString("hashString", "oopsDintWork");
        java.lang.reflect.Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);
        return testHashMap2;
    }

}
