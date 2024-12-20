package com.johann.demoparkapi.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClienteVagaProjection {

    String getPlaca();

    String getMarca();

    String getModelo();

    String getCor();

    String getClienteCpf();

    String getRecibo();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDataEntrada();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDataSaida();

    String getVagaCodigo(); // mapper consegue pegar lá na Vaga.codigo

    BigDecimal getValor();

    BigDecimal getDesconto();
}
