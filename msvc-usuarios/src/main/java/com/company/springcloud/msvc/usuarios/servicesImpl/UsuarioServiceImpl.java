package com.company.springcloud.msvc.usuarios.servicesImpl;

import com.company.springcloud.msvc.usuarios.clients.CursoClientRest;
import com.company.springcloud.msvc.usuarios.models.entity.Usuario;
import com.company.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import com.company.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService  {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoClientRest cursoClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> getById(Long id) {
        return usuarioRepository.findById( id );
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save( usuario );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById( id );
        cursoClientRest.eliminarUsuarioPorId( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByCorreo( email );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllById(Iterable<Long> ids) {
        return (List<Usuario>) usuarioRepository.findAllById( ids );
    }
}
