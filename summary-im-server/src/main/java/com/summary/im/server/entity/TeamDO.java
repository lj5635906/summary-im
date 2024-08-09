package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 群
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("team")
public class TeamDO extends BaseDO<TeamDO> {

    /*** 群id */
    @TableId(value = "team_id", type = IdType.AUTO)
    private Long teamId;
    /*** 群名称 */
    private String teamName;
    /*** 群主用户id */
    private Long teamAdmin;
    /*** 群主名称 */
    private String teamAdminName;

    @Builder
    public TeamDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long teamId, String teamName, Long teamAdmin, String teamAdminName) {
        super(version, createTime, updateTime, deleteFlag);
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamAdmin = teamAdmin;
        this.teamAdminName = teamAdminName;
    }
}
