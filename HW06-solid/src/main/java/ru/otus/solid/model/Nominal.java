package ru.otus.solid.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Nominal {

    RU_100(100),
    RU_200(200),
    RU_500(500),
    RU_1000(1000),
    RU_2000(2000),
    RU_5000(5000);

    private final Integer value;
}
