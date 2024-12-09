package com.johann.demoparkapi.config;

import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

public class SpringTimezoneConfig {

    @PostConstruct
    public void timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
