# api-inteligente-voz-spring-boot

API inteligente de orcamento financeiro construida com Spring Boot 3 e Spring AI.

## Funcionalidades

- Cadastro e listagem de transacoes (receitas/despesas) via JPA + H2.
- Assistente financeiro com ChatClient (tool calling).
- Transcricao de audio para texto (OpenAI Whisper).
- Sintese de voz a partir de texto (OpenAI TTS).
- Endpoint `/api/transacoes/audio` que recebe um audio, transcreve e registra a transacao.
- Endpoint `/api/transacoes/{id}/audio` que narra a transacao em audio.

## Como executar

Defina a variavel de ambiente `OPENAI_API_KEY` e rode:

```bash
mvn spring-boot:run
```

## Testes

```bash
mvn test
```

4 testes (1 de servico, 3 de controller).

## Autor

Ricardo Schonfelder Filho - Bootcamp Itaú - Java com Inteligência Artificial (DIO)
