package ru.otus.solid.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ATM {

    private List<CashBox> cashBoxList;
}
