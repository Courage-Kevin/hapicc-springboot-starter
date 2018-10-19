package com.hapicc.pojo;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "sys_user")
@JsonIgnoreProperties(value = { "password" })
@JsonInclude(Include.NON_NULL)
public class SysUser {
    @Id
    private String id;

    private String img;

    private String name;

    @Column(name = "login_name")
    private String loginName;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private Byte gender;

    private String phone;

    @Column(name = "phone_verified")
    private Boolean phoneVerified;

    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "date_created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date dateCreated;

    @Column(name = "last_updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date lastUpdated;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return login_name
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return gender
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return phone_verified
     */
    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    /**
     * @param phoneVerified
     */
    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return email_verified
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * @param emailVerified
     */
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * @return date_created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return last_updated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}