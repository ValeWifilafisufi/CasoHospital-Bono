package CasoHospital.Bono.controller;

import CasoHospital.Bono.assemblers.BonoModelAssemblers;
import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.service.BonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bono")
@RequiredArgsConstructor
@Tag(name = "Gestión de Bonos", description = "Endpoints para administrar el sistema de Bonos del Hospital")
public class BonoController {

    private final BonoService bonoService;
    private final BonoModelAssemblers assembler;

    //----------------- OBTENER TODOS LOS BONOS (PAGINADO) -----------------
    @Operation(summary = "Obtener todos los bonos", description = "Retorna una lista con todos los bonos registrados en el sistema")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BonoResponseDto>>> obtenerTodos(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<BonoResponseDto> pagedAssembler) {

        Page<BonoResponseDto> paginaBono = bonoService.obtener(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(paginaBono, assembler));
    }

    //----------------- BUSCAR POR FOLIO -----------------
    @Operation(summary = "Obtener bono por folio", description = "Retorna la información de un bono específico mediante su número de folio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bono encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El número de folio no corresponde a ningún bono")
    })
    @GetMapping("/folio/{folio}")
    public ResponseEntity<EntityModel<BonoResponseDto>> obtenerPorFolio(
            @Parameter(description = "Número de folio del bono a buscar", example = "1001") @PathVariable Long folio) {

        BonoResponseDto bono = bonoService.obtenerPorFolio(folio);
        return ResponseEntity.ok(assembler.toModel(bono));
    }

    //----------------- BUSCAR POR FECHA (PAGINADO) -----------------
    @Operation(summary = "Buscar bonos por fecha de emisión", description = "Retorna una lista de bonos emitidos en una fecha específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bonos encontrados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron bonos en la fecha indicada")
    })
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<PagedModel<EntityModel<BonoResponseDto>>> buscarPorFecha(
            @PathVariable LocalDate fecha,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<BonoResponseDto> pagedAssembler) {

        Page<BonoResponseDto> pagina = bonoService.buscarPorFecha(fecha, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //----------------- BUSCAR POR RUN PACIENTE (PAGINADO) -----------------
    @Operation(summary = "Buscar bonos por RUN de paciente", description = "Retorna una lista de bonos asociados al RUN de un paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bonos encontrados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existen bonos registrados para el RUN ingresado")
    })
    @GetMapping("/run/{run}")
    public ResponseEntity<PagedModel<EntityModel<BonoResponseDto>>> buscarPorRun(
            @PathVariable String run,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<BonoResponseDto> pagedAssembler) {

        Page<Page<BonoResponseDto>> pagina = bonoService.buscarPorRun(run, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //----------------- GUARDAR/CREAR BONO -----------------
    @Operation(summary = "Emitir un nuevo bono", description = "Registra un bono en el sistema validando previamente al Paciente y su Previsión")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bono emitido y creado correctamente"),
            @ApiResponse(responseCode = "404", description = "El RUN del paciente o el código de previsión no existen")
    })
    @PostMapping
    public ResponseEntity<EntityModel<BonoResponseDto>> guardar(
            @Valid @RequestBody BonoRequestDto dto) {

        BonoResponseDto bonoGuardado = bonoService.guardar(dto);
        return ResponseEntity.status(201).body(assembler.toModel(bonoGuardado));
    }

    //----------------- ACTUALIZAR BONO -----------------
    @Operation(summary = "Actualizar datos de un bono", description = "Modifica los datos financieros o de fecha de un bono existente mediante su folio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bono actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "El número de folio no coincide con ningún bono")
    })
    @PutMapping("/{folio}")
    public ResponseEntity<EntityModel<BonoResponseDto>> actualizar(
            @Parameter(description = "Número de folio del bono a actualizar", example = "1001") @PathVariable Long folio,
            @Valid @RequestBody BonoRequestDto dto) {

        BonoResponseDto bonoActualizado = bonoService.actualizar(folio, dto);
        return ResponseEntity.ok(assembler.toModel(bonoActualizado));
    }

    //----------------- ELIMINAR BONO -----------------
    @Operation(summary = "Eliminar un bono del sistema", description = "Remueve permanentemente un bono mediante su número de folio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bono eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El número de folio ingresado no existe")
    })
    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Número de folio del bono a eliminar", example = "1001") @PathVariable Long folio) {

        bonoService.eliminar(folio);
        return ResponseEntity.noContent().build();
    }
}