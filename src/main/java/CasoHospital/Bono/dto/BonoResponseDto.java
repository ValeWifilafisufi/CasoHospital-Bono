package CasoHospital.Bono.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonoResponseDto {

    @Schema(description = "Numero de folio generado automaticamente.", example = "2")
    private Long nroFolio;
    @Schema(description = "Monto de copago", example = "5600")
    private BigDecimal montoCopago;
    @Schema(description = "Monto del seguro", example = "2000")
    private BigDecimal montoSeguro;
    @Schema(description = "Fecha de emision", example = "2025-02-22")
    private LocalDate fechaEmision;
    @Schema(description = "Nombre de la previson asignada al paciente", example = "Fonasa Tramo B")
    private String nombrePrevision;
    @Schema(description = "RUN del paciente", example = "22.359.190-6")
    private String run;
}
