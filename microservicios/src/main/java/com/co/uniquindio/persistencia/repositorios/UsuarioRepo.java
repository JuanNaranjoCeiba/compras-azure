package com.co.uniquindio.persistencia.repositorios;

import com.co.uniquindio.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario,Integer> {
    @Query("select u from Usuario u where u.correo = :correo")
    Optional<Usuario> findByCorreo(String correo);
}
