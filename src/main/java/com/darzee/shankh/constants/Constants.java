package com.darzee.shankh.constants;

import com.darzee.shankh.enums.OrderItemStatus;
import com.darzee.shankh.enums.OrderStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final List<OrderStatus> ACTIVE_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.ACCEPTED);
    public static final List<OrderStatus> CLOSED_ORDER_STATUS_LIST = Arrays.asList(OrderStatus.DELIVERED);
    public static final List<OrderItemStatus> ACTIVE_ORDER_ITEM_STATUS_LIST =
            Arrays.asList(OrderItemStatus.ACCEPTED, OrderItemStatus.UNDER_CUTTING, OrderItemStatus.UNDER_STITCHING,
                    OrderItemStatus.UNDER_FINISHING, OrderItemStatus.COMPLETED);
    public static final List<OrderItemStatus> CLOSED_ORDER_ITEM_STATUS_LIST = Arrays.asList(OrderItemStatus.DELIVERED);
    public static final Double CM_TO_INCH_DIVIDING_FACTOR = 2.54;
    public static final Double INCH_TO_CM_MULTIPLYING_FACTOR = 2.54;
    public static final Double DEFAULT_DOUBLE_CM_MEASUREMENT_VALUE = 0d;

    public static final LocalDate REPORTING_START_DATE = LocalDate.of(2023, 5, 1);
    public static final String PARAMS_MAP_SORT_KEY = "sort_on";
    public static final String PARAMS_MAP_SORT_ORDER = "order";
    public static final String PARAMS_MAP_COUNT_PER_PAGE_KEY = "count_per_page";
    public static final String PARAMS_MAP_PAGE_COUNT_KEY = "page_count";

    public static final List<String> PHONE_NUMBER_POLLUTERS = Arrays.asList("+91", "0");

    public static final String PORTFOLIO_COVER_IMAGE = "portfolio_cover_image";


}
