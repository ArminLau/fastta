package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.AddressBook;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 地址薄管理业务层接口
 */

public interface AddressBookService extends IService<AddressBook> {
    /**
     * 设置当前登录客户的默认的收货地址
     * @param addressBook
     * @return
     */
    boolean setDefaultAddress(AddressBook addressBook);

    /**
     * 获取当前登录客户默认的收货地址
     * @return
     */
    AddressBook getDefaultAddress();

    /**
     * 获取当前登录客户的所有收获地址
     * @return
     */
    List<AddressBook> getAddressBooks();

    /**
     * 根据提供的多个地址簿ID批量删除地址簿
     * @param ids
     * @return
     */
    boolean batchDeleteAddressBooks(List<Long> ids);
}
