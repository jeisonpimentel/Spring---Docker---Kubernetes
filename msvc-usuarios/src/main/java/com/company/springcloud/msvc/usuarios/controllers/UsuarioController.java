package com.company.springcloud.msvc.usuarios.controllers;

import com.company.springcloud.msvc.usuarios.models.entity.Usuario;
import com.company.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController()
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        Optional<Usuario> u = usuarioService.getById(id);

        if (u.isPresent()) {
            return ResponseEntity.ok().body(u.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Usuario usuario, BindingResult result ) {

        if( usuarioService.findByEmail( usuario.getCorreo() ).isPresent() ){
            return ResponseEntity.badRequest().body(Collections.singletonMap("Error email duplicado",
                    "Ya existe un usuario registrado con el correo: " + usuario.getCorreo() ) );
        }

        if( result.hasErrors() ){
            return validate( result );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {

        if( result.hasErrors() ){
            return validate( result );
        }

        Optional<Usuario> o = usuarioService.getById(id);
        if (o.isPresent()) {
            Usuario usuarioDB = o.get();

            if( !usuario.getCorreo().equalsIgnoreCase( usuarioDB.getCorreo() ) && usuarioService.findByEmail( usuario.getCorreo() ).isPresent() ){
                return ResponseEntity.badRequest().body(Collections.singletonMap("Error email duplicado",
                        "Ya existe un usuario registrado con el correo: " + usuario.getCorreo() ) );
            }

            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setCorreo(usuario.getCorreo());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<Usuario> o = usuarioService.getById(id);

        if (o.isPresent()) {
            usuarioService.deleteById( o.get().getId() );
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findAllByIds")
    public ResponseEntity<?> findAllByIds( @RequestParam List<Long> ids ){
        return ResponseEntity.ok( usuarioService.findAllById( ids ) );
    }

    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put( err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage() );
        });
        return ResponseEntity.badRequest().body(errores);
    }

}