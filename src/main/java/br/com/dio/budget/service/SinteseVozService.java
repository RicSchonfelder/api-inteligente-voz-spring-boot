package br.com.dio.budget.service;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Service;

@Service
public class SinteseVozService {

    private final OpenAiAudioSpeechModel speechModel;

    public SinteseVozService(OpenAiAudioSpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    public byte[] sintetizar(String texto) {
        SpeechResponse resposta = speechModel.call(new SpeechPrompt(texto));
        return resposta.getResult().getOutput();
    }
}
