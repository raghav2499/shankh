package com.darzee.shankh.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.Constants.*;
import static com.darzee.shankh.enums.OrderFilter.*;

public class GetOrderDetailsRequest {

    public static Map<String, Object> getParamsMap(Long boutiqueId, String statusList, Boolean urgent, String sortKey,
                                                   Integer count, Integer offset) {
        Map<String, Object> finalParamsMap = new HashMap<>();
        setFilter(finalParamsMap, boutiqueId, statusList, urgent);
        setSortingCriteria(finalParamsMap, sortKey);
        setPagingCriteria(finalParamsMap, count, offset);
        return finalParamsMap;
    }

    private static void setFilter(Map<String, Object> paramsMap, Long boutiqueId, String statuses, Boolean urgent) {
        if (boutiqueId != null) {
            paramsMap.put(BOUTIQUE_ID.getFilterName(), boutiqueId);
        }
        if (statuses != null) {
            List<Integer> statusList = Arrays.asList(statuses.trim()
                            .split(","))
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            paramsMap.put(STATUS.getFilterName(), statusList);
        }
        if (urgent != null) {
            paramsMap.put(URGENT.getFilterName(), urgent);
        }
    }

    private static void setSortingCriteria(Map<String, Object> paramsMap, String sortKey) {
        String parasmMapSortKey = PARAMS_MAP_SORT_KEY;
        paramsMap.put(parasmMapSortKey, sortKey);
    }

    private static void setPagingCriteria(Map<String, Object> paramsMap, Integer count, Integer offset) {
        paramsMap.put(PARAMS_MAP_COUNT_KEY, count);
        paramsMap.put(PARAMS_MAP_OFFSET_KEY, offset);
    }
}
