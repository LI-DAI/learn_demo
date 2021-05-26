package com.learn.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LD
 * @date 2021/5/26 15:27
 */
@AllArgsConstructor
public enum Enabled {

    /**
     * 已停用
     */
    DISABLED(0),

    /**
     * 已启用
     */
    ENABLED(1);

    @Getter
    private final Integer value;

}
