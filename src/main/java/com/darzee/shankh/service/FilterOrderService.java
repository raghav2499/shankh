package com.darzee.shankh.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.darzee.shankh.constants.Constants.*;

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
        if (paramMap.get(PARAMS_MAP_SORT_KEY) != null) {
            String sortOnParam = (String) paramMap.get(PARAMS_MAP_SORT_KEY);
            return Sort.by(sortOnParam);
        }
        return null;
    }

}
