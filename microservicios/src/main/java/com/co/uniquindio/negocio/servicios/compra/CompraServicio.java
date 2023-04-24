package com.co.uniquindio.negocio.servicios.compra;



import com.co.uniquindio.negocio.respuestas.*;
import com.co.uniquindio.persistencia.dto.CompraDTO;

import java.util.List;

public interface CompraServicio {

    CancelarCompraRespuesta cancelarCompra(Integer id)throws Exception;
    EstadoCompraRespuesta estadoCompra(Integer id)throws Exception;
    CrearCompraRespuesta crearCompra(CompraDTO compra)throws Exception;
    List<HistorialCompraRespuesta>historialCompras(String correo)throws Exception;
    DetalleCompraRespuesta detalleCompra(String numeroFactura) throws Exception;
}
