package com.example.summary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.summary.entity.Book;
import com.example.summary.entity.User;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class LambdaControllerTest {
    List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

    @Test
    public void LambdaTest() {
        names.sort(String::compareTo);
    }
}