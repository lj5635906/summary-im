# summary-im

基于 Netty 实现消息推送服务与 IM 即时通讯，同时支持 TCP 和 WebSocket 协议。

Netty 是一个广泛使用的高性能网络编程框架，非常适合用于开发即时通讯系统。即时通讯系统通常需要处理大量并发连接和实时消息传递。本项目既是根据Netty完成消息推送服务与 IM 即时通讯。同时支持 TCP 和 WebSocket 协议。

服务端主要支持：通知消息推送、点对点聊天、群聊、点对点未读消息、群聊未读消息等。

服务端主要支持：通知消息接收、点对点聊天、群聊、首次联系服务端失败自动重连、断线自动重连、自动心跳监测等。

Netty 版本

```xml
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.100.Final</version>
</dependency>
```



**主要实现思路** 

* 服务端、客户端

  * 服务端与客户端定义统一的消息报文、统一消息类型。

    消息报文：

    ```java
    /**
     * IM 消息体
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ImMsg implements Serializable {
        /*** 聊天id */
        private Long chatId;
        /*** 消息类型 */
        private int msgType;
        /*** 群聊消息的群id */
        private Long teamId;
        /*** 消息发送者用户Id */
        private Long fromUserId;
        /*** 消息接收者用户Id */
        private Long toUserId;
        /*** 发送客户端类型 */
        private int fromClientType;
        /*** 客户端消息Id */
        private String msgIdClient;
        /*** 消息体类型 */
        private int bodyType;
        /*** 消息体 */
        private String body;
        /*** 消息发送时间 */
        private Long sendTime;
    }
    ```

    消息类型：

    ```java
    /**
     * 消息类型
     */
    @Getter
    @AllArgsConstructor
    public enum MsgType {
        // 心跳
        heartbeat(0),
        // 登录
        login(1),
        // 退出登录
        logout(2),
        // 聊天
        chat(3),
        // 聊天-服务端应答消息
        chat_answer(4),
        // 进入某个聊天
        chat_enter(5),
        // 退出某个聊天
        chat_quit(6),
        // 通知
        notice(7);
    }
    ```

  * 自定义服务端、客户端消息编码、消息解码。

* 服务端

  * 配置 Netty 的 ChannelInitializer

    ```java
    public class ImChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    
        private final MsgHandlerAdapter msgHandlerAdapter;
    
        public ImChannelInitializer(MsgHandlerAdapter msgHandlerAdapter) {
            this.msgHandlerAdapter = msgHandlerAdapter;
        }
    
        @Override
        protected void initChannel(NioSocketChannel channel) throws Exception {
            // 获取管道
            ChannelPipeline pipeline = channel.pipeline();
    
            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
            pipeline.addLast(new SocketChooseHandler(msgHandlerAdapter));
        }
    }
    ```

    

  * 定义协议选择处理器

    根据请求连接报文动态配置 Netty 连接的通道，WebSocket 连接报文 " GET / " 开头。

    ```java
    @Slf4j
    public class SocketChooseHandler extends ByteToMessageDecoder {
    
        private final MsgHandlerAdapter msgHandlerAdapter;
        /**
         * 默认暗号长度为23
         */
        private static final int MAX_LENGTH = 23;
        /**
         * WebSocket握手的协议前缀
         */
        private static final String WEBSOCKET_PREFIX = "GET /";
    
        public SocketChooseHandler(MsgHandlerAdapter msgHandlerAdapter) {
            this.msgHandlerAdapter = msgHandlerAdapter;
        }
    
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
    
            if (isWebSocket(byteBuf)) {
                ChannelPipeline pipeline = ctx.pipeline();
                pipeline.addLast(new HttpServerCodec());
                // Http消息组装
                pipeline.addLast("aggregator", new WebSocketFrameAggregator(65536));
                // WebSocket通信支持
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                pipeline.addLast("decoder", new WebSocketMsgDecoder());
                pipeline.addLast("encoder", new WebSocketMsgEncoder());
                pipeline.addLast("handler", new ImServerWebSocketChannelHandler(msgHandlerAdapter));
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws-message"));
                pipeline.remove(this);
            } else {
                ChannelPipeline pipeline = ctx.pipeline();
                pipeline.addLast("decoder", new MsgDecoder());
                pipeline.addLast("encoder", new MsgEncoder());
                pipeline.addLast("handler", new ImServerSocketChannelHandler(msgHandlerAdapter));
                pipeline.remove(this);
            }
    
            byteBuf.resetReaderIndex();
        }
    
        private static boolean isWebSocket(ByteBuf byteBuf) {
            int length = byteBuf.readableBytes();
            if (length > MAX_LENGTH) {
                length = MAX_LENGTH;
            }
    
            // 标记读位置
            byteBuf.markReaderIndex();
            byte[] content = new byte[length];
            byteBuf.readBytes(content);
            String protocol = new String(content);
    
            // websocket 连接开始： GET /ws-message HTTP/1.1
            return protocol.startsWith(WEBSOCKET_PREFIX);
        }
    
    }
    ```

    

  * 定义 TCP 协议处理通道

    IM用户登录将 IM用户 与 Channel 进行缓存，并进行绑定。

  * 定义 WebSocket 协议处理通道

    WebSocket 协议处理通道 **继承** TCP 协议处理通道，因 WebSocket 需要处理 “三次握手”，其它都是统一的。

    ```java
    public class ImServerWebSocketChannelHandler extends ImServerSocketChannelHandler {
    
        public ImServerWebSocketChannelHandler(MsgHandlerAdapter msgHandlerAdapter) {
            super(msgHandlerAdapter);
        }
    
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    
            if (msg instanceof HttpRequest request) {
                // websocket 连接请求
                handHttpRequest(ctx, request);
            } else {
                if (msg instanceof ImMsgRequest) {
                    super.channelRead(ctx, msg);
                } else {
                    log.debug("other:{}", msg);
                }
            }
    
        }
    
        /**
         * 处理客户端向服务端发起 http 握手请求的业务
         * WebSocket在建立握手时，数据是通过HTTP传输的。但是建立之后，在真正传输时候是不需要HTTP协议的。
         * WebSocket 连接过程：
         * 首先，客户端发起http请求，经过3次握手后，建立起TCP连接；http请求里存放WebSocket支持的版本号等信息，如：Upgrade、Connection、WebSocket-Version等；
         * 然后，服务器收到客户端的握手请求后，同样采用HTTP协议回馈数据；
         * 最后，客户端收到连接成功的消息后，开始借助于TCP传输信道进行全双工通信。
         */
        private void handHttpRequest(ChannelHandlerContext ctx, HttpRequest request) {
    
            // 如果请求失败或者该请求不是客户端向服务端发起的 http 请求，则响应错误信息
            if (!request.decoderResult().isSuccess() || !("websocket".equals(request.headers().get("Upgrade")))) {
                sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }
    
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    "ws://" + request.headers().get("Host"), null, false);
            WebSocketServerHandshaker handShaker = wsFactory.newHandshaker(request);
            if (handShaker == null) {
                // 握手失败
                log.debug("握手失败....");
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 握手成功
                handShaker.handshake(ctx.channel(), request);
            }
    
        }
    
        /**
         * 服务端向客户端响应消息
         */
        private void sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse response) {
            if (response.status().code() != 200) {
                //创建源缓冲区
                ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
                //将源缓冲区的数据传送到此缓冲区
                response.content().writeBytes(byteBuf);
                //释放源缓冲区
                byteBuf.release();
            }
            //写入请求，服务端向客户端发送数据
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
            if (response.status().code() != 200) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
    ```

  * 点对点聊天、群聊需要各自进行处理。

  * 根据消息类型，进行各个消息类型具体处理

    通知消息走缓存获取IM用户是否在线，聊天消息走缓存获取IM用户是否在线，若在线check用户是否在该聊天，在该聊天直接推送聊天消息，不再则推送通知消息。

* 客户端

  * IM 客户端需要单例，全局只能创建一次。

  * 配置 Netty 的 ChannelInitializer，设置编码、解码、消息通道处理器。

  * 连接失败，通过定时线程池进行连接重试。

    ```java
            /**
         * 重新连接 处理线程池
         */
        private ScheduledExecutorService autoReconnectScheduledExecutor;
        /**
         * 自动重连次数
         */
        private final AtomicInteger AUTO_RECONNECT_TIME = new AtomicInteger(0);
        
        /**
         * 自动重连
         */
        private void autoReConnect(Long userId, ClientType clientType) {
            if (autoReconnectScheduledExecutor == null) {
                autoReconnectScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
            }
            autoReconnectScheduledExecutor.schedule(() -> {
                log.error("im client connect host【{}】 port【{}】 第[{}]次重试", host, port, AUTO_RECONNECT_TIME.incrementAndGet());
                if (this.connect()) {
                    login(userId, clientType);
                    autoReconnectScheduledExecutor.shutdown();
                } else {
                    autoReConnect(userId, clientType);
                }
            }, 3, TimeUnit.SECONDS);
        }
    ```

    

  * 在消息处理器建立连接后 缓存通道管道信息，登录后缓存IM用户信息。

  * 连接被动断开与连接异常时，通过定时线程池进行连接被动重连

    ```java
    /**
         * 重新连接 处理线程池
         */
        private static ScheduledExecutorService RE_CONNECT_SCHEDULED_EXECUTOR;
        /**
         * lock
         */
        private final Object lock = new Object();
    
        /**
         * 重连
         *
         * @param ctx .
         */
        public void reConnect(ChannelHandlerContext ctx) {
    
            if (ClientCtxState.getCurrentCtxState() == ClientCtxState.State.DISCONNECT_ACTIVE) {
                // 用户主动断开连接，不进行重连机制
                log.info("用户主动断开连接，不进行重连机制");
                return;
            }
    
            log.info("被动断开连接，5s 后自动重新连接");
    
            if (RE_CONNECT_SCHEDULED_EXECUTOR == null) {
                synchronized (lock) {
                    RE_CONNECT_SCHEDULED_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
                }
            }
    
            RE_CONNECT_SCHEDULED_EXECUTOR.schedule(() -> {
                boolean isConnect = imClient.connect();
                if (isConnect) {
                    RE_CONNECT_SCHEDULED_EXECUTOR.shutdown();
                } else {
                    reConnect(ctx);
                }
    
            }, 3, TimeUnit.SECONDS);
    
        }
    ```

    

  * 通过定时线程池进行心跳检测。

  * 根据消息类型，进行各个消息类型具体处理。



**数据结构**

用户：

```sql
drop table if exists users;
CREATE TABLE users(
    user_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '用户id',

    username     			varchar(20)	 	NOT NULL                    COMMENT '用户名称',
    mobile       			varchar(20)	 	NOT NULL                    COMMENT '电话',
    head_img     			varchar(125)	NOT NULL                    COMMENT '头像',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_user_id (user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表';
```

聊天：

```sql
drop table if exists chat;
CREATE TABLE chat(
    chat_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '聊天id',

    event_type		        int(2) 	        NOT NULL                    COMMENT '类型:点对点-0、群聊-1',
    from_user_id     		bigint(20)	    NOT NULL      DEFAULT 0     COMMENT '用户id(消息发送人)',
    to_user_id     			bigint(20)	    NOT NULL                    COMMENT '用户id(消息接收人)',
    team_id     			bigint(20)	    NOT NULL      DEFAULT 0     COMMENT '群id',
    from_unread_num         int(3)          NOT NULL      DEFAULT 0     COMMENT '未读消息数',
    to_unread_num           int(3)          NOT NULL      DEFAULT 0     COMMENT '未读消息数',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  UNIQUE KEY index_from_to_user_id (from_user_id,to_user_id),
  KEY index_to_user_id (to_user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天';
```

群 ：

```sql
drop table if exists team;
CREATE TABLE team(
    team_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群id',

    team_name     			varchar(50)	 	NOT NULL                    COMMENT '群名称',
    team_admin     			bigint(20)	    NOT NULL                    COMMENT '群主用户id',
    team_admin_name     	varchar(20)	 	NOT NULL                    COMMENT '群主名称',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)'

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群';
```

点对点消息：

```sql
drop table if exists person_msg;
CREATE TABLE person_msg(
    person_msg_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '点对点消息id',

    from_user_id     		bigint(20)	    NOT NULL                    COMMENT '用户id(消息发送人)',
    to_user_id     			bigint(20)	    NOT NULL                    COMMENT '用户id(消息接收人)',
    client_type             int(2)          NOT NULL                    COMMENT '客户端类型',
    body_type               int(2)          NOT NULL                    COMMENT '消息体类型',
    body     			    varchar(256) 	NOT NULL                    COMMENT '消息体',
    voice_time              int(3)          NOT NULL   DEFAULT 0        COMMENT '语音时长',
    cancel_flag             boolean         NOT NULL   DEFAULT 0        COMMENT '撤回标志(0/false-否,1/true-是)',
    read_flag               boolean         NOT NULL   DEFAULT 0        COMMENT '已读标识(0/false-否,1/true-是)',
    msg_id_client		    varchar(50) 	NOT NULL                    COMMENT '客户端消息id',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_from_user_id (from_user_id),
  KEY index_to_user_id (to_user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点对点消息';
```

群消息：

```sql
drop table if exists team_msg;
CREATE TABLE team_msg(
    team_msg_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群消息id',

    team_id     			bigint(20)	    NOT NULL                    COMMENT '群id',
    from_user_id     		bigint(20)	    NOT NULL                    COMMENT '用户id(消息发送人)',
    client_type             int(2)          NOT NULL                    COMMENT '客户端类型',
    msg_type                int(2)          NOT NULL                    COMMENT '消息类型',
    body_type               int(2)          NOT NULL                    COMMENT '消息体类型',
    body     			    varchar(256) 	NOT NULL                    COMMENT '消息体',
    voice_time              int(3)          NOT NULL   DEFAULT 0        COMMENT '语音时长',
    cancel_flag             boolean         NOT NULL   DEFAULT 0        COMMENT '撤回标志(0/false-否,1/true-是)',
    read_flag               boolean         NOT NULL   DEFAULT 0        COMMENT '已读标识(0/false-否,1/true-是)',
    read_num                int(3)          NOT NULL   DEFAULT 0        COMMENT '消息已读人数',

    msg_id_client		    varchar(50) 	NOT NULL                    COMMENT '客户端消息id',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_team_id (team_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群消息';
```

群成员消息：

```sql
drop table if exists team_member_msg;
CREATE TABLE team_member_msg(
    team_member_msg_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群成员消息id',

    chat_id     			bigint(20)	    NOT NULL                    COMMENT '聊天id',
    team_id     			bigint(20)	    NOT NULL                    COMMENT '群id',
    team_msg_id     		bigint(20)	    NOT NULL                    COMMENT '群消息id',
    from_user_id     		bigint(20)	    NOT NULL                    COMMENT '用户id(消息发送人)',
    to_user_id     			bigint(20)	    NOT NULL                    COMMENT '用户id(消息接收人)',
    read_flag               boolean         NOT NULL   DEFAULT 0        COMMENT '已读标识(0/false-否,1/true-是)',
    read_time               datetime        NOT NULL   DEFAULT CURRENT_TIMESTAMP        COMMENT '消息接受者阅读消息时间',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_team_id (team_id),
  KEY index_to_user_id (to_user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群成员消息';
```

