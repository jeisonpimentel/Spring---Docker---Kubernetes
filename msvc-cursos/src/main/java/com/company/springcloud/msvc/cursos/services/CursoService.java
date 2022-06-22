package com.company.springcloud.msvc.cursos.services;

import com.company.springcloud.msvc.cursos.models.Usuario;
import com.company.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> findAll();

    Optional<Curso> findById(Long id );

    Curso save( Curso curso );

    void deleteById( Long id );

    Optional<Usuario> asignarUsuario( Usuario usuario, Long cursoId );
    Optional<Usuario> crearUsuario( Usuario usuario, Long cursoId );
    //Optional<Usuario> eliminarUsuario( Usuario usuario, Long cursoId );

    Optional<Curso> porIdConUsuarios( Long id );
    void eliminarCursoUsuarioPorId( Long id );
}
