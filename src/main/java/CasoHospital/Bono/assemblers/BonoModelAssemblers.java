package CasoHospital.Bono.assemblers;

import CasoHospital.Bono.controller.BonoController;
import CasoHospital.Bono.dto.BonoResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BonoModelAssemblers implements RepresentationModelAssembler<BonoResponseDto, EntityModel<BonoResponseDto>> {

@Override
public EntityModel<BonoResponseDto> toModel(BonoResponseDto bono){
    return EntityModel.of(bono,
            linkTo(methodOn(BonoController.class)
                    .buscarPorFolio(bono.getNroFolio()))
                    .withSelfRel(),
            linkTo(methodOn(BonoController.class)
                    .obtenerTodos(null, null))
                    .withRel("Todas-los-Bonos")
    );
}
}