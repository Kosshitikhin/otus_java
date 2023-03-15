package ru.otus.solid.repository.impl;

import lombok.Data;
import ru.otus.solid.exception.NotEnoughCashException;
import ru.otus.solid.exception.NotFoundException;
import ru.otus.solid.model.ATM;
import ru.otus.solid.model.CashBox;
import ru.otus.solid.repository.CashBoxRepository;

import java.util.List;

@Data
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
            throw new NotEnoughCashException(String.format("Не достаточно купюр номиналом %s", cashBox.getNominal()));
        }
        atmCashBox.setCount(atmCashBox.getCount() - cashBox.getCount());
    }

    @Override
    public List<CashBox> getAvailableCash() {
        return atm.getCashBoxList();
    }

    private CashBox checkAndGetCashBox(CashBox cashBox) {
        return atm.getCashBoxList().stream()
                .filter(cb -> cb.equals(cashBox))
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Ячейки с номиналом %s не найдена", cashBox.getNominal())));
    }
}
