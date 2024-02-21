package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Boutique;
import com.darzee.shankh.entity.Order;
import com.darzee.shankh.entity.OrderItem;
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

public class OrderItemSpecificationClause {

    public static Specification<OrderItem> findOrderItemByStatus(List<Integer> value) {
        return (root, cq, cb) -> root.get("orderItemStatus").in(value);
    }

    public static Specification<OrderItem> findOrderItemByBoutiqueId(Long boutiqueId) {
        return (root, cq, cb) -> {
            Join<OrderItem, Boutique> boutique = root.join("order").join("boutique");
            return cb.equal(boutique.get("id"), boutiqueId);
        };
    }


    public static Specification<OrderItem> findPriorityOrderItem(Boolean value) {
        return (root, cq, cb) -> cb.equal(root.get("isPriorityOrder"), value);
    }

    public static Specification<OrderItem> findOrderItemsByOrderId(Long value) {
        return (root, cq, cb) -> {
            Join<OrderItem, Order> order = root.join("order");
            return cb.equal(order.get("id"), value);
        };
    }

    public static Specification<OrderItem> findOrderItemsByDeliveryDateFrom(LocalDateTime value) {
        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("deliveryDate"), value);
    }

    public static Specification<OrderItem> findOrderItemsByDeliveryDateTill(LocalDateTime value) {
        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("deliveryDate"), value);
    }


    public static Specification<OrderItem> findNonDeletedOrderItems() {
        return (root, cq, cb) -> cb.or(
                cb.notEqual(root.get("isDeleted"), true),
                cb.isNull(root.get("isDeleted")));
    }
    private static Specification<OrderItem> getAppropriatePredicate(String key, Object value) {
        OrderFilter filter = OrderFilter.getFilter(key);
        if (filter == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter found in request");
        }
        switch (filter) {
            case ITEM_STATUS:
                return findOrderItemByStatus((List<Integer>) value);
            case BOUTIQUE_ID:
                return findOrderItemByBoutiqueId((Long) value);
            case PRIORITY_ORDERS_ONLY:
                return findPriorityOrderItem((Boolean) value);
            case ORDER_ID:
                return findOrderItemsByOrderId((Long) value);
            case DELIVERY_DATE_FROM:
                return findOrderItemsByDeliveryDateFrom((LocalDateTime) value);
            case DELIVERY_DATE_TILL:
                return findOrderItemsByDeliveryDateTill((LocalDateTime) value);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "We've not started grouping on " + filter);
        }
    }

    public static Specification<OrderItem> getSpecificationBasedOnFilters(Map<String, Object> paramsMap) {
        return (root, cq, cb) -> {

            List<Predicate> predicateList = new ArrayList<>();

            paramsMap.forEach((key, value) -> {
                Specification<OrderItem> specification = getAppropriatePredicate(key, value);
                if (specification != null) {
                    predicateList.add(specification.toPredicate(root, cq, cb));
                }
            });
            Specification<OrderItem> defaultSpecification = findNonDeletedOrderItems();
            predicateList.add(defaultSpecification.toPredicate(root, cq, cb));
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
