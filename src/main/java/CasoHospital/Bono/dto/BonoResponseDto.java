package CasoHospital.Bono.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BonoResponseDto {

    private Long nro_folio;
    private BigDecimal montoCopago;
    private BigDecimal montoSeguro;
    private LocalDate fechaEmision;
    private Long codPrevision;
    private String run;
}
