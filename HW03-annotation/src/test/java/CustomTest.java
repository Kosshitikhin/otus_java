import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.util.Random;

public class CustomTest {

    @Before
    void before() {
        System.out.println("Custom before");
    }

    @Test
    void test() {
        System.out.println("Custom test. Random value:" + new Random().nextInt());
    }

    @After
    void after() {
        System.out.println("Custom after");
    }
}
