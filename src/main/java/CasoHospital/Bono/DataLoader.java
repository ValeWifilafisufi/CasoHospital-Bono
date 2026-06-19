package CasoHospital.Bono;

import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.model.Role;
import CasoHospital.Bono.model.User;
import CasoHospital.Bono.repository.BonoRepository;
import CasoHospital.Bono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner{

    private final BonoRepository bonoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("valentina").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("valentina");
            adminUser.setPassword(passwordEncoder.encode("123"));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);
            log.info("Usuario administrador creado automáticamente (username: valentina).");
        }

        if (userRepository.findByUsername("Maty").isEmpty()) {
            User adminUser2 = new User();
            adminUser2.setUsername("Maty");
            adminUser2.setPassword(passwordEncoder.encode("1234"));
            adminUser2.setRole(Role.ADMIN);
            userRepository.save(adminUser2);
            log.info("Usuario administrador creado automáticamente (username: Maty).");
        }

        if (bonoRepository.count() > 0) {
            log.info(">>> DataFaker: La base de datos ya tiene bonos. Carga omitida.");
            return;
        }

        log.info(">>> DataFaker: Generando 100 bonos aleatorios con pacientes reales...");
        Faker faker = new Faker(Locale.of("es"));

        List<String> poolPacientes = List.of(
                "22.359.190-6",
                "18.765.432-1",
                "11.111.111-1",
                "22.222.222-2",
                "10.333.333-3"
        );

        String[] previsiones = {
                "Fonasa", "Isapre Consalud", "Isapre Cruz Blanca", "Isapre Banmédica"
        };

        for (int i = 0; i < 100; i++) {
            Bono bono = new Bono();

            double copagoAleatorio = faker.number().randomDouble(0, 2000, 60000);
            double seguroAleatorio = faker.number().randomDouble(0, 5000, 200000);
            bono.setMontoCopago(BigDecimal.valueOf(copagoAleatorio));
            bono.setMontoSeguro(BigDecimal.valueOf(seguroAleatorio));

            int diasAtras = faker.number().numberBetween(0, 365);
            bono.setFechaEmision(LocalDate.now().minusDays(diasAtras));

            String runAleatorio = poolPacientes.get(faker.random().nextInt(poolPacientes.size()));
            String previsionAleatoria = previsiones[faker.random().nextInt(previsiones.length)];

            bono.setRun(runAleatorio);
            bono.setNombrePrevision(previsionAleatoria);

            bonoRepository.save(bono);
        }

        log.info(">>> DataFaker: ¡100 bonos insertados exitosamente y listos para las pruebas!");
    }
}