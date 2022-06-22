package com.company.springcloud.msvc.cursos.iservices;

import com.company.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.company.springcloud.msvc.cursos.models.Usuario;
import com.company.springcloud.msvc.cursos.models.entity.Curso;
import com.company.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.company.springcloud.msvc.cursos.repositories.CursoRepository;
import com.company.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    UsuarioClientRest usuarioClientRest;

    @Transactional(readOnly = true)
    @Override
    public List<Curso> findAll() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Curso> findById(Long id) {
        return cursoRepository.findById( id );
    }

    @Transactional
    @Override
    public Curso save(Curso curso) {
        return cursoRepository.save( curso );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cursoRepository.deleteById( id );
    }

    @Transactional
    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional< Curso > o = cursoRepository.findById( cursoId );
        if(o.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest.findById( usuario.getId() );

            Curso curso               = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioMsvc.getId() );
            curso.addCursoUsuario( cursoUsuario );
            cursoRepository.save( curso );
            return Optional.of( usuarioMsvc );
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional< Curso > o = cursoRepository.findById( cursoId );
        if(o.isPresent()){
            Usuario usuarioCreado = usuarioClientRest.create( usuario );

            Curso curso               = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioCreado.getId() );
            curso.addCursoUsuario( cursoUsuario );
            cursoRepository.save( curso );
            return Optional.of( usuarioCreado );
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {

        Optional<Curso> o = cursoRepository.findById( id );
        if( o.isPresent()){
            Curso curso = o.get();

            if( !curso.getCursoUsuarios().isEmpty() ) {
                List<Long> ids = curso.getCursoUsuarios()
                        .stream()
                        .map( cu -> cu.getUsuarioId() )
                        .collect( Collectors.toList() );

                List<Usuario> usuarios = usuarioClientRest.findAllByIds( ids );
                curso.setUsuarios( usuarios );
            }
            return Optional.of( curso );
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId( id );
    }

    /*
    @Transactional
    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional< Curso > o = cursoRepository.findById( cursoId );
        if(o.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest( usuario );

            Curso curso               = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId( usuarioMsvc.getId() );
            curso.removeCursoUsuario( cursoUsuario );
            cursoRepository.save( curso );
            return Optional.of( usuarioMsvc );
        }
        return Optional.empty();
    }
    */

}
