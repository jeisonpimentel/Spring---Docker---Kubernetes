package com.company.springcloud.msvc.cursos.clients;

import com.company.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/findById/{id}")
    Usuario findById( @PathVariable Long id );

    @PostMapping("/create")
    Usuario create( @RequestBody Usuario usuario );

    @GetMapping("/findAllByIds")
    List<Usuario> findAllByIds(@RequestParam Iterable<Long> ids);

}
