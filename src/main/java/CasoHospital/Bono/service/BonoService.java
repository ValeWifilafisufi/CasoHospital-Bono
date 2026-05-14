package CasoHospital.Bono.service;

import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.repository.BonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BonoService {

    private final BonoRepository bonoRepository;

    private BonoResponseDto mapToDto(Bono bono){
        return new BonoResponseDto(
                bono.getNroFolio();
                bono.getMontoCopago();
                bono.getMontoSeguro();
                bono.getFechaEmision();
                bono.getCodPrevision();
                bono.getRun();
        );
    }

    public List<BonoResponseDto> obtenerTodos(){
        return bonoRepository.findAll().stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    public Optional<Bono> buscarPorFolio(Long folio){
        return bonoRepository.findById(folio).map(this::mapToDto);
    }

    public List<BonoResponseDto> buscarPorFecha (LocalDate fecha){
        return bonoRepository.findByFechaEmision(fecha).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    public List<BonoResponseDto> buscarPorRun(String run){
        return bonoRepository.findByRunContainingIgnoreCase(run).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
}
