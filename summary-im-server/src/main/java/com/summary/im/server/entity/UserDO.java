package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class UserDO extends BaseDO<UserDO> {

    /*** 用户id */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /*** 用户名称 */
    private String username;
    /*** 电话 */
    private String mobile;
    /*** 头像 */
    private String headImg;

    @Builder
    public UserDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long userId, String username, String mobile, String headImg) {
        super(version, createTime, updateTime, deleteFlag);
        this.userId = userId;
        this.username = username;
        this.mobile = mobile;
        this.headImg = headImg;
    }
}
