package com.darzee.shankh.constants;

import com.darzee.shankh.enums.OrderStatus;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final List<OrderStatus> activeOrderStatusList = Arrays.asList(OrderStatus.STITCHING_NOT_STARTED,
            OrderStatus.STITCHING_IN_PROGRESS,
            OrderStatus.ORDER_READY_FOR_TRIAL);

    public static final List<OrderStatus> closedOrderStatusList = Arrays.asList(OrderStatus.ORDER_COMPLETED);
    public static final Double CM_TO_INCH_FACTOR = 2.54;
    public static final Integer MEASUREMENT_RESPONSE_TRUNCATION_LENGTH = 4;
}
