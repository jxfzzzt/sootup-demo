package example;


import cn.hutool.core.util.StrUtil;

public class Test {
    private Long id;

    private String name;

    public void testFuzz(int n) {
        if (n % 15 == 0) System.out.println("Hello %15");
        else System.out.println("You cannot divide 15");
    }

    public String strify(String str) {
        return StrUtil.format("StrUtil format = {}", str);
    }
}
