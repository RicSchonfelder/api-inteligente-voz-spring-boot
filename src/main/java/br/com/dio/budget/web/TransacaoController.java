package br.com.dio.budget.web;

import br.com.dio.budget.domain.TipoTransacao;
import br.com.dio.budget.domain.Transacao;
import br.com.dio.budget.repository.TransacaoRepository;
import br.com.dio.budget.service.AssistenteFinanceiro;
import br.com.dio.budget.service.SinteseVozService;
import br.com.dio.budget.service.TranscricaoService;
import br.com.dio.budget.web.dto.TransacaoRequest;
import br.com.dio.budget.web.dto.TransacaoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoRepository repository;
    private final AssistenteFinanceiro assistente;
    private final TranscricaoService transcricao;
    private final SinteseVozService sintese;

    public TransacaoController(TransacaoRepository repository,
                               AssistenteFinanceiro assistente,
                               TranscricaoService transcricao,
                               SinteseVozService sintese) {
        this.repository = repository;
        this.assistente = assistente;
        this.transcricao = transcricao;
        this.sintese = sintese;
    }

    @GetMapping
    public List<TransacaoResponse> listar() {
        return repository.findAll().stream().map(TransacaoResponse::de).toList();
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest request) {
        Transacao transacao = assistente.registrarTransacao(
                request.descricao(), request.tipo(), request.valor(), request.data());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoResponse.de(transacao));
    }

    @PostMapping("/audio")
    public ResponseEntity<TransacaoResponse> criarPorAudio(MultipartFile audio) throws Exception {
        String texto = transcricao.transcrever(audio);
        // Exemplo simples: "receita 100.00 supermercado 2025-11-11"
        String[] partes = texto.trim().split("\\s+");
        TipoTransacao tipo = TipoTransacao.valueOf(partes[0].toUpperCase());
        java.math.BigDecimal valor = new java.math.BigDecimal(partes[1]);
        String descricao = partes.length > 2 ? partes[2] : "Transacao por voz";
        Transacao transacao = assistente.registrarTransacao(
                descricao, tipo, valor, java.time.LocalDate.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoResponse.de(transacao));
    }

    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> narrar(@PathVariable Long id) {
        Transacao t = repository.findById(id).orElseThrow();
        String texto = "Transacao " + t.getDescricao() + " no valor de " + t.getValor();
        byte[] audio = sintese.sintetizar(texto);
        return ResponseEntity.ok().contentType(MediaType.valueOf("audio/mpeg")).body(audio);
    }

    @PostMapping("/assistente")
    public ResponseEntity<String> perguntar(@RequestBody String mensagem) {
        return ResponseEntity.ok(assistente.responder(mensagem));
    }
}
