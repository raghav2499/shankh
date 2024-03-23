package com.darzee.shankh.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.*;

@Service
public class FilterOrderService {

    public Pageable getPagingCriteria(Map<String, Object> paramMap) {
        Sort sortCriteria = sortIfNeeded(paramMap);
        Integer size = Integer.valueOf(paramMap.get(PARAMS_MAP_COUNT_PER_PAGE_KEY).toString());
        Integer page = Integer.valueOf((String) Optional.ofNullable(paramMap.get(PARAMS_MAP_PAGE_COUNT_KEY)).orElse(0).toString());
        if (sortCriteria == null) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, sortCriteria);
    }

    public Sort sortIfNeeded(Map<String, Object> paramMap) {
        if (paramMap.get(PARAMS_MAP_SORT_KEY) != null) {
            String sortOnParam = (String) paramMap.get(PARAMS_MAP_SORT_KEY);
            String sortOrder = (String) paramMap.get(PARAMS_MAP_SORT_ORDER);
            Direction sortDirection = (sortOrder == "desc") ? Direction.DESC : Direction.ASC;
            return Sort.by(sortDirection, sortOnParam);
        }
        return null;
    }

}
