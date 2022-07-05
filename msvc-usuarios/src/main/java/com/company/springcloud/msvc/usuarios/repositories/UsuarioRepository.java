package com.company.springcloud.msvc.usuarios.repositories;

import com.company.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo( String correo );
}
