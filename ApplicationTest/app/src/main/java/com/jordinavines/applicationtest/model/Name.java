package com.jordinavines.applicationtest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class Name implements Parcelable{

    String first;

    String last;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return first + " " + last;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(first);
        out.writeString(last);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Name> CREATOR = new Parcelable.Creator<Name>() {
        public Name createFromParcel(Parcel in) {
            return new Name(in);
        }

        public Name[] newArray(int size) {
            return new Name[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Name(Parcel in) {
        first= in.readString();
        last= in.readString();

    }
}
