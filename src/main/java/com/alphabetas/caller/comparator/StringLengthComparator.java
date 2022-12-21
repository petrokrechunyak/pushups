package com.alphabetas.caller.comparator;

import com.alphabetas.caller.model.CallerName;

import java.util.Comparator;

public class StringLengthComparator implements Comparator<CallerName> {


    @Override
    public int compare(CallerName callerName, CallerName t1) {
        String s1 = callerName.getName();
        String s2 = t1.getName();
        if(s1.length() != s2.length())
            return s2.length() - s1.length();
        return s1.compareTo(s2);
    }
}
