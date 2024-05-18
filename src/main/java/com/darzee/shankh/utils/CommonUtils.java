package com.darzee.shankh.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommonUtils {

    public static <S, T> List<T> mapList(List<S> source, Function<S, T> function) {
        List<T> returnList = new ArrayList<>();
        for (S sourceObj : source) {
            returnList.add(function.apply(sourceObj));
        }
        return returnList;
    }

    public static String doubleToString(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (value == null) {
            return "";
        }
        return df.format(value);
    }

    public static String sanitisePhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            return phoneNumber;
        }
        String sanitisedString = Stream.of(phoneNumber)
                .map(s -> s.replaceAll("^\\+91|^0", "")) // Remove leading +91 or 0
                .findFirst()
                .orElse("");
        return sanitisedString;
    }

    public static Integer stringToInt(String str) {
        Integer result = null;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return result;
        }
        return result;
    }

    public static Map<String, String> splitUsername(String username) {
    String[] parts = username.split("\\+"); // assuming the country code is prefixed with '+'
    String phoneNumber = parts[0];
    String countryCode = "+" + parts[1];

    Map<String, String> result = new HashMap<>();
    result.put("phoneNumber", phoneNumber);
    result.put("countryCode", countryCode);

    return result;
    }
}
