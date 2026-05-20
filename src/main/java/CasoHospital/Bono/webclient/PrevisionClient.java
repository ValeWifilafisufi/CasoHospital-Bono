package CasoHospital.Bono.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class PrevisionClient {

    private final WebClient webClient;

    public PrevisionClient(@Value("${prevision-service.url}") String previsionUrl) {

        this.webClient = WebClient.builder()
                .baseUrl(previsionUrl)
                .build();
    }

    public Map<String, Object> obtenerPrevision(Long codigo) {

        return webClient.get()
                .uri("/{codigo}", codigo)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}