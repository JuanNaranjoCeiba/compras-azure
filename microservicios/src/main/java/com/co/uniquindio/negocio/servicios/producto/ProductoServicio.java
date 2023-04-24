package com.co.uniquindio.negocio.servicios.producto;



import com.co.uniquindio.persistencia.entidades.Producto;

import java.util.List;

public interface ProductoServicio {

    List<Producto>listaProductos()throws Exception;
}
