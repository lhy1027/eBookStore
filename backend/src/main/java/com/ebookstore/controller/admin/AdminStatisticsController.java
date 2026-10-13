package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Order;
import com.ebookstore.entity.SystemConfig;
import com.ebookstore.entity.User;
import com.ebookstore.mapper.OrderMapper;
import com.ebookstore.mapper.SystemConfigMapper;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.IOrderService;
import com.ebookstore.service.interf.IUserService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    private final IBookService bookService;
    private final IOrderService orderService;
    private final IUserService userService;
    private final OrderMapper orderMapper;
    private final SystemConfigMapper configMapper;

    private int getWarningThreshold() {
        SystemConfig config = configMapper.selectOne(
                new QueryWrapper<SystemConfig>().eq("config_key", "stock_warning_threshold"));
        if (config != null) {
            try { return Integer.parseInt(config.getConfigValue()); } catch (NumberFormatException e) { }
        }
        return 5;
    }

    @GetMapping("/sales")
    public Result<Map<String, Object>> sales(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // 默认最近30天
        if (startDate == null) startDate = LocalDate.now().minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
        if (endDate == null) endDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        Map<String, Object> result = new HashMap<>();
        result.put("dailySales", orderMapper.selectDailySales(startDate, endDate));
        result.put("monthlySales", orderMapper.selectMonthlySales(startDate, endDate));
        result.put("totalAmount", orderMapper.selectTotalAmount(startDate, endDate));
        result.put("totalOrders", orderMapper.selectTotalOrders(startDate, endDate));
        result.put("totalBooks", orderMapper.selectTotalBooksSold(startDate, endDate));
        return Result.success(result);
    }

    @GetMapping("/hot-books")
    public Result<List<Map<String, Object>>> hotBooks() {
        return Result.success(orderMapper.selectHotBooks());
    }

    @GetMapping("/low-stock")
    public Result<List<Book>> lowStock() {
        int threshold = getWarningThreshold();
        List<Book> books = bookService.list(new QueryWrapper<Book>().le("stock", threshold).orderByAsc("stock"));
        return Result.success(books);
    }

    @GetMapping("/category-sales")
    public Result<List<Map<String, Object>>> categorySales() {
        return Result.success(orderMapper.selectCategorySales());
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        int threshold = getWarningThreshold();
        Map<String, Object> result = new HashMap<>();
        result.put("bookCount", bookService.count());
        result.put("orderCount", orderService.count());
        result.put("userCount", userService.count());
        result.put("warningCount", bookService.count(new QueryWrapper<Book>().le("stock", threshold)));
        return Result.success(result);
    }
}