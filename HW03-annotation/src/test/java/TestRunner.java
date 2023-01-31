import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import static homework.reflection.ReflectionHelper.*;

public class TestRunner {

    public static void run(String className) throws ClassNotFoundException {
        (new TestRunner()).test(className);
    }

    private void test(String className) throws ClassNotFoundException {
        var clazz = Class.forName(className);
        var before = getAnnotationMethods(clazz, Before.class);
        var test = getAnnotationMethods(clazz, Test.class);
        var after = getAnnotationMethods(clazz, After.class);
        var pass = 0;
        var fail = 0;

        for (var testMethod : test) {
            Object newObject = getInstance(clazz);
            try {
                for (var beforeMethod : before) {
                    callMethod(newObject, beforeMethod);
                }
                callMethod(newObject, testMethod);
                pass++;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                fail++;
            } finally {
                try {
                    for (var afterMethod : after) {
                        callMethod(newObject, afterMethod);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.printf("Tests total: %s, Passed: %s, Failed: %s%n", pass + fail, pass, fail);
    }
}
