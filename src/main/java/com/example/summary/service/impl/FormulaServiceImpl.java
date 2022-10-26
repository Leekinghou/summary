package com.example.summary.service.impl;

import com.example.summary.service.FormulaService;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/10/26 09:37
 */

public class FormulaServiceImpl implements FormulaService {

    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
}
