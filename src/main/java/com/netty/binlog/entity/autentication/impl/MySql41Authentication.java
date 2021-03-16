package com.netty.binlog.entity.autentication.impl;

import com.netty.binlog.entity.autentication.AuthMethod;

/**
 * @author by chow
 * @Description 使用 MySql41 方式的身份认证；
 * @date 2021/3/11 上午12:09
 */
public class MySql41Authentication implements AuthMethod {

    private String schema;
    private String username;
    private String password;
    private String scramble;
    private int charset;

    /**
     * 将认证请求包，封装为 byte[]，方便发送
     *
     * @return 认证请求包的 byte[] 形式
     */
    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    /**
     * 认证
     *
     * @param schema   数据库名称，无则填 null
     * @param username 认证的数据库账号
     * @param password 认证的数据库密码
     * @param scramble capability flags, CLIENT_SSL always set
     * @param charset  编码
     */
    @Override
    public void init(String schema, String username, String password, String scramble, int charset) {
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.scramble = scramble;
        this.charset = charset;
    }
}
