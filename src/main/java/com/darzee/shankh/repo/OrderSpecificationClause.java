package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderSpecificationClause {

    public static Specification<Order> findOrderByStatuses(List<Integer> statusList) {

        return (root, cq, cb) -> root.get("status").in(statusList);
    }

    public static Specification<Order> findOrderByBoutiqueId(Long boutiqueId) {

        return (root, cq, cb) -> root.get("boutique_id").in(boutiqueId);
    }

    public static Specification<Order> findPriorityOrders(Boolean fetchPriorityOrders) {

        return (root, cq, cb) -> root.get("is_priority_order").in(fetchPriorityOrders);
    }

    public static Specification<Order> getSpecificationBasedOnFilters(Map<String, Object> paramsMap) {

        return (root, cq, cb) -> {

            List<Predicate> predicateList = new ArrayList<>();

            paramsMap.forEach((key, value) -> {
                Specification<Order> specification = getAppropriatePredicate(key, value);
                if (specification != null) {
                    predicateList.add(specification.toPredicate(root, cq, cb));
                }
            });

            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private static Specification<Order> getAppropriatePredicate(String key, Object value) {

        OrderFilter filter = OrderFilter.getFilter(key);

        if (filter == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter found in request");
        }

        switch (filter) {
            case STATUS:
                return findOrderByStatuses((List<Integer>) value);
            case BOUTIQUE_ID:
                return findOrderByBoutiqueId((Long) value);
            case URGENT:
                return findPriorityOrders((Boolean) value);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "We've not started grouping on " + filter);
        }

    }
}
