package com.company.springcloud.msvc.usuarios.controllers;

import com.company.springcloud.msvc.usuarios.models.entity.EmailRequest;
import com.company.springcloud.msvc.usuarios.servicesImpl.EmailServiceImpl;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    EmailServiceImpl emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail( @RequestBody EmailRequest request ){

        Response response = emailService.sendEmail( request );

        if( response.getStatusCode() == 200 || response.getStatusCode() == 202 ){
            return new ResponseEntity<>("send successfully!!", HttpStatus.OK );
        }

        return new ResponseEntity<>("failed to sent!!", HttpStatus.NOT_FOUND );
    }

}
