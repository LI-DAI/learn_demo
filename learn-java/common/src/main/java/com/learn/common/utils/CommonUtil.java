package com.learn.common.utils;

import com.alibaba.fastjson.JSON;
import com.learn.common.entity.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author LD
 * @date 2021/5/15 18:43
 */
public class CommonUtil {

    public static void print(Result<Object> result, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
