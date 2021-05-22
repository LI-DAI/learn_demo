package com.learn.admin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));
        return map2XContentBuilder(jsonObject);
    }

    /**
     * 将 Map 转化为 XContentBuilder
     *
     * @param map 要转换的map
     * @return {@link XContentBuilder}
     * @throws Exception /
     */
    public static XContentBuilder map2XContentBuilder(Map<String, Object> map) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            xContentBuilder.field(entry.getKey(), entry.getValue());
        }
        xContentBuilder.endObject();
        return xContentBuilder;
    }

}
