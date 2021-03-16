package com.netty.binlog.enumeration;

/**
 * @author by chow
 * @Description 认证方法
 * @date 2021/3/16 下午11:31
 */
public enum AuthMethods {

    /**
     * NATIVE 认证方式，比较常用的是这个
     */
    NATIVE("mysql_native_password")
    ;

    /**
     * 认证方法详细描述
     */
    private String desc;

    AuthMethods(String desc) {
        this.desc = desc;
    }

    /**
     * 比较两个认证方式枚举是否一致
     * @param authMethod 认证方式枚举
     * @return 若一致，则返回 true
     */
    public boolean equals(AuthMethods authMethod) {
        return this.desc.equals(authMethod.desc);
    }
}
