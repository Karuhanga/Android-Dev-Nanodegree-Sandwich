package com.udacity.sandwichclub.utils;

import java.util.List;

public class StringUtils {
    public static String stringify(List<String> stringList) {
        StringBuilder result = new StringBuilder();
        for (String s : stringList) {
            result.append("\n").append(s);
        }
        return result.toString().replaceFirst("\n", "");
    }
}
