package com.netty.binlog.entity.autentication;

import com.netty.binlog.entity.autentication.impl.MySql41Authentication;
import com.netty.binlog.enumeration.AuthMethods;

/**
 * @author by chow
 * @Description 认证方式工厂类
 *
 * 因为这里不会有很多的实现类，所以就使用了简单工厂模式。
 *
 * 三种身份认证方式：
 * 1、PLAIN Authentication：只支持在 TLS 协议的基础上进行的认证方式
 *
 * 2、MYSQL41 Authentication：支持 MySQL 4.1+；
 *    1. Client: AuthenticateStart
 *    2. Server: challenge
 *    3. Client: [ authzid(empty) ] \0 authcid(user name) \0 response \0
 *    4. Server: AuthenticateOk
 *    【 response 】 = HEX(SHA1(password) ^ SHA1(challenge + SHA1(SHA1(password))));
 *
 * 3、SHA256_MEMORY Authentication：和 MYSQL41 Authentication 相似，区别在于使用 SHA256 进行加密。
 *
 * @date 2021/3/16 下午11:04
 */
public class AuthMethodFactory {

    /**
     * 通过 AuthMethods 枚举，获取指定的实现类
     * @param authMethod 认证方法枚举类
     * @return 认证方法实现类
     */
    public AuthMethod getAuthMethodImpl(AuthMethods authMethod) {
        // 若是 MySQL41 的认证方式
        if (authMethod.equals(AuthMethods.NATIVE)) {
            return new MySql41Authentication();
        }
        return null;
    }

}
