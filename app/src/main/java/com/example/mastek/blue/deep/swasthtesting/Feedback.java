package com.example.mastek.blue.deep.swasthtesting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mitali on 24-03-2016.
 */
public class Feedback implements Parcelable {
    public final String question;
    public final String choice1;
    public final String choice2;
    public final String choice3;
    public final String choice4;
    public final String choice5;


    public Feedback(String question, String choice1, String choice2, String choice3, String choice4, String choice5) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.choice1);
        dest.writeString(this.choice2);
        dest.writeString(this.choice3);
        dest.writeString(this.choice4);
        dest.writeString(this.choice5);
    }

    protected Feedback(Parcel in) {
        this.question = in.readString();
        this.choice1 = in.readString();
        this.choice2 = in.readString();
        this.choice3 = in.readString();
        this.choice4 = in.readString();
        this.choice5 = in.readString();
    }

    public static final Parcelable.Creator<Feedback> CREATOR = new Parcelable.Creator<Feedback>() {
        public Feedback createFromParcel(Parcel source) {
            return new Feedback(source);
        }

        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };
}
