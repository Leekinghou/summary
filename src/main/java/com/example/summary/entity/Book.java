package com.example.summary.entity;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/10/25 16:31
 */

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    /**
     * 序号
     */
    private Integer id;
    /**
     * 作者
     */
    private String author;
    /**
     * 书名
     */
    private String name;
    /**
     * 价格
     */
    private double price;
}
