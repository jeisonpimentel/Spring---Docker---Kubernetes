package com.company.springcloud.msvc.usuarios.services;

import com.company.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();
    Optional<Usuario> getById( Long id );
    Usuario save( Usuario usuario );
    void deleteById( Long id );
    Optional<Usuario> findByEmail( String email );
    List<Usuario> findAllById( Iterable<Long> ids );



}
