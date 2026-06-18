import CasoHospital.Bono.dto.BonoRequestDto;
import CasoHospital.Bono.dto.BonoResponseDto;
import CasoHospital.Bono.exception.RecursoNoEncontradoException;
import CasoHospital.Bono.model.Bono;
import CasoHospital.Bono.repository.BonoRepository;
import CasoHospital.Bono.webclient.PacienteClient;
import CasoHospital.Bono.webclient.PrevisionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BonoService {

    private final BonoRepository bonoRepository;
    private final PacienteClient pacienteClient;
    private final PrevisionClient previsionClient;

    public BonoResponseDto mapToDto(Bono bono) {
        return new BonoResponseDto(
                bono.getNroFolio(),
                bono.getMontoCopago(),
                bono.getMontoSeguro(),
                bono.getFechaEmision(),
                bono.getCodPrevision(),
                bono.getRun()
        );
    }

    public Page<BonoResponseDto> obtenerTodos(Pageable pageable) {
        return bonoRepository.findAll(pageable).map(this::mapToDto);
    }

    public BonoResponseDto obtenerPorFolio(Long folio) {
        Bono bono = bonoRepository.findById(folio)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un bono con el folio " + folio));
        return mapToDto(bono);
    }

    public Page<BonoResponseDto> buscarPorFecha(LocalDate fecha, Pageable pageable) {
        Page<BonoResponseDto> pagina = bonoRepository.findByFechaEmision(fecha, pageable).map(this::mapToDto);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen bonos registrados para la fecha " + fecha);
        }
        return pagina;
    }

    public Page<BonoResponseDto> buscarPorRun(String run, Pageable pageable) {
        Page<BonoResponseDto> pagina = bonoRepository.findByRunContainingIgnoreCase(run, pageable).map(this::mapToDto);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen bonos registrados con el RUN " + run);
        }
        return pagina;
    }

    @Transactional
    public BonoResponseDto guardar(BonoRequestDto dto) {
        Map<String, Object> paciente = pacienteClient.obtenerPaciente(dto.getRun());
        if (paciente == null) {
            throw new RecursoNoEncontradoException("Paciente con RUN " + dto.getRun() + " no encontrado en el sistema");
        }
        Map<String, Object> prevision = previsionClient.obtenerPrevision(dto.getCodPrevision());
        if (prevision == null) {
            throw new RecursoNoEncontradoException("Previsión con código " + dto.getCodPrevision() + " no encontrada en el sistema");
        }

        Bono nuevoBono = new Bono();
        nuevoBono.setMontoCopago(dto.getMontoCopago());
        nuevoBono.setMontoSeguro(dto.getMontoSeguro());
        nuevoBono.setFechaEmision(dto.getFechaEmision());

        return mapToDto(bonoRepository.save(nuevoBono));
    }

    @Transactional
    public BonoResponseDto actualizar(Long folio, BonoRequestDto dto) {
        Bono existente = bonoRepository.findById(folio)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un bono con el folio " + folio));

        existente.setMontoCopago(dto.getMontoCopago());
        existente.setMontoSeguro(dto.getMontoSeguro());
        existente.setFechaEmision(dto.getFechaEmision());
        existente.setCodPrevision(dto.getCodPrevision());
        existente.setRun(dto.getRun());

        return mapToDto(bonoRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long folio) {
        if (!bonoRepository.existsById(folio)) {
            throw new RecursoNoEncontradoException("No existe un bono con el folio " + folio);
        }
        bonoRepository.deleteById(folio);
    }
}