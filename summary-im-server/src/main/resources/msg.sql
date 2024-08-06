drop table if exists users;
CREATE TABLE users(
    user_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '用户id',

    username     			varchar(20)	 	NOT NULL                    COMMENT '用户名称',
    head_img     			varchar(125)	NOT NULL                    COMMENT '头像',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_user_id (user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表';

drop table if exists team;
CREATE TABLE team(
    team_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群id',

    team_name     			varchar(20)	 	NOT NULL                    COMMENT '群名称',
    team_admin     			bigint(20)	    NOT NULL                    COMMENT '群主',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)'

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群';

drop table if exists team_member;
CREATE TABLE team_member(
    team_member_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群成员id',

    team_id     			bigint(20)	    NOT NULL                    COMMENT '群id',
    user_id     			bigint(20)	    NOT NULL                    COMMENT '用户id',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_team_id (team_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群成员';

drop table if exists team_msg;
CREATE TABLE team_msg(
    team_msg_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '群消息id',

    msg_id_client		    varchar(50) 	NOT NULL                    COMMENT '客户端消息id',
    team_id     			bigint(20)	    NOT NULL                    COMMENT '群id',
    from_user_id     		bigint(20)	    NOT NULL                    COMMENT '用户id(消息发送人)',
    client_type             int(2)          NOT NULL                    COMMENT '客户端类型',
    body_type               int(2)          NOT NULL                    COMMENT '消息体类型',
    body     			    varchar(256) 	NOT NULL                    COMMENT '消息体',
    voice_time              int(3)          NOT NULL   DEFAULT 0        COMMENT '语音时长',
    cancel_flag             boolean         NOT NULL   DEFAULT 0        COMMENT '撤回标志(0/false-否,1/true-是)',
    read_flag               boolean         NOT NULL   DEFAULT 0        COMMENT '已读标识(0/false-否,1/true-是)',
    read_num                int(3)          NOT NULL   DEFAULT 0        COMMENT '消息已读人数',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_team_id (team_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群消息';

drop table if exists person_msg;
CREATE TABLE person_msg(
    person_msg_id 	            bigint(20)	 	NOT NULL PRIMARY KEY 	AUTO_INCREMENT	COMMENT '点对点消息id',

    msg_id_client		    varchar(50) 	NOT NULL                    COMMENT '客户端消息id',
    from_user_id     		bigint(20)	    NOT NULL                    COMMENT '用户id(消息发送人)',
    to_user_id     			bigint(20)	    NOT NULL                    COMMENT '用户id(消息接收人)',
    body_type               int(2)          NOT NULL                    COMMENT '消息体类型',
    body     			    varchar(256) 	NOT NULL                    COMMENT '消息体',
    voice_time              int(3)          NOT NULL   DEFAULT 0        COMMENT '语音时长',
    cancel_flag             boolean         NOT NULL   DEFAULT 0        COMMENT '撤回标志(0/false-否,1/true-是)',
    read_flag               boolean         NOT NULL   DEFAULT 0        COMMENT '已读标识(0/false-否,1/true-是)',

    version 				int(11) 		NOT NULL DEFAULT 0			COMMENT '版本号',
    create_time 			datetime 		DEFAULT CURRENT_TIMESTAMP 	COMMENT '创建时间',
    update_time 			datetime 		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag 			boolean 		NOT NULL DEFAULT 0			COMMENT '删除标志(0/false-未删除,1/true-已删除)',
  KEY index_from_user_id (from_user_id),
  KEY index_to_user_id (to_user_id)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点对点消息';