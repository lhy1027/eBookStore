package com.ebookstore.controller.client;

import com.ebookstore.entity.Address;
import com.ebookstore.entity.User;
import com.ebookstore.service.interf.IAddressService;
import com.ebookstore.service.interf.IUserService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IAddressService addressService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public Result<User> profile() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(userService.getById(userId));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public Result<String> updateProfile(@RequestBody User user) {
        Long userId = SecurityUtil.getCurrentUserId();
        user.setId(userId);
        userService.updateProfile(user);
        return Result.success("更新成功");
    }

    @GetMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public Result<List<Address>> addresses() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(addressService.getUserAddresses(userId));
    }

    @PostMapping("/addresses")
    @PreAuthorize("isAuthenticated()")
    public Result<String> addAddress(@RequestBody Address address) {
        Long userId = SecurityUtil.getCurrentUserId();
        address.setUserId(userId);
        addressService.addAddress(address);
        return Result.success("添加成功");
    }

    @PutMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<String> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        Long userId = SecurityUtil.getCurrentUserId();
        address.setId(id);
        address.setUserId(userId);
        addressService.updateById(address);
        return Result.success("更新成功");
    }

    @DeleteMapping("/addresses/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<String> deleteAddress(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.success("删除成功");
    }

    @PutMapping("/addresses/{id}/default")
    @PreAuthorize("isAuthenticated()")
    public Result<String> setDefault(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        addressService.setDefault(id, userId);
        return Result.success("设置成功");
    }
}
