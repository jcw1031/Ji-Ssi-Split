package com.woopaca.jissisplit.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String reutersCode;

    @Enumerated(value = EnumType.STRING)
    private StockType type;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ExchangeType exchangeType;

    private String logo;
}
