package com.darzee.shankh.request;

import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.darzee.shankh.constants.Constants.*;
import static com.darzee.shankh.enums.OrderFilter.*;
import static com.darzee.shankh.enums.OrderItemSort.*;

public class GetOrderDetailsRequest {

    public static Map<String, Object> getPagingCriteria(Integer countPerPage, Integer pageCount, String sortKey,
                                                        String sortOrder) {
        Map<String, Object> finalParamsMap = new HashMap<>();
        pageCount = pageCount - 1; //pageCount is 0 indexed
        if (sortKey != null) {
            setSortingCriteria(finalParamsMap, sortKey, sortOrder);
        }
        setPagingCriteria(finalParamsMap, countPerPage, pageCount);
        return finalParamsMap;
    }


    /**
     * Status ordinal mapping saved in DB, and the one known to clients is different. So status enum is judged from
     * the one in request, and then is mapped to corresponding DB ordinal
     *
     * @return
     */
    public static Map<String, Object> getFilterMap(Long boutiqueId, String itemStatuses, String orderStatusList,
                                                   Boolean priorityOrdersOnly, Long customerId,
                                                   String deliveryDateFrom, String deliveryDateTill,
                                                   Long orderId, Boolean paymentDue) {
        Map<String, Object> filterMap = new HashMap<>();
        if (boutiqueId != null) {
            filterMap.put(BOUTIQUE_ID.getFilterName(), boutiqueId);
        }
        if (itemStatuses != null) {
            List<Integer> statusList = Arrays.asList(itemStatuses.trim()
                            .split(","))
                    .stream()
                    .map(Integer::parseInt)
                    .map(requestOrdinal -> OrderItemStatus.getOrderItemStatusEnumOrdinalMap().get(requestOrdinal))
                    .map(orderItemStatus -> orderItemStatus.ordinal())
                    .collect(Collectors.toList());
            filterMap.put(ITEM_STATUS.getFilterName(), statusList);
        }
        if (orderStatusList != null) {
            List<Integer> statusList = Arrays.asList(orderStatusList.trim()
                            .split(","))
                    .stream()
                    .map(Integer::parseInt)
                    .map(requestOrdinal -> OrderStatus.getOrderStatusEnumOrdinalMap().get(requestOrdinal))
                    .map(orderStatus -> orderStatus.ordinal())
                    .collect(Collectors.toList());
            filterMap.put(ORDER_STATUS.getFilterName(), statusList);
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
        if (orderId != null) {
            filterMap.put(ORDER_ID.getFilterName(), orderId);
        }
        if (Boolean.TRUE.equals(paymentDue)) {
            filterMap.put(PAYMENT_DUE.getFilterName(), Boolean.TRUE);
        }
        return filterMap;
    }

    private static void setSortingCriteria(Map<String, Object> paramsMap, String requestSortKey, String sortOrder) {
        String parasmMapSortKey = PARAMS_MAP_SORT_KEY;
        String paramsMapSortOrder = PARAMS_MAP_SORT_ORDER;

        String paramsSortValue = "";
        switch (requestSortKey) {
            case "trial_date":
                paramsSortValue = TRIAL_DATE.getOrderSortString();
                break;
            case "delivery_date":
                paramsSortValue = DELIVERY_DATE.getOrderSortString();
                break;
            case "created_at":
                paramsSortValue = CREATED_AT.getOrderSortString();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorting is not supported for " + requestSortKey);
        }
        paramsMap.put(parasmMapSortKey, paramsSortValue);
        paramsMap.put(paramsMapSortOrder, sortOrder);
    }

    private static void setPagingCriteria(Map<String, Object> paramsMap, Integer countPerPage, Integer pageCount) {
        paramsMap.put(PARAMS_MAP_COUNT_PER_PAGE_KEY, countPerPage);
        paramsMap.put(PARAMS_MAP_PAGE_COUNT_KEY, pageCount);
    }
}
