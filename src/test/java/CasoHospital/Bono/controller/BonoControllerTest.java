package CasoHospital.Bono.controller;

import CasoHospital.Bono.assemblers.BonoModelAssemblers;
import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.security.JwtService;
import CasoHospital.Bono.service.BonoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // <-- ¡Importación corregida!
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BonoController.class)
@ActiveProfiles("test")
@Import(BonoModelAssemblers.class)
@WithMockUser
@DisplayName("Tests Unitarios - BonoController")
public class BonoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BonoService bonoService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @DisplayName("GIVEN: Existen bonos WHEN: GET /api/bono THEN: Retorna 200 OK y la lista paginada")
    void shouldReturnTodosLosBonos() throws Exception {
        BonoResponseDto bono1 = new BonoResponseDto(1L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "11.111.111-1");
        BonoResponseDto bono2 = new BonoResponseDto(2L, new BigDecimal("15000"), new BigDecimal("25000"), LocalDate.now(), "Isapre", "22.222.222-2");

        List<BonoResponseDto> listaFalsa = Arrays.asList(bono1, bono2);
        Page<BonoResponseDto> paginaFalsa = new PageImpl<>(listaFalsa);

        when(bonoService.obtenerTodos(any(Pageable.class))).thenReturn(paginaFalsa);

        mockMvc.perform(get("/api/bono")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bonoResponseDtoList[0].run").value("11.111.111-1"))
                .andExpect(jsonPath("$._embedded.bonoResponseDtoList[1].run").value("22.222.222-2"));
    }

    @Test
    @DisplayName("GIVEN: Folio válido WHEN: GET /api/bono/folio/{folio} THEN: Retorna 200 OK")
    void shouldReturnBonoByFolio() throws Exception {
        BonoResponseDto bono = new BonoResponseDto(100L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "12.345.678-9");

        when(bonoService.obtenerPorFolio(100L)).thenReturn(bono);

        mockMvc.perform(get("/api/bono/folio/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nroFolio").value(100))
                .andExpect(jsonPath("$.run").value("12.345.678-9"));
    }

    @Test
    @DisplayName("GIVEN: DTO válido WHEN: POST /api/bono THEN: Retorna 201 Created")
    void shouldCreateBono() throws Exception {
        BonoRequestDto request = new BonoRequestDto(new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "12.345.678-9");
        BonoResponseDto response = new BonoResponseDto(50L, new BigDecimal("10000"), new BigDecimal("30000"), LocalDate.now(), "Fonasa", "12.345.678-9");

        when(bonoService.guardar(any(BonoRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/bono")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nroFolio").value(50))
                .andExpect(jsonPath("$.run").value("12.345.678-9"));
    }

    @Test
    @DisplayName("GIVEN: Folio y DTO válidos WHEN: PUT /api/bono/{folio} THEN: Retorna 200 OK")
    void shouldUpdateBono() throws Exception {
        Long folio = 1L;
        BonoRequestDto updateRequest = new BonoRequestDto(new BigDecimal("15000"), new BigDecimal("35000"), LocalDate.now(), "12.345.678-9");
        BonoResponseDto response = new BonoResponseDto(1L, new BigDecimal("15000"), new BigDecimal("35000"), LocalDate.now(), "Isapre", "12.345.678-9");

        when(bonoService.actualizar(eq(folio), any(BonoRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/bono/" + folio)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoCopago").value(15000))
                .andExpect(jsonPath("$.nombrePrevision").value("Isapre")); // CORREGIDO: antes decía codPrevision
    }

    @Test
    @DisplayName("GIVEN: Folio válido WHEN: DELETE /api/bono/{folio} THEN: Retorna 204 No Content")
    void shouldDeleteBono() throws Exception {
        Long folio = 1L;
        doNothing().when(bonoService).eliminar(folio);

        mockMvc.perform(delete("/api/bono/" + folio).with(csrf()))
                .andExpect(status().isNoContent());
    }
}