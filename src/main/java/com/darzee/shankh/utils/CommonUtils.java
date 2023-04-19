package com.darzee.shankh.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.darzee.shankh.constants.Constants.MEASUREMENT_RESPONSE_TRUNCATION_LENGTH;

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
        return String.valueOf(df.format(value));
    }

    public static String constructName(String firstName, String lastName) {
        return (Optional.ofNullable(firstName).orElse("") + " " + Optional.ofNullable(lastName).orElse("")).trim();
    }

}
