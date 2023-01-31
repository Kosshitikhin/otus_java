import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class AnnotationTest {

    @Before
    void beforeOne() {
        System.out.println("beforeOne");
    }

    @Before
    void beforeTwo() {
        System.out.println("beforeTwo");
    }

    @Test
    void test() {
        System.out.println("Test value:" + Math.random() * 1000);
    }

    @After
    void afterOne() {
        System.out.println("afterOne");
    }

    @After
    void afterTwo() {
        System.out.println("afterTwo");
    }
}
