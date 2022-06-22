package com.company.springcloud.msvc.cursos.controllers;

import com.company.springcloud.msvc.cursos.models.Usuario;
import com.company.springcloud.msvc.cursos.models.entity.Curso;
import com.company.springcloud.msvc.cursos.services.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    CursoService cursoService;

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok( cursoService.findAll() );
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById( @PathVariable Long id ){

        Optional< Curso > o = cursoService.porIdConUsuarios( id );
        if( o.isPresent() ){
            return ResponseEntity.ok().body( o.get() );
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Curso curso, BindingResult result ) {

        if( result.hasErrors() ){
            return validate( result );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(curso));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

        if( result.hasErrors() ){
            return validate( result );
        }

        Optional< Curso > o = cursoService.findById( id );

        if (o.isPresent()) {
            Curso cursoDB = o.get();
            cursoDB.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body( cursoService.save( cursoDB) );
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById( @PathVariable Long id ) {
        Optional<Curso> o = cursoService.findById( id );

        if ( o.isPresent() ) {
            cursoService.deleteById( o.get().getId() );
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId ){

        Optional< Usuario > o;

        try {
            o = cursoService.asignarUsuario( usuario, cursoId );
        } catch (FeignException ex ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por el ID o error en la comunicación: " +
                            ex.getMessage() ) );
        }

        if( o.isPresent() ){
            return ResponseEntity.status(HttpStatus.CREATED).body( o.get() );
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId ){

        Optional< Usuario > o;

        try {
            o = cursoService.crearUsuario( usuario, cursoId );
        } catch (FeignException ex ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .body(Collections.singletonMap("mensaje", "No se puede crear el usuario o error en la comunicación: " +
                     ex.getMessage() ) );
        }

        if( o.isPresent() ){
            return ResponseEntity.status(HttpStatus.CREATED).body( o.get() );
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{id}")
    public ResponseEntity<?> eliminarUsuarioPorId( @PathVariable Long id ){
        cursoService.eliminarCursoUsuarioPorId( id );
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put( err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage() );
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
