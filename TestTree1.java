package com.gpdi.test;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @description
 * @autor liums
 * Created by 17194 on 2020/12/21 11:05.
 */
public class TestTree1 {

    @Data
    public static class Menu{
        public Menu(int id, String name, int pid) {
            this.id = id;
            this.name = name;
            this.pid = pid;
        }

        public Menu() {
        }

        private int id;
        private String name;
        private int pid;
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
        List<Menu> menus = searchByPid(list, -1);
        setChildren(list, menus);
        System.out.println(JSON.toJSONString(menus));
    }

    /**
     * 递归给子项的子项的...children 赋值
     * @param list
     * @param childrenList
     */
    private static void setChildren(List<Menu> list, List<Menu> childrenList){
        if(childrenList==null || childrenList.size()<=0){
            return;
        }
        for (int i = 0; i < childrenList.size(); i++) {
            Menu menu = childrenList.get(i);
            List<Menu> children = searchByPid(list, menu.getId());
            menu.setChildren(children);
            setChildren(list, menu.getChildren());
        }
    }

    /**
     * 通过父Id查询其子项
     * @param list
     * @param pid
     * @return
     */
    private static List<Menu> searchByPid(List<Menu> list, int pid){
        List<Menu> res = new ArrayList<>();
        if(list==null || list.size()<=0){
            return res;
        }
        System.out.println(list.size());
        Iterator<Menu> iterator = list.iterator();
        while (iterator.hasNext()){
            Menu next = iterator.next();
            if(next.getPid() == pid){
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
        for (int i = 5; i <= 100; i++) {
            list.add(new Menu(i,"菜单1-子项"+i,1));
        }
        for (int i = 101; i <= 200; i++) {
            list.add(new Menu(i,"菜单2-子项"+i,2));
        }
        for (int i = 201; i <= 400; i++) {
            list.add(new Menu(i,"菜单3-子项"+i,3));
        }
        for (int i = 400; i <= 600; i++) {
            list.add(new Menu(i,"菜单4-子项"+i,4));
        }
        for (int i = 601; i <= 1000; i++) {
            list.add(new Menu(i,"菜单1-子项-5-子项"+i,5));
        }
//        System.out.println(list.toString());
    }
}
