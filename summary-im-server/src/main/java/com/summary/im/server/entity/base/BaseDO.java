package com.summary.im.server.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基础信息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDO<T extends Model<T>> extends Model<T> {
    /*** 版本号*/
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /*** 创建时间*/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /*** 更新时间*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*** 删除标志(0/false-未删除,1/true-已删除)*/
    @TableField(value = "delete_flag")
    @TableLogic
    private Boolean deleteFlag;
}
