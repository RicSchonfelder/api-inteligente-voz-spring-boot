package br.com.dio.budget.web.dto;

import br.com.dio.budget.domain.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(Long id, String descricao, String tipo, BigDecimal valor, LocalDate data) {

    public static TransacaoResponse de(Transacao t) {
        return new TransacaoResponse(t.getId(), t.getDescricao(), t.getTipo().name(), t.getValor(), t.getData());
    }
}
