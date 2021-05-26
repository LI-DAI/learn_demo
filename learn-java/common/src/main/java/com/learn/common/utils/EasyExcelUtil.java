package com.learn.common.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LD
 * @date 2021/3/24 15:25
 */
public class EasyExcelUtil {

    private static final int DEFAULT_SIZE = 50;

    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, int size) {
        return new AnalysisEventListener<T>() {
            final List<T> dataList = new ArrayList<>();

            @Override
            public void invoke(T data, AnalysisContext analysisContext) {
                dataList.add(data);
                if (dataList.size() == size) {
                    consumer.accept(dataList);
                    dataList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (dataList.size() > 0) {
                    consumer.accept(dataList);
                }
            }
        };
    }

    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer) {
        return getListener(consumer, DEFAULT_SIZE);
    }
    
}
