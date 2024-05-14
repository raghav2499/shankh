package com.darzee.shankh.service;

import com.darzee.shankh.enums.OrderItemStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemStateMachineService {

    Map<OrderItemStatus, List<OrderItemStatus>> validStateTransitions = new HashMap<OrderItemStatus, List<OrderItemStatus>>() {{
        put(OrderItemStatus.DRAFTED, Arrays.asList(OrderItemStatus.ACCEPTED));
        put(OrderItemStatus.ACCEPTED, Arrays.asList(OrderItemStatus.UNDER_CUTTING, OrderItemStatus.UNDER_STITCHING, OrderItemStatus.UNDER_FINISHING, OrderItemStatus.COMPLETED));
        put(OrderItemStatus.UNDER_CUTTING, Arrays.asList(OrderItemStatus.ACCEPTED, OrderItemStatus.UNDER_STITCHING, OrderItemStatus.UNDER_FINISHING, OrderItemStatus.COMPLETED));
        put(OrderItemStatus.UNDER_STITCHING, Arrays.asList(OrderItemStatus.ACCEPTED, OrderItemStatus.UNDER_CUTTING, OrderItemStatus.UNDER_FINISHING, OrderItemStatus.COMPLETED));
        put(OrderItemStatus.UNDER_FINISHING, Arrays.asList(OrderItemStatus.ACCEPTED, OrderItemStatus.UNDER_CUTTING, OrderItemStatus.UNDER_FINISHING, OrderItemStatus.COMPLETED));
        put(OrderItemStatus.COMPLETED, Arrays.asList(OrderItemStatus.ACCEPTED, OrderItemStatus.UNDER_CUTTING, OrderItemStatus.UNDER_STITCHING, OrderItemStatus.UNDER_FINISHING, OrderItemStatus.DELIVERED));
    }};

    public boolean isTransitionAllowed(OrderItemStatus fromState, OrderItemStatus toState) {
        if (validStateTransitions.containsKey(fromState) && validStateTransitions.get(fromState).contains(toState)) {
            return true;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ooops! Invalid transition");
    }
}