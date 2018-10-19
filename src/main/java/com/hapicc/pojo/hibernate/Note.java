package com.hapicc.pojo.hibernate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

// 标注该类为实体类，将映射到指定的数据库表
@Entity
// 标注与该实体类对应的数据库表
@Table(name = "note")
// 标注自动填充 @CreatedDate 与 @LastModifiedDate 标注的属性字段
// 需要在 @SpringBootApplication 注解的启动类添加 @EnableJpaAuditing 注解
@EntityListeners({ AuditingEntityListener.class })
@JsonIgnoreProperties(value = { "version", "testTransient" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note implements Serializable {

    // 标注该属性映射为数据库表的主键
    @Id
    // 与 @Id 一同使用，标注主键的生成策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 标注该属性字段为表示实体版本的乐观锁值
    @Version
    // 标注该属性字段不允许为 null
    @Column(nullable = false)
    private Long version;

    // 标注该属性不允许为空或者 null，不限制表字段，用于 @Valid
    @NotBlank
    // 标注与该属性对应的表字段长度（仅对 String 有效，用于指定 VARCHAR 的长度），不允许为 null
    @Column(length = 128, nullable = false)
    private String title;

    // 标注该属性字段的读取策略以及不允许为 null
    @Basic(fetch = FetchType.EAGER, optional = false)
    // 标注与该属性对应的表字段长度（仅对 String 有效，用于指定 VARCHAR 的长度）
    @Column(length = 1024)
    private String content;

    // 标注该属性用于表示实体创建时间
    @CreatedDate
    // 标注与该属性对应的表字段为 DATETIME 类型
    @Temporal(TemporalType.TIMESTAMP)
    // 标注与该属性对应的表字段名称，不允许为 null，不允许更新
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;

    // 标注该属性用于表示实体最近更新时间
    @LastModifiedDate
    // 标注与该属性对应的表字段为 DATETIME 类型
    @Temporal(TemporalType.TIMESTAMP)
    // 标注与该属性对应的表字段名称，不允许为 null
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date updatedAt;

    // 标注该属性不映射到表字段，JPA 将忽略该属性
    @Transient
    private String testTransient;

    public Note() {
    }

    public Note(Long id, String title, Date createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTestTransient() {
        return testTransient;
    }

    public void setTestTransient(String testTransient) {
        this.testTransient = testTransient;
    }
}
