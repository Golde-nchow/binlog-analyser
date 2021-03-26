package com.netty.binlog.handler;

import com.netty.binlog.constant.CharsetConstant;
import com.netty.binlog.constant.FieldType;
import com.netty.binlog.constant.ProtocolStatusFlags;
import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by chow
 * @Description 获取 binlog 信息的处理器
 *
 * 问题：由于查询后的数据，是通过多个数据报进行响应的，所以如何一下子处理这些数据报？
 *
 * @date 2021/3/24 下午10:54
 */
public class FetchBinlogHandler extends SimpleChannelInboundHandler<PackageData> {

    /**
     * 数据库：字段-值
     */
    private Map<String, Object> fields = new LinkedHashMap<>();

    /**
     * 元数据序列号
     */
    private AtomicInteger metaDataSequence = new AtomicInteger(1);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {

        ByteBuf content = msg.getContent();
        Integer packageSequenceId = msg.getHeader().getSequenceId();
        int sequenceId = metaDataSequence.get();

        if (packageSequenceId == 1) {
            // 如果是第一个包，那么存储的肯定是列数量
            // int<lenenc>：列数量
            int result = ByteUtil.readLenencInt(content);

            // 设置元数据数量
            metaDataSequence.compareAndSet(sequenceId, result + sequenceId);
            System.out.println("列数量：" + result);

        } else if (packageSequenceId <= sequenceId) {
            // 若少于元数据的数量，那么就一直读字段的属性（ Column Definition包 ），每次读完，包序列号就+1
            // 1、string<lenenc>：catalog 目录，目前一直是 def
            String catalog = ByteUtil.readLenencString(content);

            // 2、string<lenenc>：数据库
            String schema = ByteUtil.readLenencString(content);

            // 3、string<lenenc>：逻辑表名（virtual table name）
            String table = ByteUtil.readLenencString(content);

            // 4、string<lenenc>：物理表名（physical table name）
            String orgTable = ByteUtil.readLenencString(content);

            // 5、string<lenenc>：字段逻辑名称
            String name = ByteUtil.readLenencString(content);

            // 6、string<lenenc>：字段物理名称
            String orgName = ByteUtil.readLenencString(content);

            // 7、int<lenenc>：定长字段的长度（0x0c）
            int fixLengthFields = ByteUtil.readLenencInt(content);

            // 8、int<2>：编码
            int characterSet = ByteUtil.readInt(content, 2);

            // 9、int<4>：列长度
            int columnLength = ByteUtil.readInt(content, 4);

            // 10、int<1>：字段类型
            int type = ByteUtil.readInt(content, 1);

            // 11、int<2>：flags 字段标志
            int flags = ByteUtil.readInt(content, 2);

            // 12、int<1>：最大显示的小数位
            // 0x00：整数、字符串
            // 0x1f：动态字符串、double、float
            // 0x00 - 0x51：小数
            int maxShownDecimalDigits = ByteUtil.readInt(content, 1);

            System.out.println("目录：" + catalog);
            System.out.println("数据库：" + schema);
            System.out.println("逻辑表名：" + table);
            System.out.println("物理表名：" + orgTable);
            System.out.println("字段逻辑名称：" + name);
            System.out.println("字段物理名称：" + orgName);
            System.out.println("定长字段的长度：" + fixLengthFields);
            System.out.println("编码：" + CharsetConstant.get(characterSet - 1));
            System.out.println("列长度：" + columnLength);
            System.out.println("字段类型：" + FieldType.get(type));
            System.out.println("flags 字段标志：" + flags);
            System.out.println("最大显示的小数位：" + maxShownDecimalDigits);

            System.out.println("======================== 第 "+ (packageSequenceId - 1) +" 个字段解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 1) {

            // EOF 数据包，是 OK_package、ERR_package、EOF_package 其中之一.
            // 代表元数据的结尾

            // Header；1字节：0xFE
            content.skipBytes(1);

            // warnings；2字节：警告数量；
            int warningNumbers = ByteUtil.readInt(content, 2);

            // status_flags；2字节；
            int statusFlags = ByteUtil.readInt(content, 2);

            System.out.println("警告数量：" + warningNumbers);
            System.out.println("状态：" + ProtocolStatusFlags.getStatusFlagsDescription(statusFlags));

            System.out.println("======================== 元数据 EOF_package 解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 2) {
            // 返回的结果：One or more Text Resultset Row
            // 若数据是 0xFB，则代表 NULL
            // 从内容开始，每个值都是一个 string<lenenc>

            int fieldNum = metaDataSequence.get() - 1;
            while (fieldNum != 0) {

                String fieldValue = ByteUtil.readLenencString(content);

                System.out.println("第"+ (metaDataSequence.get() - fieldNum) + "个字段的值：" + fieldValue);

                fieldNum--;
            }
            System.out.println("======================== 字段值解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 3) {
            // 结果集的 EOF 结束包
            // Header；1字节：0xFE
            content.skipBytes(1);

            // warnings；2字节：警告数量；
            int warningNumbers = ByteUtil.readInt(content, 2);

            // status_flags；2字节；
            int statusFlags = ByteUtil.readInt(content, 2);

            System.out.println("警告数量：" + warningNumbers);
            System.out.println("状态：" + ProtocolStatusFlags.getStatusFlagsDescription(statusFlags));

            System.out.println("======================== 结果集 EOF_package 解析完成 ========================");
        }
    }
}
