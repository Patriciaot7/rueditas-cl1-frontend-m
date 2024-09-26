package pe.edu.cibertec.rueditas_cl1_frontend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas.RueditasRequestDTO;
import pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas.RueditasResponseDTO;
import pe.edu.cibertec.rueditas_cl1_frontend.viewModel.BusquedaModel;

@Controller
@RequestMapping("/rueda")
public class RueditaController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String inicio(Model model) {
        BusquedaModel busquedaModel = new BusquedaModel("00", "", "", "", "", "", "");
        model.addAttribute("busquedaModel", busquedaModel);
        return "inicio";
    }

    @PostMapping("/resultados")
    public String buscar(@RequestParam("placa") String placa, Model model){

        //validar campo
        if (placa == null || placa.trim().length() == 0 || placa.trim().length() >= 8) {
            BusquedaModel busquedaModel = new BusquedaModel("01", "Debe ingresar una placa correcta", "", "", "", "", "");
            model.addAttribute("busquedaModel", busquedaModel);
            return "inicio";
        }

        try {
            //invocar servicio de busqueda
            String endpoint = "http://localhost:8081/rueda/busqueda";
            RueditasRequestDTO busquedaRequestDTO = new RueditasRequestDTO(placa);
            RueditasResponseDTO busquedaResponseDTO = restTemplate.postForObject(endpoint, busquedaRequestDTO, RueditasResponseDTO.class);

            if (busquedaResponseDTO.codigo().equals("00")) {
                BusquedaModel busquedaModel = new BusquedaModel("00", "" ,busquedaResponseDTO.marca(), busquedaResponseDTO.modelo()
                        , busquedaResponseDTO.nroAsientos(), busquedaResponseDTO.precio(), busquedaResponseDTO.color());
                model.addAttribute("busquedaModel", busquedaModel);
                return "principal";
            } else {
                BusquedaModel busquedaModel = new BusquedaModel("01", "No se encontró un vehículo para la placa ingresada", "", "", "", "", "");
                model.addAttribute("busquedaModel", busquedaModel);
                return "inicio";
            }


        }catch (Exception e){
            BusquedaModel busquedaModel = new BusquedaModel("01", "Error: No se encontró un vehículo para la placa ingresada", "", "", "", "", "");
            model.addAttribute("busquedaModel", busquedaModel);
            return "inicio";
        }


    }




}