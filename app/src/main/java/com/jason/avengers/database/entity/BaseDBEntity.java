package com.jason.avengers.database.entity;

import com.jason.avengers.database.json.JsonDBEntity;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;


/**
 * Created by jason on 2018/7/24.
 *
 * @Entity：这个对象需要持久化。
 * @Id：这个对象的主键。
 * @Index：这个对象中的索引。对经常大量进行查询的字段创建索引，会提高你的查询性能。
 * @NameInDb：有的时候数据库中的字段跟你的对象字段不匹配的时候，可以使用此注解。
 * @Transient:如果你有某个字段不想被持久化，可以使用此注解。
 * @Relation:做一对多，多对一的注解。
 * @Uid:修改表或字段使用<br/>
 * <p>
 * PS:需要注意的是：默认情况下，id是会被objectbox管理的，也就是自增id，
 * 如果你想手动管理id需要在注解的时候加上@Id(assignable = true)即可。
 * 当你在自己管理id的时候如果超过long的最大值，objectbox 会报错。
 * id=0的表示此对象未被持久化，id的值不能为负数。
 */

@BaseEntity
public abstract class BaseDBEntity extends JsonDBEntity {

    @Id
    @NameInDb("_ID")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
