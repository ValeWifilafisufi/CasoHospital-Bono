package CasoHospital.Bono.repository;

import CasoHospital.Bono.model.Bono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface BonoRepository extends JpaRepository {


    List<Bono> findByRunContainingIgnoreCase (String run);
    List<Bono> findByFechaEmision (LocalDate fechaEmision);

}
