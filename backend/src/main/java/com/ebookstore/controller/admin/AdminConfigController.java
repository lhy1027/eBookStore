package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.SystemConfig;
import com.ebookstore.mapper.SystemConfigMapper;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminConfigController {

    private final SystemConfigMapper configMapper;

    @GetMapping("/{key}")
    public Result<String> getConfig(@PathVariable String key) {
        SystemConfig config = configMapper.selectOne(
                new QueryWrapper<SystemConfig>().eq("config_key", key));
        return Result.success(config != null ? config.getConfigValue() : "5");
    }

    @PutMapping("/{key}")
    @Log("修改系统配置")
    public Result<String> setConfig(@PathVariable String key, @RequestBody Map<String, String> params) {
        String value = params.get("value");
        SystemConfig config = configMapper.selectOne(
                new QueryWrapper<SystemConfig>().eq("config_key", key));
        if (config == null) {
            config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setDescription("库存预警阈值");
            configMapper.insert(config);
        } else {
            config.setConfigValue(value);
            configMapper.updateById(config);
        }
        return Result.success("保存成功");
    }
}