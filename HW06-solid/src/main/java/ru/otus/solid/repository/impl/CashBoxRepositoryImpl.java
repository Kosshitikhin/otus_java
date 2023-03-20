package ru.otus.solid.repository.impl;

import lombok.AllArgsConstructor;
import ru.otus.solid.exception.ATMException;
import ru.otus.solid.model.ATM;
import ru.otus.solid.model.CashBox;
import ru.otus.solid.repository.CashBoxRepository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CashBoxRepositoryImpl implements CashBoxRepository {

    private final ATM atm;

    @Override
    public void addCashToCashBox(CashBox cashBox) {
        if (cashBox.getCount() < 0) {
            throw new IllegalArgumentException("Количество добавляемых купюр не может быть отрицательным");
        }

        var atmCashBox = checkAndGetCashBox(cashBox);
        atmCashBox.setCount(atmCashBox.getCount() + cashBox.getCount());
}

    @Override
    public void removeCashFromCashBox(CashBox cashBox) {
        var atmCashBox = checkAndGetCashBox(cashBox);

        if (cashBox.getCount() > atmCashBox.getCount()) {
            throw ATMException.notEnough(String.format("Не достаточно купюр номиналом %s", cashBox.getNominal()));
        }
        atmCashBox.setCount(atmCashBox.getCount() - cashBox.getCount());
    }

    @Override
    public List<CashBox> getAvailableCash() {
        return atm.getCashBoxList();
    }

    @Override
    public List<CashBox> toCash(List<CashBox> cashBoxList, int sum) {
        List<CashBox> resultCashBoxList = new ArrayList<>();

        for (CashBox cashBox : cashBoxList) {
            var nominalValue = cashBox.getNominal().getValue();

            if (sum >= nominalValue) {
                var count = getCount(sum, nominalValue, cashBox.getCount());
                sum -= nominalValue * count;
                resultCashBoxList.add(new CashBox(cashBox.getNominal(), count));
            }
        }

        if (sum > 0) throw ATMException.convert("В источнике недостаточно денежных средств");

        return resultCashBoxList;
    }

    private static int getCount(int sum, int nominalValue, int sourceCashCount) {
        int count = sum / nominalValue;
        return sourceCashCount - count >= 0 ? count : sourceCashCount;
    }

    private CashBox checkAndGetCashBox(CashBox cashBox) {
        return atm.getCashBoxList().stream()
                .filter(cb -> cb.equals(cashBox))
                .findFirst().orElseThrow(() -> ATMException.notFound(String.format("Ячейка с номиналом %s не найдена", cashBox.getNominal())));
    }
}
