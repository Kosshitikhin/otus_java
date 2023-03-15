package ru.otus.solid.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CashBox {

    private Nominal nominal;

    private int count;
}
