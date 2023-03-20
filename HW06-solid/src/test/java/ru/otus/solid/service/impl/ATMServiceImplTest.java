package ru.otus.solid.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.exception.ATMException;
import ru.otus.solid.model.ATM;
import ru.otus.solid.model.CashBox;
import ru.otus.solid.repository.impl.CashBoxRepositoryImpl;
import ru.otus.solid.service.ATMService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.solid.model.Nominal.*;

class ATMServiceImplTest {

    private ATMService atmService;

    @BeforeEach
    public void init() {
        var cashBoxList = new ArrayList<CashBox>();
        cashBoxList.add(new CashBox(RU_5000, 1));
        cashBoxList.add(new CashBox(RU_2000, 1));
        cashBoxList.add(new CashBox(RU_1000, 1));
        cashBoxList.add(new CashBox(RU_500, 1));
        cashBoxList.add(new CashBox(RU_200, 1));
        cashBoxList.add(new CashBox(RU_100, 1));

        var atm = new ATM(cashBoxList);
        var cashBoxRepository = new CashBoxRepositoryImpl(atm);
        atmService = new ATMServiceImpl(cashBoxRepository);
    }

    @DisplayName("Внести наличные")
    @Test
    public void putCash() {
        var cashBoxes = new ArrayList<CashBox>();
        cashBoxes.add(new CashBox(RU_100, 1));
        cashBoxes.add(new CashBox(RU_200, 1));
        cashBoxes.add(new CashBox(RU_500, 1));
        cashBoxes.add(new CashBox(RU_1000, 1));
        cashBoxes.add(new CashBox(RU_2000, 1));
        cashBoxes.add(new CashBox(RU_5000, 1));

        atmService.putCash(cashBoxes);
        assertEquals(17600, getSum(atmService.getAvailableCash()));
    }

    @DisplayName("Получение наличных")
    @Test
    public void getCash() {
        var sum = 5000;

        atmService.getCash(sum);
        assertEquals(3800, getSum(atmService.getAvailableCash()));
    }

    @DisplayName("Получить информацию о остатке наличных в банкомате")
    @Test
    public void getAvailableCash() {
        var availableCashList = atmService.getAvailableCash();

        assertEquals(6, availableCashList.size());
        assertEquals(8800, getSum(availableCashList));
    }

    @DisplayName("Получить деньги - неуспешный сценарий")
    @Test
    public void getCashWithException() {
        var sum = 9000;
        assertThrows(ATMException.class, () -> atmService.getCash(sum));
    }

    private int getSum(List<CashBox> cashBoxList) {
        return cashBoxList.stream()
                .mapToInt(cb -> cb.getNominal().getValue() * cb.getCount())
                .sum();
    }

}