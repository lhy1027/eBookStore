package com.ebookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebookstore.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("SELECT DATE(create_time) AS date, SUM(total_amount) AS amount, COUNT(*) AS orders " +
            "FROM orders WHERE status IN ('待发货', '已发货', '已签收') " +
            "AND create_time >= #{startDate} AND create_time < DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> selectDailySales(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m') AS month, SUM(total_amount) AS amount, COUNT(*) AS orders " +
            "FROM orders WHERE status IN ('待发货', '已发货', '已签收') " +
            "AND create_time >= #{startDate} AND create_time < DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month")
    List<Map<String, Object>> selectMonthlySales(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status IN ('待发货', '已发货', '已签收') " +
            "AND create_time >= #{startDate} AND create_time < DATE_ADD(#{endDate}, INTERVAL 1 DAY)")
    BigDecimal selectTotalAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT COUNT(*) FROM orders WHERE status IN ('待发货', '已发货', '已签收') " +
            "AND create_time >= #{startDate} AND create_time < DATE_ADD(#{endDate}, INTERVAL 1 DAY)")
    Long selectTotalOrders(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT COALESCE(SUM(oi.quantity), 0) FROM order_item oi " +
            "INNER JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.status IN ('待发货', '已发货', '已签收') " +
            "AND o.create_time >= #{startDate} AND o.create_time < DATE_ADD(#{endDate}, INTERVAL 1 DAY)")
    Long selectTotalBooksSold(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT b.id, b.name, SUM(oi.quantity) AS sales FROM order_item oi " +
            "INNER JOIN orders o ON oi.order_id = o.id " +
            "INNER JOIN book b ON oi.book_id = b.id " +
            "WHERE o.status IN ('待发货', '已发货', '已签收') " +
            "GROUP BY b.id, b.name ORDER BY sales DESC LIMIT 10")
    List<Map<String, Object>> selectHotBooks();

    @Select("SELECT c.name, COALESCE(SUM(oi.quantity), 0) AS value FROM category c " +
            "LEFT JOIN book b ON b.category_id = c.id " +
            "LEFT JOIN order_item oi ON oi.book_id = b.id " +
            "LEFT JOIN orders o ON oi.order_id = o.id AND o.status IN ('待发货', '已发货', '已签收') " +
            "GROUP BY c.id, c.name ORDER BY value DESC")
    List<Map<String, Object>> selectCategorySales();
}