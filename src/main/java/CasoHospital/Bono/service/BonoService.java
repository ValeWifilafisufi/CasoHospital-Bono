package CasoHospital.Bono.service;

import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.repository.BonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BonoService {

    private final BonoRepository bonoRepository;
    private final PacienteClient pacienteClient;
    private final PrevisionClient previsionClient;

    private BonoResponseDto mapToDto(Bono bono){

        return new BonoResponseDto(
                bono.getNroFolio(),
                bono.getMontoCopago(),
                bono.getMontoSeguro(),
                bono.getFechaEmision(),
                bono.getCodPrevision(),
                bono.getRun()
        );
    }

    public List<BonoResponseDto> obtenerTodos(){
        return bonoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<BonoResponseDto> buscarPorFolio(Long folio){
        return bonoRepository.findById(folio)
                .map(this::mapToDto);
    }

    public List<BonoResponseDto> buscarPorFecha(LocalDate fecha){
        return bonoRepository.findByFechaEmision(fecha)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BonoResponseDto> buscarPorRun(String run){
        return bonoRepository.findByRunContainingIgnoreCase(run)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // GUARDAR
    public BonoResponseDto guardar(BonoRequestDto dto){
        // VALIDAR PACIENTE
        Map<String, Object> paciente =
                pacienteClient.obtenerPaciente(dto.getRun());
        if (paciente == null){
            throw new RuntimeException("Paciente no encontrado");
        }
        // VALIDAR PREVISION
        Map<String, Object> prevision =
                previsionClient.obtenerPrevision(dto.getCodPrevision());
        if (prevision == null){
            throw new RuntimeException("Prevision no encontrada");
        }
        Bono nuevoBono = new Bono();
        nuevoBono.setMontoCopago(dto.getMontoCopago());
        nuevoBono.setMontoSeguro(dto.getMontoSeguro());
        nuevoBono.setFechaEmision(dto.getFechaEmision());
        nuevoBono.setCodPrevision(dto.getCodPrevision());
        nuevoBono.setRun(dto.getRun());
        Bono bonoGuardado = bonoRepository.save(nuevoBono);

        return mapToDto(bonoGuardado);
    }
    //Actualizar
    public Optional<Bono> actualizar(Long folio, Bono bono){
        return bonoRepository.findById(folio).map(existente ->
        {existente.setMontoCopago(bono.getMontoCopago());
            existente.setMontoSeguro(bono.getMontoSeguro());
            existente.setFechaEmision(bono.getFechaEmision());

            return bonoRepository.save(existente);
        });
    }

    //Eliminar
    public void eliminar(Long folio){bonoRepository.deleteById(folio);}

}
