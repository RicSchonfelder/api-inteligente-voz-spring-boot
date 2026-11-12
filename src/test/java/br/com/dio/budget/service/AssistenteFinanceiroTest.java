package br.com.dio.budget.service;

import br.com.dio.budget.domain.Transacao;
import br.com.dio.budget.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssistenteFinanceiroTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    @Mock
    private TransacaoRepository repository;

    @Test
    void registraTransacaoPersisteERetorna() {
        Transacao salva = new Transacao("Supermercado", br.com.dio.budget.domain.TipoTransacao.DESPESA,
                new BigDecimal("120.50"), LocalDate.of(2025, 11, 11));
        when(repository.save(any(Transacao.class))).thenReturn(salva);

        org.mockito.Mockito.when(chatClientBuilder.defaultSystem(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(chatClientBuilder);
        org.mockito.Mockito.when(chatClientBuilder.build()).thenReturn(chatClient);

        AssistenteFinanceiro assistente = new AssistenteFinanceiro(chatClientBuilder, repository);
        Transacao resultado = assistente.registrarTransacao("Supermercado",
                br.com.dio.budget.domain.TipoTransacao.DESPESA, new BigDecimal("120.50"),
                LocalDate.of(2025, 11, 11));

        assertThat(resultado.getDescricao()).isEqualTo("Supermercado");
        assertThat(resultado.getValor()).isEqualByComparingTo("120.50");
    }
}
