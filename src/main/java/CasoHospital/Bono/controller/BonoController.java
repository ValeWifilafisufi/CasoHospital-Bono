package CasoHospital.Bono.controller;

import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.service.BonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bono")
@RequiredArgsConstructor
@Tag(value = (name = "Staff", description = "Operaciones relacionadas con el Staff")
public class BonoController {

    private final BonoService bonoService;

    @GetMapping
    @Operation(summary = "Obtener todos los staff", description = "Obtiene una lista con todos los staff ingresados")
    public ResponseEntity<List<BonoResponseDto>> obtenerTodos(){
        return ResponseEntity.ok(bonoService.obtenerTodos());
    }

    @GetMapping("/folio/{folio}")
    public ResponseEntity<BonoResponseDto> buscarPorFolio(
            @PathVariable Long folio){

        return bonoService.buscarPorFolio(folio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<BonoResponseDto>> buscarPorFecha(
            @PathVariable LocalDate fecha){

        return ResponseEntity.ok(
                bonoService.buscarPorFecha(fecha));
    }

    @GetMapping("/run/{run}")
    public ResponseEntity<List<BonoResponseDto>> buscarPorRun(
            @PathVariable String run){

        return ResponseEntity.ok(
                bonoService.buscarPorRun(run));
    }

    @PostMapping
    public ResponseEntity<BonoResponseDto> crear(
            @Valid @RequestBody BonoRequestDto dto){

        BonoResponseDto respuesta =
                bonoService.guardar(dto);

        return ResponseEntity.status(201).body(respuesta);
    }

    @PutMapping("/{folio}")
    public ResponseEntity<Bono> actualizar(
            @PathVariable Long folio,
            @Valid @RequestBody Bono bono){

        return bonoService.actualizar(folio, bono)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long folio){

        if (bonoService.buscarPorFolio(folio).isEmpty()){
            return ResponseEntity.notFound().build();
        }

        bonoService.eliminar(folio);

        return ResponseEntity.noContent().build();
    }
}