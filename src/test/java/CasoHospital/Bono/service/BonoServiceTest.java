package CasoHospital.Bono.service;

import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.exception.RecursoNoEncontradoException;
import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.repository.BonoRepository;
import CasoHospital.Bono.webclient.PacienteClient;
import CasoHospital.Bono.webclient.PrevisionClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - BonoService")
public class BonoServiceTest {

    @Mock
    private BonoRepository bonoRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private PrevisionClient previsionClient;

    @InjectMocks
    private BonoService bonoService;

    @Test
    @DisplayName("GIVEN: Pageable WHEN: obtenerTodos THEN: Retorna Page de BonoResponseDto")
    void shouldObtenerTodos() {
        Bono bono = new Bono(1L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "12.345.678-9");
        Page<Bono> paginaFalsa = new PageImpl<>(List.of(bono));
        Pageable pageable = PageRequest.of(0, 10);

        when(bonoRepository.findAll(pageable)).thenReturn(paginaFalsa);

        Page<BonoResponseDto> resultado = bonoService.obtenerTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("12.345.678-9", resultado.getContent().get(0).getRun());
        verify(bonoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GIVEN: Folio válido WHEN: obtenerPorFolio THEN: Retorna BonoResponseDto")
    void shouldObtenerPorFolio() {
        Bono bono = new Bono(1L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "12.345.678-9");

        when(bonoRepository.findById(1L)).thenReturn(Optional.of(bono));

        BonoResponseDto resultado = bonoService.obtenerPorFolio(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getNroFolio());
        verify(bonoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GIVEN: Folio inexistente WHEN: obtenerPorFolio THEN: Lanza Exception")
    void shouldThrowExceptionWhenObtenerPorFolioNoExiste() {
        when(bonoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            bonoService.obtenerPorFolio(99L);
        });
    }

    @Test
    @DisplayName("GIVEN: DTO válido WHEN: guardar THEN: Retorna BonoResponseDto guardado")
    void shouldGuardarBonoExitosamente() {
        BonoRequestDto request = new BonoRequestDto(new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "12.345.678-9");
        Bono bonoGuardado = new Bono(1L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "12.345.678-9");
        Map<String, Object> mockPaciente = new HashMap<>();
        mockPaciente.put("nombre", "Juan");
        mockPaciente.put("id_prevision", 1L);
        mockPaciente.put("prevision", 1L);
        mockPaciente.put("codPrevision", 1L);

        Map<String, Object> mockPrevision = new HashMap<>();
        mockPrevision.put("nombre", "Fonasa");
        mockPrevision.put("nombrePrevision", "Fonasa");

        lenient().when(pacienteClient.obtenerPaciente("12.345.678-9")).thenReturn(mockPaciente);
        lenient().when(previsionClient.obtenerPrevision(any())).thenReturn(mockPrevision);

        when(bonoRepository.save(any(Bono.class))).thenReturn(bonoGuardado);

        BonoResponseDto resultado = bonoService.guardar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getNroFolio());
        verify(bonoRepository, times(1)).save(any(Bono.class));
    }

    @Test
    @DisplayName("GIVEN: Paciente inexistente WHEN: guardar THEN: Lanza Exception")
    void shouldThrowExceptionWhenPacienteNoExiste() {
        BonoRequestDto request = new BonoRequestDto(new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "99.999.999-9");

        when(pacienteClient.obtenerPaciente("99.999.999-9")).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> {
            bonoService.guardar(request);
        });

        verify(bonoRepository, never()).save(any(Bono.class));
    }

    @Test
    @DisplayName("GIVEN: Folio válido WHEN: eliminar THEN: Ejecuta deleteById")
    void shouldEliminarBono() {
        Long folio = 1L;
        when(bonoRepository.existsById(folio)).thenReturn(true);
        doNothing().when(bonoRepository).deleteById(folio);

        bonoService.eliminar(folio);

        verify(bonoRepository, times(1)).existsById(folio);
        verify(bonoRepository, times(1)).deleteById(folio);
    }
}