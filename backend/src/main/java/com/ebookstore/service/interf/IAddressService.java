package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Address;
import java.util.List;

public interface IAddressService extends IService<Address> {
    List<Address> getUserAddresses(Long userId);
    void addAddress(Address address);
    void setDefault(Long id, Long userId);
}
