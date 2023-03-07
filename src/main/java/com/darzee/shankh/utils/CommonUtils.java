package com.darzee.shankh.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommonUtils {

    public static <S, T> List<T> mapList(List<S> source, Function<S, T> function) {
        List<T> returnList = new ArrayList<>();
        for(S sourceObj : source) {
            returnList.add(function.apply(sourceObj));
        }
        return returnList;
    }

    public static String doubleToString(Double value) {
        if(value == null) {
            return "";
        }
        return value.toString();
    }

}
