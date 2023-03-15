package ru.otus.solid.util;

import ru.otus.solid.exception.ConvertException;
import ru.otus.solid.model.CashBox;

import java.util.ArrayList;
import java.util.List;


public abstract class CashUtils {

    public static List<CashBox> toCash(List<CashBox> cashBoxList, int sum) {
        List<CashBox> resultCashBoxList = new ArrayList<>();

        cashBoxList.sort((o1, o2) -> o2.getNominal().getValue() - o1.getNominal().getValue());

        for (CashBox cashBox : cashBoxList) {
            var nominalValue = cashBox.getNominal().getValue();

            if (sum >= nominalValue) {
                var count = getCount(sum, nominalValue, cashBox.getCount());
                sum -= nominalValue * count;
                resultCashBoxList.add(new CashBox(cashBox.getNominal(), count));
            }
        }

        if (sum > 0) throw new ConvertException("В источнике недостаточно денежных средств");

        return resultCashBoxList;
    }

    private static int getCount(int sum, int nominalValue, int sourceCashCount) {
        int count = sum / nominalValue;
        return sourceCashCount - count >= 0 ? count : sourceCashCount;
    }

}
