package com.jordinavines.applicationtest.model;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class Name {

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
}
