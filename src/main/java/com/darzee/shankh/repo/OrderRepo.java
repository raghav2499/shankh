package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query(value = "SELECT distinct(ord_amt.id), ord_amt.total_amount, DATE_TRUNC('day', ord.created_at)  " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND (ord_ite.is_deleted != true or ord_ite.is_deleted is null) " +
            "AND (ord.is_deleted != true or ord.is_deleted is null) " +
            "AND ord.order_status != 0 " +
            "AND ord_ite.order_item_status != 6 ", nativeQuery = true)
    List<Object[]> getOrderAmountsBetweenTheDates(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query(value = "SELECT DISTINCT ord_amt.id, ord_amt.total_amount, ord_ite.order_type " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND (ord_ite.is_deleted != true OR ord_ite.is_deleted IS NULL) " +
            "AND (ord.is_deleted != true OR ord.is_deleted IS NULL) " +
            "AND ord.order_status != 0 " +
            "AND ord_ite.order_item_status != 6", nativeQuery = true)
    List<Object[]> getOrderTypeBasedOrderAmountData(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DISTINCT ord_amt.id, ord_amt.total_amount, ord.customer_id AS customerId " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND ord.is_deleted != true " +
            "AND (ord_ite.is_deleted != true or ord_ite.is_deleted is null) " +
            "AND (ord.is_deleted != true or ord.is_deleted is null) " +
            "AND ord_ite.order_item_status != 6 ", nativeQuery = true)
    List<Object[]> getTopCustomersByTotalAmount(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(oi.id) FROM OrderItem oi INNER JOIN oi.order o " +
            "WHERE o.boutique.id = :boutiqueId AND o.orderStatus in (1,2) " +
            "AND (oi.isDeleted != true or oi.isDeleted is null) " +
            "AND (o.isDeleted != true or o.isDeleted is null) " +
            "AND oi.createdAt >= :startDate AND oi.createdAt < :endDate")
    Integer getNewItemsCount(@Param("boutiqueId") Long boutiqueId,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(oi.id) FROM OrderItem oi INNER JOIN oi.order o " +
            "WHERE o.boutique.id = :boutiqueId AND oi.orderItemStatus = 4 " +
            "AND (oi.isDeleted != true or oi.isDeleted is null) " +
            "AND (o.isDeleted != true or o.isDeleted is null) " +
            "AND oi.updatedAt >= :startDate " +
            "AND oi.updatedAt < :endDate")
    Integer getCompletedItemsCount(@Param("boutiqueId") Long boutiqueId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);


    List<Order> findAllByCustomerId(Long customerId);

    Optional<Order> findByBoutiqueOrderIdAndBoutiqueId(Long boutiqueOrderId, Long boutiqueId);

    Integer countByBoutiqueId(Long boutiqueId);
}
