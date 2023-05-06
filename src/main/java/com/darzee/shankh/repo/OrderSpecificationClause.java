package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Customer;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderSpecificationClause {

    public static Specification<Order> findOrderByStatuses(List<Integer> statusList) {
        return (root, cq, cb) -> root.get("orderStatus").in(statusList);
    }

    public static Specification<Order> findOrderByBoutiqueId(Long boutiqueId) {
        return (root, cq, cb) -> {
            Join<Order, Boutique> boutique = root.join("boutique");
            return cb.equal(boutique.get("id"), boutiqueId);
        };
    }

    public static Specification<Order> findPriorityOrders(Boolean fetchPriorityOrders) {
        return (root, cq, cb) -> cb.equal(root.get("isPriorityOrder"), fetchPriorityOrders);
    }

    public static Specification<Order> findCustomerOrders(Long customerId) {
        return (root, cq, cb) -> {
            Join<Order, Customer> customer = root.join("customer");
            return cb.equal(customer.get("id"), customerId);
        };
    }

    public static Specification<Order> findOrdersByDeliveryDateFrom(LocalDateTime deliveryDateFrom) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("deliveryDate"), deliveryDateFrom);
    }

    public static Specification<Order> findOrdersByDeliveryDateTill(LocalDateTime deliveryDateTill) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("deliveryDate"), deliveryDateTill);
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
            case PRIORITY_ORDERS_ONLY:
                return findPriorityOrders((Boolean) value);
            case CUSTOMER_ID:
                return findCustomerOrders((Long) value);
            case DELIVERY_DATE_FROM:
                return findOrdersByDeliveryDateFrom((LocalDateTime) value);
            case DELIVERY_DATE_TILL:
                return findOrdersByDeliveryDateTill((LocalDateTime) value);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "We've not started grouping on " + filter);
        }

    }
}
