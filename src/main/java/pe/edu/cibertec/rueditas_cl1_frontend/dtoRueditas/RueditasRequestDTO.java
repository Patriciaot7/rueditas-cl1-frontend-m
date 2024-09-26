package pe.edu.cibertec.rueditas_cl1_frontend.dtoRueditas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RueditasRequestDTO {

    //validaciones iniciales
    @NotBlank(message = "Se queriere el campo placa")
    @Pattern(regexp = "^[A-Z0-9]{3}-[0-9]{3}$", message = "Debe ingresar una placa alfanumérica correcta")
    @Size(min = 7, max = 7, message = "Válido solo 7 caracteres")
    private String placa;

    public RueditasRequestDTO(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}

