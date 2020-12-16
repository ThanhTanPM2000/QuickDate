package com.example.quickdate.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexString {

    public Boolean regexFunc(String str, String str2) {
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(str2);
        return !matcher.find();
    }

}
