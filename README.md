小白一个，刚学习完 Netty，打算做一个 Demo 练练手。

**感谢以下项目提供的思路，向他们学习！**

1、https://github.com/ksfzhaohui/easy-binlog

2、https://github.com/Zhangwusheng/netty-binlog

**思路**

1、启动：
<br/>1.1、需要一个 Bootstrap 作为客户端的启动类。
<br/>1.2、然后添加指定的处理器来解析包，添加管道传输数据；
<br/>1.3、配置 ChannelFuture 类监听端口发起异步连接等。

2、那么需要什么处理器呢？
<br/>2.1、需要支持对 MySQL 协议进行解析的类。
<br/>2.2、需要支持对 MySQL 不同操作进行解析和处理；操作主要分为：建立连接操作、身份认证操作、内置事件操作。
<br/>2.3、我们主要是对 MySQL 内置事件进行提取一些主要信息：MDL、DDL、DCL操作。
