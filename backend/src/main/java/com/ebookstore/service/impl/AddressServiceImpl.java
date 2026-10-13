package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Address;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.AddressMapper;
import com.ebookstore.service.interf.IAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return list(new QueryWrapper<Address>().eq("user_id", userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAddress(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            // 取消其他默认地址
            update(new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Address>()
                    .eq("user_id", address.getUserId())
                    .set("is_default", 0));
        }
        save(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id, Long userId) {
        Address address = getById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }
        update(new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Address>()
                .eq("user_id", userId).set("is_default", 0));
        address.setIsDefault(1);
        updateById(address);
    }
}
