package CasoHospital.Bono.config;

import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.repository.BonoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BonoRepository bonoRepository;

    @Override
    public void run(String... args) {

        if (bonoRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");

        bonoRepository.save(new Bono(null,new BigDecimal("12000"),
                new BigDecimal("35000"),LocalDate.of(2026, 5, 10),
                1L,"22.359.190-6"));

        bonoRepository.save(new Bono(null,new BigDecimal("8500"),
                new BigDecimal("28000"),LocalDate.of(2026, 5, 11),
                2L,"18.765.432-1"));

        bonoRepository.save(new Bono(null,new BigDecimal("15000"),
                new BigDecimal("40000"),LocalDate.of(2026, 5, 12),
                3L,"11.111.111-1"));

        bonoRepository.save(new Bono(null,new BigDecimal("10000"),
                new BigDecimal("32000"),LocalDate.of(2026, 5, 13),
                4L,"22.222.222-2"));

        bonoRepository.save(new Bono(null,new BigDecimal("7000"),
                new BigDecimal("25000"),LocalDate.of(2026, 5, 14),
                5L,"10.333.333-3"));

        log.info(">>> DataInitializer: {} bonos insertados correctamente.",
                bonoRepository.count());
    }
}