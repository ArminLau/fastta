package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.Flavor;
import com.linkstart.fastta.mapper.FlavorMapper;
import com.linkstart.fastta.service.FlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品风味业务层的接口实现类
 */

@Service
@Slf4j
public class FlavorServiceImpl extends ServiceImpl<FlavorMapper, Flavor> implements FlavorService {
    @Autowired
    private FlavorMapper flavorMapper;

    @Override
    public boolean addFlavor(List<Flavor> flavors) {
        if(CollUtil.isEmpty(flavors)){
            return true;
        }

        //获取数据库中所有的菜品风味信息
        LambdaQueryWrapper<Flavor> queryWrapper = new LambdaQueryWrapper<>();
        //只查询风味名字和风味选项，确保其他值为null
        queryWrapper.select(Flavor::getName, Flavor::getOption);
        List<Flavor> dbFlavors = flavorMapper.selectList(queryWrapper);

        if(CollUtil.isNotEmpty(dbFlavors)){
            //过滤掉数据库中已存在的风味
            flavors = flavors.stream().filter(x -> !dbFlavors.contains(x)).collect(Collectors.toList());
        }
        return CollUtil.isEmpty(flavors) ? true : this.saveBatch(flavors);
    }

    @Override
    public List<Flavor> getFlavor() {
        List<Flavor> dbFlavors = this.list();
        if(CollUtil.isEmpty(dbFlavors)){
            return null;
        }
        Map<String, List<String>> flavorMap = new HashMap<>();
        dbFlavors.forEach(x -> {
            String flavorName = x.getName();
            if(flavorMap.containsKey(flavorName)){
                flavorMap.get(flavorName).add(x.getOption());
            }else {
                List<String> options = new ArrayList<>();
                options.add(x.getOption());
                flavorMap.put(flavorName, options);
            }
        });
        List<Flavor> flavors = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : flavorMap.entrySet()) {
            flavors.add(new Flavor(entry.getKey(), entry.getValue()));
        }
        return flavors;
    }
}