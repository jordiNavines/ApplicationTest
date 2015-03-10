package com.jordinavines.applicationtest.common;

import com.jordinavines.applicationtest.model.User;

import java.util.Comparator;

/**
 * Created by jordinavines on 10/03/2015.
 */
public class DepartmentComparator implements Comparator<User> {
    public int compare(User userA, User userB) {
        return userA.getDepartment().compareToIgnoreCase(userB.getDepartment());
    }
}
