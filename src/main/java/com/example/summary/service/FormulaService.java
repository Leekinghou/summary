package com.example.summary.service;

import org.springframework.stereotype.Service;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/10/26 09:35
 */

// 定义一个公式接口
@Service
@FunctionalInterface
public interface FormulaService {
    // 计算
    double calculate(int a);

    /**
     * 补充：通过 default 关键字这个新特性，可以非常方便地对之前的接口做拓展，而此接口的实现类不必做任何改动。
     * @param a
     * @return
     */
    // 求平方根
    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
/**
 * 只有一个抽象方法的接口被称为函数式接口（Functional Interface）
 * default关键字修饰的方法不算抽象方法
 * 因此可以毫无顾忌的添加默认方法，它并不违反函数式接口（Functional Interface）的定义。
 *
 * 只要接口中仅仅包含一个抽象方法，我们就可以将其改写为 Lambda 表达式。
 * 为了保证一个接口明确的被定义为一个函数式接口（Functional Interface），
 * 我们需要为该接口添加注解：@FunctionalInterface。这样，一旦你添加了第二个抽象方法，编译器会立刻抛出错误提示。
 */
