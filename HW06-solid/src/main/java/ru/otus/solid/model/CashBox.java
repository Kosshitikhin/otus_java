package ru.otus.solid.model;

import lombok.Data;

@Data
public class CashBox {

    private final Nominal nominal;

    private int count;

    public CashBox(Nominal nominal, int count) {
        this.nominal = nominal;
        if (count < 0) {
            throw new IllegalArgumentException("Количество купюр в ячейке не может быть отрицательным");
        }
        this.count = count;
    }
}
