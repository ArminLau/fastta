package com.linkstart.fastta.controller;

import cn.hutool.core.collection.CollUtil;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.entity.AddressBook;
import com.linkstart.fastta.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 地址簿管理控制层
 */
@RestController
@RequestMapping("/addressBook")
@PreAuthorize("hasAuthority('Customer')")
@Slf4j
@Api(tags = "收获地址管理接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @ApiOperation("保存收货地址")
    @PostMapping
    public R saveAddressBook(@RequestBody AddressBook addressBook){
        addressBook.setUserId(ThreadContext.getOnlineUser().getId());
        return R.judge(addressBookService.save(addressBook), "新建地址保存成功", "新建地址保存失败");
    }

    @ApiOperation("修改收货地址")
    @PutMapping
    public R updateAddressBook(@RequestBody AddressBook addressBook){
        return R.judge(addressBookService.updateById(addressBook), "更新地址成功", "更新地址失败");
    }

    @ApiOperation("批量删除收货地址")
    @DeleteMapping
    public R batchDeleteAddressBook(@RequestParam List<Long> ids){
        return R.judge(addressBookService.batchDeleteAddressBooks(ids), "删除指定地址成功", "删除指定地址失败");
    }

    @ApiOperation("设定默认收货地址")
    @PutMapping("/default")
    public R setDefaultAddressBook(@RequestBody AddressBook addressBook){
        log.info("客户{}修改了默认的收获地址为{}", ThreadContext.getOnlineUser().getUsername(), addressBook.getDetail());
        return R.judge(addressBookService.setDefaultAddress(addressBook), "保存默认收货地址成功", "保存默认收货地址失败");
    }

    @ApiOperation("获取顾客默认收货地址")
    @GetMapping("/default")
    public R getDefaultAddressBook(){
        AddressBook defaultAddress = addressBookService.getDefaultAddress();
        if(defaultAddress == null){
            return R.error("用户没有默认的收货地址");
        }
        return R.success(defaultAddress);
    }

    @ApiOperation("查询顾客所有的收货地址")
    @GetMapping("/list")
    public R getAllAddressBook(){
        List<AddressBook> addressBooks = addressBookService.getAddressBooks();
        return R.success(addressBooks);
    }

    @ApiOperation("根据ID获取相应的收货地址信息")
    @GetMapping("/{id}")
    public R getAddressBookById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook == null){
            return R.error("没有找到对应的收货地址");
        }
        return R.success(addressBook);
    }
}