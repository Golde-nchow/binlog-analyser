package com.netty.binlog.entity.autentication.impl;

import com.netty.binlog.constant.ClientCapabilitiesConstant;
import com.netty.binlog.entity.autentication.AuthMethod;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.StringUtils;

/**
 * @author by chow
 * @Description 使用 MySql41 方式的身份认证；
 * @date 2021/3/11 上午12:09
 */
public class MySql41Authentication implements AuthMethod {

    /**
     * 数据库名称
     */
    private String schema;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * auth-plugin-data:不知道什么意思
     */
    private String scramble;

    /**
     * 编码
     */
    private int charset;

    /**
     * 将认证请求包，封装为 ByteBuf，方便发送
     *
     * 基本上就是按照 mysql 文档进行转换的：
     * https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#Protocol::HandshakeResponse41
     *
     * @return 认证请求包的 ByteBuf 形式
     */
    @Override
    public ByteBuf toByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();

        // 好像官网没有给出为什么要这样做，但是貌似需要什么就 | 什么就可以了.
        int clientCapabilities = ClientCapabilitiesConstant.LONG_FLAG |
                ClientCapabilitiesConstant.PROTOCOL_41 |
                ClientCapabilitiesConstant.SECURE_CONNECTION |
                ClientCapabilitiesConstant.PLUGIN_AUTH;

        // 若有数据库名称，则连接到数据库
        if (StringUtils.isNoneEmpty(schema)) {
            clientCapabilities |= ClientCapabilitiesConstant.CONNECT_WITH_DB;
        }

        // 首先是4字节： capability flags
        byte[] capabilityFlags = ByteUtil.writeInt(clientCapabilities, 4);
        byteBuf.writeBytes(capabilityFlags);

        // 4字节： max-packet size
        byte[] maxPacketSize = ByteUtil.writeInt(0, 4);
        byteBuf.writeBytes(maxPacketSize);

        // 1字节： character set
        byte[] characterSet = ByteUtil.writeInt(charset, 1);
        byteBuf.writeBytes(characterSet);

        // 23字节： reserved（内容全都为0）
        byte[] reserved = ByteUtil.writeInt(0, 23);
        byteBuf.writeBytes(reserved);

        // NullString 结束： plugin name（其实就是用户名）
        byte[] pluginName = ByteUtil.writeNullTerminatedString(username);
        byteBuf.writeBytes(pluginName);

        /*
         * 二选一：目前肯定是选择第二种
         *
         * 1、CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA：客户端插件检测客户端数据
         *    lenenc-int     ：length of auth-response
         *    NullString 结束 ：auth-response
         *
         * 2、CLIENT_SECURE_CONNECTION：客户端建立安全连接
         *    1字节          ：length of auth-response
         *    NullString 结束：auth-response（使用 scramble、SHA1等加密后的密码）
         */

        // 对密码进行加密：HEX(SHA1(password) ^ SHA1(challenge + SHA1(SHA1(password))))
        byte[] encryptedPassword = ByteUtil.encryptPasswordWithSha1(password, scramble);

        // 1字节：length of auth-response
        byte[] lengthOfAuthResponse = ByteUtil.writeInt(encryptedPassword.length, 1);
        byteBuf.writeBytes(lengthOfAuthResponse);

        // n个字符，没有结束符：auth-response
        byteBuf.writeBytes(encryptedPassword);

        /*
         * 若建立的是数据库连接，加上下面的 n 字节数据：
         * NullString 结束：CLIENT_CONNECT_WITH_DB
         * 目前不需要.
         */
        if (StringUtils.isNoneEmpty(schema)) {
            byte[] connectWithDb = ByteUtil.writeNullTerminatedString(schema);
            byteBuf.writeBytes(connectWithDb);
        }

        /*
         * 若使用的是 CLIENT_PLUGIN_AUTH，加上下面的 n 字节数据：
         * NullString 结束：auth plugin name （目前是 mysql_native_password）
         */

        // NullString 结束：auth plugin name
        byte[] authPluginName = ByteUtil.writeNullTerminatedString("mysql_native_password");
        byteBuf.writeBytes(authPluginName);

        /*
         * 若有客户端连接参数，加上：
         * lenenc-int：length of all key-values
         * lenenc-str：key
         * lenenc-str：value
         * 若有更多的数据，请设置更多的 key-value 对，并且延长长度.
         *
         * 目前没有.
         */

        return byteBuf;
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
