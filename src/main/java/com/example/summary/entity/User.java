package com.example.summary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/10/25 13:38
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    private String type;

    private String name;

}
