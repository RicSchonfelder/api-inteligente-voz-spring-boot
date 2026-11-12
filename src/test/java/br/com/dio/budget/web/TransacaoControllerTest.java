package br.com.dio.budget.web;

import br.com.dio.budget.ApiApplication;
import br.com.dio.budget.domain.TipoTransacao;
import br.com.dio.budget.domain.Transacao;
import br.com.dio.budget.repository.TransacaoRepository;
import br.com.dio.budget.service.AssistenteFinanceiro;
import br.com.dio.budget.service.SinteseVozService;
import br.com.dio.budget.service.TranscricaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoRepository repository;

    @MockBean
    private AssistenteFinanceiro assistente;

    @MockBean
    private TranscricaoService transcricao;

    @MockBean
    private SinteseVozService sintese;

    @Test
    void criaTransacao() throws Exception {
        Transacao salva = new Transacao("Mercado", TipoTransacao.DESPESA,
                new BigDecimal("50.00"), LocalDate.of(2025, 11, 11));
        when(assistente.registrarTransacao(any(), any(), any(), any())).thenReturn(salva);

        String body = """
                {
                  "descricao": "Mercado",
                  "tipo": "DESPESA",
                  "valor": 50.00,
                  "data": "2025-11-11"
                }
                """;

        mockMvc.perform(post("/api/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Mercado"))
                .andExpect(jsonPath("$.tipo").value("DESPESA"));
    }

    @Test
    void listaTransacoes() throws Exception {
        Transacao t = new Transacao("Salario", TipoTransacao.RECEITA,
                new BigDecimal("3000.00"), LocalDate.of(2025, 11, 1));
        when(repository.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Salario"));
    }

    @Test
    void rejeitaTransacaoInvalida() throws Exception {
        String body = """
                {"descricao": "", "tipo": "DESPESA", "valor": 0, "data": "2025-11-11"}
                """;

        mockMvc.perform(post("/api/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
