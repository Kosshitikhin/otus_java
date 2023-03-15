package ru.otus.solid.repository;

import ru.otus.solid.model.CashBox;

import java.util.List;

public interface CashBoxRepository {

    void addCashToCashBox(CashBox cashBox);

    void removeCashFromCashBox(CashBox cashBox);

    List<CashBox> getAvailableCash();
}
