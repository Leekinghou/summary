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


class StreamControllerTest {
    static List<User> userList = Arrays.asList(
        new User(1, "a", "小明"),
        new User(2, "b", "小王"),
        new User(3, "b", "小红")
    );

    static List<Book> bookList = Arrays.asList(
            new Book(200, "大冰", "摸摸头", 32.80),
            new Book(110, "大冰", "好吗，好的", 39.50),
            new Book(41, "天下霸唱", "鬼吹灯", 88.20),
            new Book(99, "番茄", "盘龙", 59.60)
    );

    @Test
    public void StreamTest() {
        // 创建stream流的方式
        ArrayList<String> list = new ArrayList<>();

        // 1、通过Collection系列提供的stream()（串行） 或parallelStream()（并行）获取
        Stream<String> stream1 = list.stream(); // 串型流
        Stream<String> stream2 = list.parallelStream();// 并行流

        // 2、通过Arrays中的静态方法stream() 获取数据流，接收参数为数组类型，将数组转成流
        User[] users = new User[2];
        Stream<User> stream3 = Arrays.stream(users);

        // 3、通过Stream类中的静态方法of()
        Stream<String> stream4 = Stream.of("11", "2");

        // 4、使用 Pattern.splitAsStream() 方法，将字符串分隔成流
        Pattern pattern = Pattern.compile(",");
        Stream<String> stringStream = pattern.splitAsStream("a,b,c,d");
        stringStream.forEach(System.out::println);
    }

    @Test
    public void UserStreamTest() {
        // 根据类型分组
        Map<String, List<User>> groupBy = userList.stream().collect(Collectors.groupingBy(User::getType));
        System.out.println(groupBy);

        // 注：返回结果的map的key表示分组的属性，上面例子的分组为type，类型为String，所以Map的key属性也为String，
        // value为集合的属性，因为这里是一个User集合的流，所以value值为list

        // 分组个数
        // 只需要在groupingBy方法里加多一个Collectors.counting()即可求得
        Map<String, Long> groupByCount = userList.stream().collect(Collectors.groupingBy(User::getType, Collectors.counting()));
        System.out.println(groupByCount);

        /**
         * 多级分组
         * 多级分组是先将stream流分组，然后再对分组后的数据再进行分组
         */
        Map<String, Map<String, List<User>>> collectGroup = userList.stream().collect(Collectors.groupingBy(User::getType,
                Collectors.groupingBy(User::getName)));
        System.out.println(collectGroup);

        /**
         * Stream转换List
         * 很多时候，我们对Stream操作之后，返回的还是Stream流，
         * 很多时候，我们想返回的List或者其他类型的对象，我们就需要对Stream流进行终止操作
         */
        String[] testStrings = { "a", "b", "c", "d" };
        List<String> list1 = Stream.of(testStrings).collect(toList());
        System.out.println(list1);


        /**
         * Stream转换Set去重
         */
        String[] testString2 = {"a", "b", "c", "d", "d"};
        Set<String> set = Stream.of(testString2).collect(Collectors.toSet());
        System.out.println(set);

        // 对结果进行简单分组
        Map<String, String> collect =  userList.stream().collect(Collectors.toMap(User::getType, User::getName, (o, n) -> o + "," + n));
        System.out.println(collect);
    }

    /**
     * Stream转换Map
     * Stream 转换为 Map，使用.collect(Collectors.toMap())即可。
     */
    @Test
    public void StreamToMapTest() {
        // Collectors.toMap 有三个重载方法
        /**
         * 参数含义分别是：
         * keyMapper：Key 的映射函数
         * valueMapper：Value 的映射函数
         * mergeFunction：当 Key 冲突时，调用的合并方法
         * mapSupplier：Map 构造器，在需要返回特定的 Map 时使用
         * 三种重载方法，所以就有三种不同的用法
         */
        Map<Integer, Book> map = new HashMap<>(3);
        for(Book book: bookList){
            map.put(book.getId(), book);
        }
        System.out.println(JSONObject.toJSONString(map));


        // 1、只指定key和value的映射
        // 如果对于转换结果要求是线程安全/有序的，那么使用Collectors.toConCurrentMap更加高效
//        Map<Integer, Book> collect1 = BookList.stream().collect(Collectors.toMap(Book::getId, book -> book));
        Map<Integer, Book> collect1 = bookList.stream().collect(Collectors.toConcurrentMap(Book::getId, book -> book));
        System.out.println(JSON.toJSONString(collect1));

        // 也可以指定结构体的某个属性为value值
        Map<Integer, String> collect2 = bookList.stream().collect(Collectors.toMap(Book::getId, Book::getName));
        System.out.println(collect2);

        // 一般而言都是取对象中的id作为key，整个对象作为value，因此java8还提供一个函数指代本身
        Map<Integer, Book> collect3 = bookList.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        System.out.println(collect3);

        // 2、使用第二个重载方法，需要传多一个参数
        // mergeFunction: 需要传入合并函数（用于当发生key冲突时，如何处理，即插入一个key已经存在的数据时的处理）
        // 如果在遇到 key 冲突时，将旧值丢弃，存入新值，就可以这样写：
        Map<String, Book> collect4 = bookList.stream().collect(Collectors.toMap(Book::getAuthor, Function.identity(),
                (oldValue, newValue) -> newValue));
        System.out.println(JSON.toJSONString(collect4));

        // 但是当 list 中数据顺序变化时，得到的结果不一致，那有啥办法可以控制每次返回的结果都可控呢
        // 我们可以定一个规则：id 大的值留下，id 小的值舍弃
        Map<String, Book> collect5 = bookList.stream().collect(Collectors.toMap(Book::getAuthor, Function.identity(),
                (oldValue, newValue) -> newValue.getId() > oldValue.getId() ? newValue: oldValue));
        System.out.println(JSON.toJSONString(collect5));
        // 试验发现，无论怎么改动 list 中数据顺序，输出的结果都是相同的。

        // 当然效果是达到了，但是代码着实有点丑
        // 长长的三元表达式 (oldValue, newValue) -> newValue.getId() > oldValue.getId() ? newValue : oldValue
        // 很不利于程序维护。
        Map<String, Book> collect = bookList.stream().collect(Collectors.toMap(Book::getAuthor, Function.identity(),
                BinaryOperator.maxBy(Comparator.comparingInt(Book::getId))));
        System.out.println(JSON.toJSONString(collect));
        // 在项目实际使用时，非常建议重写这个 merge 方法，因为很难从数据角度控制 key 不重复，
        // 已定义 merge 方法可以增加程序健壮性，避免非必要的程序异常。

        // 3、调用第三个重载方法，需要传4个参数，即在合并函数之后，再传入一个函数，用于自定义返回 Map的 类型
        // 如果要得到 TreeMap 的结构，比如想得到 key 以 id 排序的 Map 结构：
        Map<Integer, Book> collect6 = bookList.stream().collect(Collectors.toMap(Book::getId, book -> book,
                (o, n) -> o, TreeMap::new));
        // 可以看到，返回的结果是通过 id 排序的，而实际代码里我们并没有写排序逻辑。
        // TreeMap中的元素默认按照keys的自然排序排列
        //（对Integer来说，其自然排序就是数字的升序；对String来说，其自然排序就是按照字母表排序）
        System.out.println(collect6);

        // 每位作者著作的价格总和sum/最大值max/最小值min
        Map<String, Double> collect7 = bookList.stream().collect(Collectors.toMap(Book::getAuthor, Book::getPrice, Double::sum));
        System.out.println(JSON.toJSONString(collect7));
    }

    // stream可以做字符串拼接
    // public static Collector<CharSequence, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)


    @Test
    public void StreamSortedTest() {
        // sorted()，产生一个新流，其中按自然顺序排序
        // sorted(Comparator)，产生一个新流，其中按Comparator比较器顺序排序
        // 注：如果是自定义的对象使用sorted()方法，需要实现Comparable 接口，基本类型可以直接使用
        // int compareTo(T t): 返回:负整数、零或正整数。如果该对象小于、等于或大于指定对象，则分别返回负整数、零或正整数。
        List<User> collect1 = userList.stream()
                                        .sorted(Comparator.comparing(User::getType).reversed())
                                        .collect(toList());
        System.out.println(JSONObject.toJSONString(collect1));
        // 注：comparing方法是Comparator内部实现的一个方法，我们可以传入某个属性，然后会替我们生成一个该属性的升序Comparator比较器
    }



    @Test
    public void CommonTest(){
        // 求总数
        // 求一个stream的长度，使用.collect(Collectors.counting()即可
        Long collect = userList.stream().collect(Collectors.counting());
        System.out.println(collect);

        // 循环遍历，使用forEach
        userList.stream().forEach(System.out::println);

        // 取最大值
        User user = userList.stream().max(Comparator.comparing(User::getId)).get();
        System.out.println(JSONObject.toJSONString(user));

        Integer max = userList.stream().map(User::getId).max(Integer::compare).get();
        System.out.println(max);

        Integer min = userList.stream().map(User::getId).min(Integer::compare).get();
        System.out.println(min);

        // 取平均值
        // 取平均值有averagingInt，averagingLong，averagingDouble三种方法，只是类型不一样，返回的都是Double
        // public static <T> Collector<T, ?, Double> averagingInt(ToIntFunction<? super T> mapper)
    }

    /**
     * 筛选
     * stream流的筛选方法为filter(predicate)
     * filter(predicate)-接收lambda，从流中排除某些元素，保留符合条件的元素
     * 该方法接受一个谓词predicate方法(返回Boolean的函数)作为参数
     */
    @Test
    public void StreamFilterTest() {
        List<Book> collect = bookList.stream().filter(x -> x.getId() > 1).collect(toList());
        System.out.println(JSONObject.toJSONString(collect));
    }

    /**
     * 去重
     * stream流的筛选方法为distinct()，它会根据元素的hashcode()和equals()去除重复的元素
     */
    @Test
    public void StreamDistinctTest() {
        List<String> stringList = Arrays.asList("a", "b", "c","a");
        stringList.stream().distinct().forEach(System.out::println);
    }

    /**
     * 截断流limit
     * limit(n)-截断流，使其元素不超过给定数量
     * limit()传入的是一个Long类型的整数
     */
    @Test
    public void StreamLimitTest() {
        List<String> stringList = Arrays.asList("a", "b", "c","a");
        stringList.stream().limit(2L).forEach(System.out::println);
        // 使用了这个limit方法后，stream流就会只剩下2个元素
    }

    /**
     * 映射
     * Stream中包含5个映射方法：map、mapToDouble、mapToInt、mapToLong和flatMap。
     * map，接收Lambda，将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每一个元素上，并将其映射成一个新的元素。
     * map最常用的操作就是取出元素中的某个属性，形成一个新的流
     */
    @Test
    public void StreamMapTest() {
        // 将Uer集合中的Name属性全部取出来形成一个新的流，再转换成集合
        List<String> nameList = userList.stream().map(User::getName).collect(toList());
        System.out.println(nameList);
        // mapToDouble/mapToInt/mapToLong，接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新DoubleStream/IntStream/LongStream。
    }

    /**
     * 匹配
     * anyMatch是否有一个元素匹配条件，返回的是一个boolean类型元素
     */
    @Test
    public void StreamMatchTest() {
        boolean anyMatch = userList.stream().anyMatch(x -> x.getType().equals("b"));
        System.out.println(anyMatch);
    }
    // allMatch,检查是否匹配所有元素，需要stream流的所有元素都匹配条件才返回true
    // noneMatch,检查是否没有匹配所有元素。
}