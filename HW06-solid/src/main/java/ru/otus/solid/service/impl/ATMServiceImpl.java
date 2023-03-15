package ru.otus.solid.service.impl;

import lombok.Data;
import ru.otus.solid.model.CashBox;
import ru.otus.solid.repository.CashBoxRepository;
import ru.otus.solid.service.ATMService;
import ru.otus.solid.util.CashUtils;

import java.util.List;

@Data
public class ATMServiceImpl implements ATMService {

    private final CashBoxRepository cashBoxRepository;

    @Override
    public void putCash(List<CashBox> cashBoxList) {
        cashBoxList.forEach(cashBoxRepository::addCashToCashBox);
    }

    @Override
    public void getCash(int sum) {
        var cashBoxList = CashUtils.toCash(cashBoxRepository.getAvailableCash(), sum);

        for (var cashBox : cashBoxList) {
            cashBoxRepository.removeCashFromCashBox(cashBox);
        }
    }

    @Override
    public List<CashBox> getAvailableCash() {
        return cashBoxRepository.getAvailableCash();
    }
}
