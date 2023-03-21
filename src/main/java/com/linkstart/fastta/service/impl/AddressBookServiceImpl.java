package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.entity.AddressBook;
import com.linkstart.fastta.mapper.AddressBookMapper;
import com.linkstart.fastta.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 地址薄管理业务层接口实现类
 */

@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public boolean setDefaultAddress(AddressBook addressBook) {
        //将旧的默认收获地址设置成非默认的
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, ThreadContext.getOnlineUser().getId()).eq(AddressBook::getIsDefault, 1);
        updateWrapper.set(AddressBook::getIsDefault, 0);
        this.update(updateWrapper);

        //将当前的addressBook保存成默认的收货地址
        addressBook.setIsDefault(1);
        boolean updateNewDefault = this.updateById(addressBook);
        return updateNewDefault;
    }

    @Override
    public boolean batchDeleteAddressBooks(List<Long> ids) {
        if(CollUtil.isEmpty(ids)) return false;
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AddressBook::getId, ids);
        return this.remove(queryWrapper);
    }

    @Override
    public AddressBook getDefaultAddress() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //查询当前登录用户的默认收货地址
        queryWrapper.eq(AddressBook::getUserId, ThreadContext.getOnlineUser().getId()).eq(AddressBook::getIsDefault, 1);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<AddressBook> getAddressBooks() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, ThreadContext.getOnlineUser().getId());
        //先展示默认收货地址，然后再根据地址簿更新时间排序
        queryWrapper.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);
        return this.list(queryWrapper);
    }
}