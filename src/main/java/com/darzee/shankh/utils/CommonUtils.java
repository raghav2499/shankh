package com.darzee.shankh.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public static String constructName(String firstName, String lastName) {
        return (Optional.ofNullable(firstName).orElse("") + " " + Optional.ofNullable(lastName).orElse("")).trim();
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
}
