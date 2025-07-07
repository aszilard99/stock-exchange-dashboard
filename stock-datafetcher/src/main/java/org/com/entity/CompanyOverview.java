package org.com.entity;

public record CompanyOverview (String symbol,
                               String latestQuarter,
                               String country,
                               String sector,
                               Double analystTargetPrice,
                               Integer analystStrongBuy,
                               Integer analystBuy,
                               Integer analystHold,
                               Integer analystSell,
                               Integer analystStrongSell,
                               Double percentInsiders,
                               Double percentInstitutions,
                               Double profitMargin,
                               Double beta
                               ){}
