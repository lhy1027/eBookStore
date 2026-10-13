package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.entity.OperationLog;
import com.ebookstore.mapper.OperationLogMapper;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLogController {

    private final OperationLogMapper operationLogMapper;

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // 获取总数
        long total = operationLogMapper.selectCount(null);
        // 获取当前页数据（含操作人）
        List<Map<String, Object>> records = operationLogMapper.selectLogsWithOperator();
        // 手动分页
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, records.size());
        List<Map<String, Object>> pageRecords = fromIndex < records.size() ? records.subList(fromIndex, toIndex) : Collections.emptyList();

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageRecords);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        return Result.success(result);
    }
}