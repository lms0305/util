package com.gpdi.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @description 生成树形结构（优化）
 * @autor liums
 * Created by 17194 on 2020/12/24 15:31.
 */
public class TreeUtil2 {

    private TreeUtil2(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * 返回树型结构
     * @param firstLevelPid 第一层的父Id
     * @param list
     * @param getIdFun
     * @param getPidFun
     * @param setChildrenFun
     * @param <T>
     * @return
     */
    public static <T> List<T> convertTree(Object firstLevelPid, List<T> list, Function<T,Object> getIdFun, Function<T,Object> getPidFun, BiConsumer<T, List<T>> setChildrenFun){
        Map<String, List<T>> groupByPidMap = groupByPid(list, getPidFun);
        List<T> res = searchByPid(groupByPidMap, firstLevelPid,getPidFun);
        setChildren(groupByPidMap,res,getIdFun,getPidFun,setChildrenFun,null,1);
        return res;
    }

    /**
     * 返回树型结构
     * @param firstLevelPid 第一层的父Id
     * @param list
     * @param getIdFun
     * @param getPidFun
     * @param setChildrenFun
     * @param setTypeFun 层级赋值，必须是int类型的
     * @param <T>
     * @return
     */
    public static <T> List<T> convertTree(Object firstLevelPid, List<T> list, Function<T,Object> getIdFun, Function<T,Object> getPidFun, BiConsumer<T, List<T>> setChildrenFun,BiConsumer<T, Integer> setTypeFun){
        Map<String, List<T>> groupByPidMap = groupByPid(list, getPidFun);
        List<T> res = searchByPid(groupByPidMap, firstLevelPid,getPidFun);
        setChildren(groupByPidMap,res,getIdFun,getPidFun,setChildrenFun,setTypeFun,1);
        return res;
    }

    private static <T> Map<String,List<T>> groupByPid(List<T> list,Function<T,Object> getPidFun){
        Map<String,List<T>> res = new HashMap<>();
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                String key = ""+getPidFun.apply(t);
                List<T> value = res.get(key);
                if(value!=null){
                    value.add(t);
                }else {
                    value = new ArrayList<>();
                    value.add(t);
                    res.put(key,value);
                }
            }
        }
        return res;
    }

    /**
     * 递归给子项的子项的...children 赋值
     * @param <T>
     * @param groupByPidMap
     * @param childrenList
     * @param getIdFun
     * @param getPidFun
     * @param setChildrenFun
     * @param setTypeFun 层级方法
     * @param type 层级初始化是1
     */
    private static <T> void setChildren(Map<String, List<T>> groupByPidMap, List<T> childrenList, Function<T,Object> getIdFun, Function<T,Object> getPidFun, BiConsumer<T, List<T>> setChildrenFun,BiConsumer<T, Integer> setTypeFun,Integer type){
        if(childrenList==null || childrenList.size()<=0){
            return;
        }
        for (int i = 0; i < childrenList.size(); i++) {
            T t = childrenList.get(i);
            List<T> children = searchByPid(groupByPidMap, getIdFun.apply(t), getPidFun);
            setChildrenFun.accept(t,children);
            if(setTypeFun!=null){
                setTypeFun.accept(t,type);
            }
            setChildren(groupByPidMap, children, getIdFun, getPidFun, setChildrenFun,setTypeFun,type+1);
        }
    }

    /**
     * 通过父Id查询其子项
     * @param groupByPidMap
     * @param pid
     * @param getPid
     * @param <T>
     * @return
     */
    private static <T> List<T> searchByPid(Map<String, List<T>> groupByPidMap, Object pid, Function<T,Object> getPid){
        List<T> res = new ArrayList<>();
        if(groupByPidMap==null || groupByPidMap.size()<=0){
            return res;
        }
        return groupByPidMap.get(""+pid);
    }
}
