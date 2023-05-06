package com.darzee.shankh.request;

import com.darzee.shankh.enums.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.Constants.*;
import static com.darzee.shankh.enums.OrderFilter.*;
import static com.darzee.shankh.enums.OrderSort.DELIVERY_DATE;
import static com.darzee.shankh.enums.OrderSort.TRIAL_DATE;

public class GetOrderDetailsRequest {

    public static Map<String, Object> getPagingAndSortCriteria(String sortKey, Integer count, Integer offset) {
        Map<String, Object> finalParamsMap = new HashMap<>();
        setSortingCriteria(finalParamsMap, sortKey);
        setPagingCriteria(finalParamsMap, count, offset);
        return finalParamsMap;
    }


    /**
     * Status ordinal mapping saved in DB, and the one known to clients is different. So status enum is judged from
     * the one in request, and then is mapped to corresponding DB ordinal
     *
     * @return
     */
    public static Map<String, Object> getFilterMap(Long boutiqueId, String statuses, Boolean priorityOrdersOnly,
                                                   Long customerId, String deliveryDateFrom, String deliveryDateTill) {
        Map<String, Object> filterMap = new HashMap<>();
        if (boutiqueId != null) {
            filterMap.put(BOUTIQUE_ID.getFilterName(), boutiqueId);
        }
        if (statuses != null) {
            List<Integer> statusList = Arrays.asList(statuses.trim()
                            .split(","))
                    .stream()
                    .map(Integer::parseInt)
                    .map(requestOrdinal -> OrderStatus.getOrderTypeEnumOrdinalMap().get(requestOrdinal))
                    .map(orderStatus -> orderStatus.ordinal())
                    .collect(Collectors.toList());
            filterMap.put(STATUS.getFilterName(), statusList);
        }
        if (priorityOrdersOnly != null) {
            filterMap.put(PRIORITY_ORDERS_ONLY.getFilterName(), priorityOrdersOnly);
        }
        if (customerId != null) {
            filterMap.put(CUSTOMER_ID.getFilterName(), customerId);
        }
        if (deliveryDateFrom != null) {
            filterMap.put(DELIVERY_DATE_FROM.getFilterName(), LocalDateTime.parse(deliveryDateFrom));
        }
        if (deliveryDateTill != null) {
            filterMap.put(DELIVERY_DATE_TILL.getFilterName(), LocalDateTime.parse(deliveryDateTill));
        }
        return filterMap;
    }

    private static void setSortingCriteria(Map<String, Object> paramsMap, String requestSortKey) {
        String parasmMapSortKey = PARAMS_MAP_SORT_KEY;
        String paramsSortValue = "";
        switch (requestSortKey) {
            case "trial_date":
                paramsSortValue = TRIAL_DATE.getOrderSortString();
                break;
            case "delivery_date":
                paramsSortValue = DELIVERY_DATE.getOrderSortString();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorting is not supported for " + requestSortKey);
        }
        paramsMap.put(parasmMapSortKey, paramsSortValue);
    }

    private static void setPagingCriteria(Map<String, Object> paramsMap, Integer count, Integer offset) {
        paramsMap.put(PARAMS_MAP_COUNT_KEY, count);
        paramsMap.put(PARAMS_MAP_OFFSET_KEY, offset);
    }
}
