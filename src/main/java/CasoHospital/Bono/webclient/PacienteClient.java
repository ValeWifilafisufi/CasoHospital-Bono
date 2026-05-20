package CasoHospital.Bono.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class PacienteClient {

    private final WebClient webClient;

    public PacienteClient(@Value("${paciente-service.url}") String pacienteUrl) {

        this.webClient = WebClient.builder()
                .baseUrl(pacienteUrl)
                .build();
    }

    public Map<String, Object> obtenerPaciente(String run) {

        return webClient.get()
                .uri("/{run}", run)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
