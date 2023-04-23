package com.darzee.shankh.service;

import com.darzee.shankh.enums.OrderSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.*;
import static com.darzee.shankh.enums.OrderSort.DELIVERY_DATE;
import static com.darzee.shankh.enums.OrderSort.TRIAL_DATE;

@Service
public class FilterOrderService {

    public Pageable getPagingCriteria(Map<String, Object> paramMap) {
        Sort sortCriteria = sortIfNeeded(paramMap);
        Integer size = Integer.valueOf(paramMap.get(PARAMS_MAP_COUNT_KEY).toString());
        Integer offset = Integer.valueOf((String) Optional.ofNullable(paramMap.get(PARAMS_MAP_OFFSET_KEY)).orElse(0).toString());
        if (sortCriteria == null) {
            return PageRequest.of(offset, size);
        }
        return PageRequest.of(offset, size, sortCriteria);
    }

    public Sort sortIfNeeded(Map<String, Object> paramMap) {
        if(paramMap.get(PARAMS_MAP_SORT_KEY) != null) {
            String sortOnParam = (String) paramMap.get("sort_on");
            OrderSort orderSort = OrderSort.getEnumMap().get(sortOnParam);
            switch(orderSort) {
                case TRIAL_DATE:
                    return Sort.by(TRIAL_DATE.name());
                case DELIVERY_DATE:
                    return Sort.by(DELIVERY_DATE.name());
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort by is not supported on " + orderSort);
            }
        }
        return null;
    }

}
