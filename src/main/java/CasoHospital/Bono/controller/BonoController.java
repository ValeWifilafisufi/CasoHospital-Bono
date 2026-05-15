package CasoHospital.Bono.controller;


import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.service.BonoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bono")
@RequiredArgsConstructor

public class BonoController {
    private final BonoService bonoService;

    @GetMapping
    public ResponseEntity<List<BonoResponseDto>> obtenerTod(){
        return ResponseEntity.ok(bonoService.obtenerTodos());
    }

    @GetMapping("/folio/{folio}")
    public ResponseEntity<BonoResponseDto> buscarPorFolio(@PathVariable Long folio){
        return bonoService.buscarPorFolio(folio).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<BonoResponseDto>> buscarPorFecha(@PathVariable LocalDate fecha){
        return ResponseEntity.ok(bonoService.buscarPorFecha(fecha));
    }

    @GetMapping("/run/{run}")
    public ResponseEntity<List<BonoResponseDto>> buscarPorRun(@PathVariable String run){
        return ResponseEntity.ok(bonoService.buscarPorRun(run));
//    }
//    @GetMapping("/fecha/{fecha}")
//    public ResponseEntity<List<CitaMedicaResponseDto>> buscarPorFecha(@PathVariable LocalDate fecha){
//        return ResponseEntity.ok(citaMedicaService.buscarPorCita(fecha));
//    }

    @PostMapping
    public ResponseEntity<CitaMedicaResponseDto> crear(@Valid @RequestBody CitaMedicaRequestDto dto){
        CitaMedicaResponseDto respuesta = citaMedicaService.guardar(dto);
        return ResponseEntity.status(201).body(respuesta);
    }

    @PutMapping("{nro}")
    public ResponseEntity<CitaMedica> actualizar(@PathVariable Long nro,
                                                 @Valid @RequestBody CitaMedica cita){
        return citaMedicaService.actualizar(nro, cita).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{nro}")
    public ResponseEntity<Void> eliminar(@PathVariable Long nro){
        if (citaMedicaService.buscarPorNroCita(nro).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        citaMedicaService.eliminar(nro);
        return ResponseEntity.noContent().build();
    }
}
