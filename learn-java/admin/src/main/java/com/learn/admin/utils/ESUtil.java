package com.learn.admin.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.util.Map;

/**
 * @author LD
 * @date 2021/5/21 11:05
 */
public class ESUtil {

    /**
     * 将实体转化为 XContentBuilder
     *
     * @param object 要转化的对象
     * @return {@link XContentBuilder}
     */
    public static XContentBuilder object2XContentBuilder(Object object) throws Exception {
        Map<String, Object> objectMap = BeanUtil.beanToMap(object);
        return map2XContentBuilder(objectMap);
    }

    /**
     * 将 Map 转化为 XContentBuilder
     *
     * @param objectMap 要转换的map
     * @return {@link XContentBuilder}
     * @throws Exception /
     */
    public static XContentBuilder map2XContentBuilder(Map<String, Object> objectMap) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();

        if (CollectionUtil.isEmpty(objectMap)) {
            return xContentBuilder;
        }

        xContentBuilder.startObject();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            xContentBuilder.field(entry.getKey(), entry.getValue());
        }
        xContentBuilder.endObject();
        return xContentBuilder;
    }

}
