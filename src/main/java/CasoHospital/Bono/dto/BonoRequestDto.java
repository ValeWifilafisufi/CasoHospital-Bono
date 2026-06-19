package CasoHospital.Bono.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BonoRequestDto {

    @NotNull(message = "El monto del copago no puede estar vacio")
    @Positive(message = "El monto del copago debe ser mayor a 0")
    @Schema(description = "Monto del copago", example = "1500")
    private BigDecimal montoCopago;

    @NotNull(message = "El monto del seguro no puede estar vacio")
    @Positive(message = "El monto del seguro debe ser mayor a 0")
    @Schema(description = "Monto del seguro", example = "4000")
    private BigDecimal montoSeguro;

    @NotNull(message = "La fecha de emision no puede estar vacia")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de emision del Bono", example = "2025-02-22")
    private LocalDate fechaEmision;

    @NotBlank(message = "El numero de rut del paciente no puede estar vacio")
    @Pattern(
            regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]$",
            message = "El run debe tener formato 12.345.678-9"
    )
    @Schema(description = "RUN del paciente", example = "22.359.190-6")
    private String run;
}
