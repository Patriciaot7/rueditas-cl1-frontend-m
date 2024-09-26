package pe.edu.cibertec.rueditas_cl1_frontend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas.RueditasRequestDTO;
import pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas.RueditasResponseDTO;
import pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas.BuscarRuedaRequestDTO;
import pe.edu.cibertec.rueditas_cl1_frontend.viewModel.busquedaModel;

@Controller
@RequestMapping("/rueda")
public class RueditasController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/buscar")
    public  String buscarRueda(Model model){
        busquedaModel busquedaModel = new busquedaModel("00", "", "", "", "", "", "");
        model.addAttribute("rueditasModel", busquedaModel);
        return "inicio";
    }

    @PostMapping("/resultados")
    public String resultados(@Validated RueditasRequestDTO requestDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            // Devolver los mensajes de error.
            busquedaModel rueditasModel = new busquedaModel(
                    "01", bindingResult.getFieldError().getDefaultMessage(), "", "", "", "", ""
            );
            model.addAttribute("rueditasModel", rueditasModel);
            return "inicio";
        }
        try {
            String endpoint = "http://localhost:8081/rueda/busqueda";

            RueditasResponseDTO responseDTO = restTemplate.postForObject(endpoint, requestDTO, RueditasResponseDTO.class);

            if (responseDTO.codigo().equals("00")){
                busquedaModel rueditasModel = new busquedaModel(
                        "00",
                        "",
                        responseDTO.marca(),
                        responseDTO.modelo(),
                        responseDTO.nroAsientos(),
                        responseDTO.precio(),
                        responseDTO.color()
                );
                model.addAttribute("rueditasModel", rueditasModel);
                return "principal";
            }else{
                busquedaModel rueditasModel = new busquedaModel(
                        "01", "No se encontró un vehículo para la placa ingresada",
                        "", "", "", "", "");
                model.addAttribute("rueditasModel", rueditasModel);
                return "inicio";
            }

        } catch (Exception e) {
            busquedaModel rueditasModel = new busquedaModel(
                    "99", "Error al encontrar un vehículo",
                    "", "", "", "", "");
            model.addAttribute("rueditasModel", rueditasModel);

            return "inicio";
        }

    }
}