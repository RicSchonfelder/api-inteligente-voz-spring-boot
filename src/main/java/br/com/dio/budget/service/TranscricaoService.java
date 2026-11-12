package br.com.dio.budget.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TranscricaoService {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscricaoService(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    public String transcrever(MultipartFile arquivo) throws IOException {
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(
                new ByteArrayResource(arquivo.getBytes()) {
                    @Override
                    public String getFilename() {
                        return arquivo.getOriginalFilename();
                    }
                });
        AudioTranscriptionResponse resposta = transcriptionModel.call(prompt);
        return resposta.getResult().getOutput();
    }
}
