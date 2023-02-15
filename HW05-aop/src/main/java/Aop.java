import ru.testlog.Calculator;
import ru.testlog.CalculatorImpl;
import ru.testlog.Ioc;

public class Aop {

    public static void main(String[] args) {
        Calculator calculator = Ioc.createClass(new CalculatorImpl());
        calculator.calculation(1);
        calculator.calculation(1, 2);
        calculator.calculation(1, 2, 3);
    }
}
