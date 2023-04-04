package com.darzee.shankh.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
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
        if (value == null) {
            return "";
        }
        return StringUtils.truncate(value.toString(), MEASUREMENT_RESPONSE_TRUNCATION_LENGTH);
    }

}
