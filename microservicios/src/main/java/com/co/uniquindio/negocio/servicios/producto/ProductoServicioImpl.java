package com.co.uniquindio.negocio.servicios.producto;

import com.co.uniquindio.persistencia.entidades.Producto;
import com.co.uniquindio.persistencia.repositorios.ProductoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductoServicioImpl implements ProductoServicio{

    public final ProductoRepo productoRepo;

    public ProductoServicioImpl(ProductoRepo productoRepo) {
        this.productoRepo = productoRepo;
    }

    @Override
    public List<Producto> listaProductos() throws Exception {
        return productoRepo.findAll();
    }
}
