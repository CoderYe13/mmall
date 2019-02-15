package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.commom.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount=shippingMapper.insert(shipping);
        /*
        *技巧：如何拿到刚刚在后端插入的记录的id,在SQL上
        * <insert id="insert" parameterType="com.mmall.pojo.Shipping" useGeneratedKeys="true" keyProperty="id">
        *     加入useGeneratedKeys="true" keyProperty="id"即可把新增加的主键赋值到自己定义的keyProperty（id）中
        *     返回的id是映射回实体类中的
         */
        if(rowCount>0){
            Map result= Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> del(Integer userId,Integer shippingId){
        //int resultCount=shippingMapper.deleteByPrimaryKey(shippingId);
        // 这里为防止出现横向越权，删除了不是这个用户的地址，这里需要判断这个是不是这个用户的地址，故采用下面的方法
       int resultCount=shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount=shippingMapper.updateByShipping(shipping);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping=shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if(shipping==null){
            return ServerResponse.createByErrorMessage("无法查询到改地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
