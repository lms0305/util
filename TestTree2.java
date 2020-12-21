package com.gpdi.test;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @description
 * @autor liums
 * Created by 17194 on 2020/12/21 11:05.
 */
public class TestTree2 {

    @Data
    public static class Menu{
        public Menu(Integer id, String name, Integer pid) {
            this.id = id;
            this.name = name;
            this.pid = pid;
        }

        public Menu() {
        }

        private Integer id;
        private String name;
        private Integer pid;
        private List<Menu> children;

        @Override
        public String toString() {
            return "Menu{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", pid=" + pid +
                    ", children=" + children +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Menu> list = new ArrayList<>();
        initData(list);
        List<Menu> trees = tree(list, Menu::getId, Menu::getPid, Menu::setChildren);
        System.out.println(JSON.toJSONString(trees));
    }

    /**
     * 返回树型结构
     * @param list
     * @param getIdFun
     * @param getPidFun
     * @param setChildrenFun
     * @param <T>
     * @return
     */
    public static <T> List<T> tree(List<T> list,Function<T,Object> getIdFun,Function<T,Object> getPidFun,BiConsumer<T, List<T>> setChildrenFun){
        List<T> res = searchByPid(list, -1,getPidFun);
        setChildren(list,res,getIdFun,getPidFun,setChildrenFun);
        return res;
    }

    /**
     * 递归给子项的子项的...children 赋值
     * @param <T>
     * @param list
     * @param childrenList
     * @param getIdFun
     * @param getPidFun
     * @param setChildrenFun
     */
    private static <T> void setChildren(List<T> list, List<T> childrenList, Function<T,Object> getIdFun, Function<T,Object> getPidFun, BiConsumer<T, List<T>> setChildrenFun){
        if(childrenList==null || childrenList.size()<=0){
            return;
        }
        for (int i = 0; i < childrenList.size(); i++) {
            T t = childrenList.get(i);
            List<T> children = searchByPid(list, getIdFun.apply(t), getPidFun);
            setChildrenFun.accept(t,children);
            setChildren(list, children, getIdFun, getPidFun, setChildrenFun);
        }
    }

    /**
     * 通过父Id查询其子项
     * @param list
     * @param pid
     * @param getPid
     * @param <T>
     * @return
     */
    private static <T> List<T> searchByPid(List<T> list, Object pid, Function<T,Object> getPid){
        List<T> res = new ArrayList<>();
        if(list==null || list.size()<=0){
            return res;
        }
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            @Nullable Object apply = ""+getPid.apply(next);
            pid = ""+pid;
            if(apply.equals(pid)){
                res.add(next);
                iterator.remove();
            }
        }
        return res;
    }

    /**
     * 初始化数据
     * @param list
     */
    private static void initData(List<Menu> list) {
        list.add(new Menu(1,"菜单1",-1));
        list.add(new Menu(2,"菜单2",-1));
        list.add(new Menu(3,"菜单3",-1));
        list.add(new Menu(4,"菜单4",-1));
        for (int i = 5; i <= 10; i++) {
            list.add(new Menu(i,"菜单1-子项"+i,1));
        }
        for (int i = 11; i <= 20; i++) {
            list.add(new Menu(i,"菜单2-子项"+i,2));
        }
        for (int i = 21; i <= 40; i++) {
            list.add(new Menu(i,"菜单3-子项"+i,3));
        }
        for (int i = 40; i <= 60; i++) {
            list.add(new Menu(i,"菜单4-子项"+i,4));
        }
        for (int i = 61; i <= 100; i++) {
            list.add(new Menu(i,"菜单1-子项-5-子项"+i,5));
        }
//        System.out.println(list.toString());
    }
}
