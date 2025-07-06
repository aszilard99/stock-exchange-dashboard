package org.com.entity;

import java.util.Date;

public record CompanyOverview (String symbol,
                               Date latestQuarter,
                               String name,
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
