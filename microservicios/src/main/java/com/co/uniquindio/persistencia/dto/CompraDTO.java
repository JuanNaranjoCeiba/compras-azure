package com.co.uniquindio.persistencia.dto;

import com.co.uniquindio.persistencia.enums.EnumMedioPago;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompraDTO {

    private EnumMedioPago medioPago;

    private UsuarioDTO usuario;

    private List<ProductoDTO>productos;
}
