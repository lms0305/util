package com.gpdi.plugins.util;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @autor liumaosheng
 * @date 2021/12/10 17:18
 */
public class AnnotationUtil {

    /**
     * 动态给Excel注解赋值
     * 调用示例：
     * setExcelAnnotationValue(TestController.Student1.class,"photo","savePath","C:\\Users\\17194\\Desktop\\test\\test111111");
     * @param clazz
     * @param fieldName
     * @param annotationName
     * @param annotationNewValue
     * @param <T>
     */
    public static void setExcelAnnotationValue(Class<?> clazz, String fieldName, String annotationName, String annotationNewValue){
        try {
            Field photo = clazz.getDeclaredField(fieldName);
            Excel excel = photo.getAnnotation(Excel.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(excel);
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map map = (Map) field.get(invocationHandler);
            map.put(annotationName,annotationNewValue);
            System.out.println(map);
            System.out.println(excel.savePath());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
