package br.com.dio.budget.web.dto;

import br.com.dio.budget.domain.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(
        @NotBlank String descricao,
        @NotNull TipoTransacao tipo,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotNull LocalDate data) {
}
