package cn.day.kbcplugin.osubot.db;

import cn.day.kbcplugin.osubot.Main;
import cn.day.kbcplugin.osubot.annotion.TableField;
import cn.day.kbcplugin.osubot.annotion.TableId;
import cn.day.kbcplugin.osubot.annotion.TableName;
import cn.day.kbcplugin.osubot.utils.StringUtil;
import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Session;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T> {


    protected final Db db;
    protected final DataSource ds;
    protected String tableName;
    protected List<String> tableFields;
    protected List<String> ignoredFields;
    protected String tableKey;
    protected Field keyField;
    protected Class<T> tClass;

    private boolean keyFeature = true;

    public BaseDao(DataSource ds) {
        db = Db.use(ds);
        ignoredFields = new ArrayList<>();
        this.ds = ds;
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        try {
            Class<T> c = (Class<T>) p.getActualTypeArguments()[0];
            tClass = c;
            Field[] fields = ReflectUtil.getFields(c);
            TableName table_name = c.getAnnotation(TableName.class);
            if (table_name != null) {
                tableName = table_name.value();
            } else {
                Main.logger.warn("类:{}没有设置表名,默认用类名作为表名",c.getCanonicalName());
                tableName = c.getCanonicalName();
            }
            tableFields = new ArrayList<>(fields.length);
            boolean noKey = true;
            for (Field field : fields) {
                String fieldName = StringUtil.camelCaseToSnakeCase(field.getName());
                TableId tableId = field.getAnnotation(TableId.class);
                if (tableId != null) {
                    noKey = false;
                    if (StrUtil.isEmptyIfStr(tableId.value())) {
                        tableKey = fieldName;
                    } else {
                        tableKey = tableId.value();
                    }
                    keyField = field;
                    tableFields.add(tableKey);
                    continue;
                }
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField == null) {
                    tableFields.add(fieldName);
                } else {
                    if (!StrUtil.isEmptyIfStr(tableField.value())) fieldName = tableField.value();
                    if (tableField.exist()) {
                        tableFields.add(fieldName);
                    } else {
                        ignoredFields.add(fieldName);
                    }
                }
            }
            if(noKey){
                keyFeature = false;
                Main.logger.warn("表:{}不存在主键,插入和更新功能不可用",tableName);
            }
        } catch (Exception e) {
            Main.logger.error("DAO反射出错:", e);
        }
    }
    public int insert(T T) {
        Session session = getSession();
        int res = 0;
        try {
            session.beginTransaction();
            CopyOptions copyOptions = CopyOptions.create()
            .setIgnoreProperties(ignoredFields.toArray(new String[0]));
            T _T = tClass.newInstance();
            BeanUtil.copyProperties(T, _T, copyOptions);
            res = session.insert(Entity.create(tableName).parseBean(_T, true, true));
            session.commit();
        } catch (SQLException e) {
            Main.logger.error("SQL出错:", e);
            session.quietRollback();
        } catch (InstantiationException | IllegalAccessException e) {
            Main.logger.error("SQL注入出错:", e);
        } finally {
            session.close();
        }
        return res;
    }

    public int updateById(T T) {
        if(!keyFeature){
            throw new RuntimeException("不存在主键的表无法使用");
        }
        BeanDesc desc = BeanUtil.getBeanDesc(T.getClass());
        Session session = getSession();
        Entity where = Entity.create(tableName).set(tableKey, desc.getProp(keyField.getName()).getValue(T));
        int res = 0;
        try {
            session.beginTransaction();
            res = session.update(
                    Entity.create(tableName).parseBean(T, true, false),
                    where);
        } catch (SQLException e) {
            Main.logger.error("SQL出错:", e);
            session.quietRollback();
        } finally {
            session.close();
        }
        return res;
    }

    public int deleteById(T T) {
        if(!keyFeature){
            throw new RuntimeException("不存在主键的表无法使用");
        }
        BeanDesc desc = BeanUtil.getBeanDesc(T.getClass());
        Session session = getSession();
        Entity where = Entity.create(tableName).set(tableKey, desc.getProp(keyField.getName()).getValue(T));
        int res = 0;
        try {
            session.beginTransaction();
            res = session.del(where);
        } catch (SQLException e) {
            Main.logger.error("SQL出错:", e);
            session.quietRollback();
        } finally {
            session.close();
        }
        return res;
    }

    public List<T> select(Entity where) {
        try (Session session = getSession()) {
            List<Entity> entities = session.find(where);
            return BeanUtil.copyToList(entities, tClass);
        } catch (SQLException e) {
            Main.logger.error("SQL出错:", e);
        }
        return null;
    }

    public T selectById(Serializable id) {
        if(!keyFeature){
            throw new RuntimeException("不存在主键的表无法使用");
        }
        Entity where = Entity.create(tableName).set(tableKey, id);
        try (Session session = getSession()) {
            List<Entity> entities = session.find(where);
            if(entities==null|| entities.isEmpty()) return null;
            return entities.get(0).toBeanIgnoreCase(tClass);
        } catch (SQLException e) {
            Main.logger.error("SQL异常:", e);
        }
        return null;
    }

    private Session getSession() {
        return Session.create(ds);
    }

}
