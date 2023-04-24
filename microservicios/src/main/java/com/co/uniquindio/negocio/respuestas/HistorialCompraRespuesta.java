package com.co.uniquindio.negocio.respuestas;

import com.co.uniquindio.persistencia.entidades.Producto;
import com.co.uniquindio.persistencia.enums.EnumCompra;
import com.co.uniquindio.persistencia.enums.EnumMedioPago;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@AllArgsConstructor
@ToString
@Builder
public class HistorialCompraRespuesta {
    @JsonProperty("total_compra")
    private BigDecimal totalCompra;

    @JsonProperty("medio_pago")
    private EnumMedioPago medioPago;

    @JsonProperty("estado_compra")
    private EnumCompra estado;

    @JsonProperty("fecha_compra")
    private Date fecha;

    @JsonProperty("numero_factura")
    private String numeroFactura;

    @JsonProperty("correo_usuario")
    private String correoUsuario;

    @JsonProperty("productos_compra")
    private List<Producto> productos;

}
