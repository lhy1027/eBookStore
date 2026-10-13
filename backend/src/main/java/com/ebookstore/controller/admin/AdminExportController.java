package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Order;
import com.ebookstore.entity.User;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.IOrderService;
import com.ebookstore.service.interf.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin/export")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminExportController {

    private final IBookService bookService;
    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping("/books")
    public void exportBooks(@RequestParam(required = false) String keyword, HttpServletResponse resp) throws IOException {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("name", keyword).or().like("book_no", keyword));
        }
        List<Book> books = bookService.list(wrapper);

        setCsvHeaders(resp, "books.csv");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        pw.println("\uFEFF编号,书名,作者,出版社,ISBN,定价,销售价,库存,状态");
        for (Book b : books) {
            pw.printf("%s,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%.2f,%d,%s%n",
                    nvl(b.getBookNo()), nvl(b.getName()), nvl(b.getAuthor()),
                    nvl(b.getPublisher()), nvl(b.getIsbn()),
                    b.getPrice() != null ? b.getPrice().doubleValue() : 0,
                    b.getSalePrice() != null ? b.getSalePrice().doubleValue() : 0,
                    b.getStock() != null ? b.getStock() : 0,
                    b.getStatus() == 1 ? "上架" : "下架");
        }
        pw.flush();
    }

    @GetMapping("/orders")
    public void exportOrders(HttpServletResponse resp) throws IOException {
        List<Order> orders = orderService.list(new QueryWrapper<Order>().orderByDesc("create_time"));

        setCsvHeaders(resp, "orders.csv");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        pw.println("\uFEFF订单号,用户ID,总金额,订单状态,物流状态,下单时间,支付时间,发货时间,签收时间");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Order o : orders) {
            pw.printf("%s,%d,%.2f,%s,%s,%s,%s,%s,%s%n",
                    nvl(o.getOrderNo()), o.getUserId() != null ? o.getUserId() : 0,
                    o.getTotalAmount() != null ? o.getTotalAmount().doubleValue() : 0,
                    nvl(o.getOrderStatus()), nvl(o.getLogisticsStatus()),
                    fmtTime(o.getCreateTime(), fmt), fmtTime(o.getPayTime(), fmt),
                    fmtTime(o.getShipTime(), fmt), fmtTime(o.getReceiptTime(), fmt));
        }
        pw.flush();
    }

    @GetMapping("/users")
    public void exportUsers(HttpServletResponse resp) throws IOException {
        List<User> users = userService.list(new QueryWrapper<User>().orderByDesc("create_time"));

        setCsvHeaders(resp, "users.csv");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        pw.println("\uFEFF用户名,手机号,邮箱,状态,注册时间");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (User u : users) {
            pw.printf("%s,%s,%s,%s,%s%n",
                    nvl(u.getUsername()), nvl(u.getPhone()), nvl(u.getEmail()),
                    u.getStatus() == 1 ? "正常" : "禁用",
                    fmtTime(u.getCreateTime(), fmt));
        }
        pw.flush();
    }

    private void setCsvHeaders(HttpServletResponse resp, String filename) {
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    private String nvl(String s) { return s != null ? s : ""; }

    private String fmtTime(LocalDateTime dt, DateTimeFormatter fmt) {
        return dt != null ? dt.format(fmt) : "";
    }
}