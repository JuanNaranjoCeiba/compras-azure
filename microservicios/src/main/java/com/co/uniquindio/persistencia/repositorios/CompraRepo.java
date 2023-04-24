package com.co.uniquindio.persistencia.repositorios;

import com.co.uniquindio.persistencia.entidades.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepo extends JpaRepository<Compra, Integer> {

    @Query("select c from Compra c join Usuario u ON c.usuario.id = u.id where u.correo = :correo")
    Optional<List<Compra>>historialCompras(String correo);

    @Query("select c from Compra c where c.numeroFactura = :numeroFactura")
    Optional<Compra>detalleCompra(String numeroFactura);
}
