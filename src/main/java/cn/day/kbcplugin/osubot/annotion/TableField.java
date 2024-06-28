package cn.day.kbcplugin.osubot.annotion;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableField {

    String value() default "";

    /**
     * 是否为数据库表字段
     * <p>
     * 默认 true 存在，false 不存在
     */
    boolean exist() default true;
}
