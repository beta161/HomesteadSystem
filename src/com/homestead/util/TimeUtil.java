package com.homestead.util;

import com.sun.xml.internal.ws.spi.db.DatabindingException;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    //时间工具类：计算审批截止时间
    public static Date calculateDeadline(Date startTime,int level){
        //level：村级=1（7天），2=乡镇（15天）
        //工具类方法通常设为静态，无需创建TU对象，方法执行后返回一个Date类型时间
        Calendar cal = Calendar.getInstance();//实例化
        cal.setTime(startTime);//开始时间赋值
        if (level == 1){
            cal.add(Calendar.DAY_OF_MONTH,7);//DOM一个月中的第几天
        }else if (level == 2){
            cal.add(Calendar.DAY_OF_MONTH,15);
        }
        return cal.getTime();
    }
    //判断是否超时
    public static boolean isOverdue(Date deadline){
        return new Date().after(deadline);//当前系统时间是否在截止时间之后
    }
}
