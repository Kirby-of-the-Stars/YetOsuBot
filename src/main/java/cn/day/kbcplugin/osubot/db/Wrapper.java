package cn.day.kbcplugin.osubot.db;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.annotion.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Wrapper<T> extends Entity {

    private String tableName;

    public Wrapper(){
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        try {
            Class<T> c = (Class<T>) p.getActualTypeArguments()[0];
            TableName table_name = c.getAnnotation(TableName.class);
            if (table_name != null) {
                tableName = table_name.value();
            } else tableName = c.getName();
            BeanUtil.copyProperties(Entity.create(tableName),this);
        }catch (ClassCastException c){
            Main.logger.error("Wrapper反射出错:",c.getCause());
        }
    }

    public Entity toEntity(){
        Entity entity = new Entity();
        BeanUtil.copyProperties(this,entity);
        return entity;
    }

}
