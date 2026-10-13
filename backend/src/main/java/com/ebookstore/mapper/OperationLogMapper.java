package com.ebookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ebookstore.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    @Select("SELECT l.id, a.name AS operator, l.operation, l.target, l.detail, l.result, l.create_time " +
            "FROM operation_log l LEFT JOIN admin a ON l.admin_id = a.id " +
            "ORDER BY l.create_time DESC")
    List<Map<String, Object>> selectLogsWithOperator();
}