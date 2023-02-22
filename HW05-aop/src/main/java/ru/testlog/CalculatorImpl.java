package ru.testlog;

import ru.testlog.annotations.Log;

public class CalculatorImpl implements Calculator {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("Calculation, param: " + param);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.printf("Calculation, param1: %s param2: %s%n", param1, param2);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, int param3) {
        System.out.printf("Calculation, param1: %s param2: %s param3: %s", param1, param2, param3);
    }
}
