package com.co.uniquindio.persistencia.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductoDTO {

    private Integer id;
    private String referencia;
    private String nombre;
    private BigDecimal precio;
    private int cantidadCompra;
}
