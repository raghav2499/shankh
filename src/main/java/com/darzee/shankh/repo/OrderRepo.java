package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import com.darzee.shankh.enums.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query(value = "SELECT SUM(distinct(ord_amt.total_amount)) AS totalAmount, DATE_TRUNC('week', ord.created_at) AS week " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND (ord_ite.is_deleted != true or ord_ite.is_deleted is null) " +
            "AND (ord.is_deleted != true or ord.is_deleted is null) " +
            "AND ord.order_status != 0 " +
            "AND ord_ite.order_item_status != 6 " +
            "GROUP BY DATE_TRUNC('week', ord.created_at) ORDER BY DATE_TRUNC('week', ord.created_at)", nativeQuery = true)
    List<Object[]> getTotalAmountByWeek(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT SUM(distinct(ord_amt.total_amount)) AS totalAmount, ord_ite.order_type AS orderType " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND (ord_ite.is_deleted != true or ord_ite.is_deleted is null) " +
            "AND (ord.is_deleted != true or ord.is_deleted is null) " +
            "AND ord.order_status != 0 " +
            "AND ord_ite.order_item_status != 6 " +
            "GROUP BY ord_ite.order_type", nativeQuery = true)
    List<Object[]> getTotalAmountByOrderType(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT SUM(distinct(ord_amt.total_amount)) AS totalAmount, ord.customer_id AS customerId " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.order_amount_id = ord_amt.id " +
            "INNER JOIN order_item ord_ite ON ord.id = ord_ite.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "AND ord.is_deleted != true " +
            "AND (ord_ite.is_deleted != true or ord_ite.is_deleted is null) " +
            "AND (ord.is_deleted != true or ord.is_deleted is null) " +
            "AND ord_ite.order_item_status != 6 " +
            "GROUP BY ord.customer_id " +
            "ORDER BY totalAmount DESC " +
            "LIMIT 2", nativeQuery = true)
    List<Object[]> getTopCustomersByTotalAmount(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT COUNT(oi.id) FROM OrderItem oi INNER JOIN oi.order o " +
            "WHERE o.boutique.id = :boutiqueId AND o.orderStatus = 1 " +
            "AND (oi.isDeleted != true or oi.isDeleted is null) " +
            "AND (o.isDeleted != true or o.isDeleted is null) " +
            "AND oi.createdAt >= :startDate AND oi.createdAt < :endDate")
    Integer getNewItemsCount(@Param("boutiqueId") Long boutiqueId,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT COUNT(oi.id) FROM OrderItem oi INNER JOIN oi.order o " +
            "WHERE o.boutique.id = :boutiqueId AND o.orderStatus = 2 " +
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

    List<Order> findByOrderStatusNot(OrderStatus orderStatus);
}
