package com.darzee.shankh.service;

import com.darzee.shankh.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderStateMachineService {

    Map<OrderStatus, List<OrderStatus>> validStateTransitions = new HashMap<OrderStatus, List<OrderStatus>>() {{
        put(OrderStatus.STITCHING_NOT_STARTED, Arrays.asList(OrderStatus.STITCHING_IN_PROGRESS, OrderStatus.ORDER_READY_FOR_TRIAL, OrderStatus.ORDER_COMPLETED));
        put(OrderStatus.STITCHING_IN_PROGRESS, Arrays.asList(OrderStatus.STITCHING_NOT_STARTED, OrderStatus.ORDER_READY_FOR_TRIAL, OrderStatus.ORDER_COMPLETED));
        put(OrderStatus.ORDER_READY_FOR_TRIAL, Arrays.asList(OrderStatus.STITCHING_NOT_STARTED, OrderStatus.STITCHING_IN_PROGRESS, OrderStatus.ORDER_COMPLETED));
        put(OrderStatus.ORDER_COMPLETED, Arrays.asList(OrderStatus.STITCHING_NOT_STARTED, OrderStatus.STITCHING_IN_PROGRESS, OrderStatus.ORDER_READY_FOR_TRIAL, OrderStatus.ORDER_DELIVERED));
        put(OrderStatus.ORDER_DELIVERED, Arrays.asList(OrderStatus.STITCHING_NOT_STARTED, OrderStatus.STITCHING_IN_PROGRESS, OrderStatus.ORDER_READY_FOR_TRIAL, OrderStatus.ORDER_COMPLETED));
    }};

    public boolean isTransitionAllowed(OrderStatus fromState, OrderStatus toState) {
        if (validStateTransitions.containsKey(fromState) && validStateTransitions.get(fromState).contains(toState)) {
            return true;
        }
        return false;
    }
}
