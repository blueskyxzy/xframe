package com.frame.base.common.utils;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by xzy on 2020/06/25  .
 */

public class CollectionUtils<T>{


    /**
     * List分组
     */
    public List<List<T>>  getListGroup(List<T> datas, int groupSize) {
        List<List<T>> lists = Lists.partition(datas, groupSize);
        return lists;
    }

}
