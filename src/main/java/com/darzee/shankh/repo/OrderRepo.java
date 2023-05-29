package com.darzee.shankh.repo;

import com.darzee.shankh.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query(value = "SELECT SUM(ord_amt.total_amount) AS totalAmount, DATE_TRUNC('week', ord.created_at) AS week " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.id = ord_amt.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "GROUP BY DATE_TRUNC('week', ord.created_at)", nativeQuery = true)
    List<Object[]> getTotalAmountByWeek(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT SUM(ord_amt.total_amount) AS totalAmount, ord.order_type AS orderType " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.id = ord_amt.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "GROUP BY ord.order_type", nativeQuery = true)
    List<Object[]> getTotalAmountByOrderType(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT SUM(ord_amt.total_amount) AS totalAmount, ord.customer_id AS customerId " +
            "FROM orders ord " +
            "INNER JOIN order_amount ord_amt ON ord.id = ord_amt.order_id " +
            "WHERE ord.boutique_id = :boutiqueId " +
            "AND ord.created_at >= :startDate " +
            "AND ord.created_at < :endDate " +
            "GROUP BY ord.customer_id " +
            "ORDER BY totalAmount DESC " +
            "LIMIT 2", nativeQuery = true)
    List<Object[]> getTopCustomersByTotalAmount(
            @Param("boutiqueId") Long boutiqueId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
