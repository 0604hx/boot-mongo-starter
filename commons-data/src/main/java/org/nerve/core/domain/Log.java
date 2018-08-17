package org.nerve.core.domain;

import org.nerve.auth.Account;
import org.nerve.domain.ID;
import org.nerve.domain.IdEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "sys_log")
public class Log extends IdEntity {
    @Indexed
    public String uid;  //用户id
    @Indexed
    public String un;   //用户名
    public String oid;  //对象id
    @Indexed
    public String on;   //对象名称
    public Date d;
    @Indexed
    public int t;     //操作类型
    public String m;    //操作描述
    public String e;   //异常信息


    public String df;   //操作字段(Data Field)
    public String dv;   //Data Field 的值（未操作前）
    public String dc;   //操作后的Data Field的值（Data Current）

    Log(){}

    Log(ID entity){
        this();
        if(entity!=null){
            this.oid = entity.id();
            this.on = entity.getClass().getSimpleName();
        }
        this.d = new Date();
    }

    Log(ID entity,String  userId,String msg){
        this(entity);
        this.uid = userId;
        this.m = msg;
    }

    Log(ID entity,int _type,String msg){
        this(entity);
        this.t = _type;
        this.m = msg;
    }

    public Log(ID entity,int _type,String userId,String msg){
        this(entity,userId,msg);
        this.t = _type;
    }

    public Log(ID entity, int _type, Account account, String msg){
        this(entity,_type,msg);
        if(account!=null){
            this.uid = account.id();
            this.un = account.getName();
        }
    }

    /**
     * 记录数据字段的修改
     * @param field
     * @param oldValue
     * @param newValue
     * @return
     */
    public Log data(String field,String oldValue,String newValue){
        this.df = field;
        this.dc = newValue;
        this.dv = oldValue;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public Date getD() {
        return d;
    }

    public void setD(Date d) {
        this.d = d;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
