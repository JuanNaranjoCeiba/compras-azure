package com.co.uniquindio.web.rest;

import com.co.uniquindio.negocio.servicios.producto.ProductoServicio;
import com.co.uniquindio.persistencia.entidades.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {

    public final ProductoServicio productoServicio;

    public ProductoRestController(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public ResponseEntity<?>listarProductos() throws Exception {
        List<Producto>listaProductos = productoServicio.listaProductos();
        return new ResponseEntity<>(listaProductos, HttpStatus.OK);
    }
}
