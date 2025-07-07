package org.com.entity;

import java.time.LocalDate;

/**
 *  Represents a stock exchange entity.
 **/
public record Symbol(
        String code,
        String name,
        boolean status,
        LocalDate ipoDate,
        String assetType
) {}
