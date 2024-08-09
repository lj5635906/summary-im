package com.summary.im.server.mapper.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 元数据操作处理器
 *
 * @author jie.luo
 * @since 2024/5/29
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        this.fillStrategy(metaObject, "version", 0);
        this.fillStrategy(metaObject, "createTime", now);
        this.fillStrategy(metaObject, "updateTime", now);
        this.fillStrategy(metaObject, "deleteFlag", false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        this.fillStrategy(metaObject, "updateTime", now);
    }
}
