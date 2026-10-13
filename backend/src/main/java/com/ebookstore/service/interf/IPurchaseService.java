package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Purchase;
import com.ebookstore.dto.request.PurchaseReq;

public interface IPurchaseService extends IService<Purchase> {
    void addPurchase(PurchaseReq req);
}
