package com.gpdi.test;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @autor liums
 * Created by 17194 on 2020/12/21 15:15.
 */
public class TreeTest {

    @Data
    public static class Menu{
        public Menu(String id, String name, String pid) {
            this.id = id;
            this.name = name;
            this.pid = pid;
        }

        public Menu() {
        }

        private String id;
        private String name;
        private String pid;
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
        List<Menu> trees = TestTree2.tree(list, Menu::getId, Menu::getPid, Menu::setChildren);
        System.out.println(JSON.toJSONString(trees));
    }

    /**
     * 初始化数据
     * @param list
     */
    private static void initData(List<Menu> list) {
        list.add(new Menu(""+1,"str-菜单1",-1+""));
        list.add(new Menu(""+2,"str-菜单2",-1+""));
        list.add(new Menu(""+3,"str-菜单3",-1+""));
        list.add(new Menu(""+4,"str-菜单4",-1+""));
        for (int i = 5; i <= 10; i++) {
            list.add(new Menu(""+i,"str-菜单1-子项"+i,1+""));
        }
        for (int i = 11; i <= 20; i++) {
            list.add(new Menu(""+i,"str-菜单2-子项"+i,2+""));
        }
        for (int i = 21; i <= 40; i++) {
            list.add(new Menu(""+i,"str-菜单3-子项"+i,3+""));
        }
        for (int i = 40; i <= 60; i++) {
            list.add(new Menu(""+i,"str-菜单4-子项"+i,4+""));
        }
        for (int i = 61; i <= 100; i++) {
            list.add(new Menu(""+i,"str-菜单1-子项-5-子项"+i,5+""));
        }
//        System.out.println(list.toString());
    }
}
