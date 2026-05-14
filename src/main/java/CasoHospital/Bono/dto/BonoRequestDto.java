package CasoHospital.Bono.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private BigDecimal montoCopago;

    @NotNull(message = "El monto del seguro no puede estar vacio")
    @Positive(message = "El monto del seguro debe ser mayor a 0")
    private BigDecimal montoSeguro;

    @NotBlank(message = "La fecha de emision no puede estar vacia")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaEmision;

    @NotNull(message = "El codigo de prevision no puede estar vacio")
    @Positive(message = "El codigo de prevision debe ser mayor a 0")
    private Long codPrevision;

    @NotNull(message = "El numero de rut del paciente no puede estar vacio")
    @Pattern(
            regexp = "^[0-9]{1,2}\\.[0-9]{3}\\.[0-9]{3}-[0-9kK]$",
            message = "El run debe tener formato 12.345.678-9"
    )
    private String run;


}
