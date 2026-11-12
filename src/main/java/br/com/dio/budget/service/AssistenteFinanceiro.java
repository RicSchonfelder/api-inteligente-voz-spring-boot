package br.com.dio.budget.service;

import br.com.dio.budget.domain.TipoTransacao;
import br.com.dio.budget.domain.Transacao;
import br.com.dio.budget.repository.TransacaoRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AssistenteFinanceiro {

    private final ChatClient chatClient;
    private final TransacaoRepository repository;

    public AssistenteFinanceiro(ChatClient.Builder builder, TransacaoRepository repository) {
        this.chatClient = builder
                .defaultSystem("Voce e um assistente financeiro. Extraia a intencao do usuario e registre transacoes.")
                .build();
        this.repository = repository;
    }

    public String responder(String mensagem) {
        return chatClient.prompt()
                .user(mensagem)
                .call()
                .content();
    }

    public Transacao registrarTransacao(String descricao, TipoTransacao tipo, BigDecimal valor, LocalDate data) {
        Transacao transacao = new Transacao(descricao, tipo, valor, data);
        return repository.save(transacao);
    }
}
