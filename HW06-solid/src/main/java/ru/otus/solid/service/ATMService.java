package ru.otus.solid.service;

import ru.otus.solid.model.CashBox;

import java.util.List;

public interface ATMService {

    void putCash(List<CashBox> cashBoxList);

    List<CashBox> getCash(int sum);

    List<CashBox> getAvailableCash();

}
