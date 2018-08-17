package org.nerve.enums;

/**
 * 通用的日志类型
 */
public enum LogType {

    //常用的类型
    DEFAULT(0),
    CREATE(1),
    UPDATE(2),
    DELETE(3),
    SYSTEM(4),
    ERROR(5),
    DATA(6),    //数据处理
    CLEAN(7),   //数据清洗
    EXPORT(8),


    //业务类型
    LOGIN(10),
    LOGOUT(11),
    UPLOAD(12),
    DOWNLOAD(13),
    BACKUP(14);


    private int n;

    LogType(int n){
        this.n = n;
    }

    @Override
    public String toString() {
        return Integer.toString(n);
    }

    public int value(){
        return n;
    }
}
