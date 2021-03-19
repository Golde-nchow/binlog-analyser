package com.netty.binlog.entity.autentication;

import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 认证方式接口
 *
 * @date 2021/3/16 下午11:12
 */
public interface AuthMethod {

    /**
     * 将认证请求包，封装为 byte[]，方便发送
     * @return 认证请求包的 byte[] 形式
     */
    ByteBuf toByteBuf();

    /**
     * 初始化认证内容
     * @param schema 数据库名称，无则填 null
     * @param username 认证的数据库账号
     * @param password 认证的数据库密码
     * @param scramble capability flags, CLIENT_SSL always set
     * @param charset 编码
     */
    void init(String schema, String username, String password, String scramble, int charset);

}
