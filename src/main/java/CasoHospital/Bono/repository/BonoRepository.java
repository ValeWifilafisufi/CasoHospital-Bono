package CasoHospital.Bono.repository;

import CasoHospital.Bono.model.Bono;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;


public interface BonoRepository extends JpaRepository<Bono, Long> {


    Page<Bono> findByRunContainingIgnoreCase (String run, Pageable pageable);
    Page<Bono> findByFechaEmision (LocalDate fechaEmision, Pageable pageable);

}
