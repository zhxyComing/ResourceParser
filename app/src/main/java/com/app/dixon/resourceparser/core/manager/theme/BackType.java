package com.app.dixon.resourceparser.core.manager.theme;

/**
 * Created by dixon.xu on 2019/2/18.
 * <p>
 * 背景Type
 */

public enum BackType {

    LEFT(90, 75),
    RIGHT(75, 90);

    // 定义一个 private 修饰的实例变量
    private int left;
    private int right;

    // 定义一个带参数的构造器，枚举类的构造器只能使用 private 修饰
    BackType(int left, int right) {
        this.left = left;
        this.right = right;
    }

    // 定义 get set 方法

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
