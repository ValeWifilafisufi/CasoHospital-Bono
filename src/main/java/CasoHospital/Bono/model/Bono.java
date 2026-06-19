package CasoHospital.Bono.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bono")
public class Bono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroFolio;

    @Column(name = "monto_copago", nullable = false)
    private BigDecimal montoCopago;

    @Column(name = "monto_seguro", nullable = false)
    private BigDecimal montoSeguro;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "nombre_prevision", nullable = false)
    private String nombrePrevision;

    @Column(name = "num_run", nullable = false)
    private String run;

}
