package com.libreriaSpring.ej1Spring;

import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Ej1SpringApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClienteServicio clienteServicio;

    public static void main(String[] args) {
        SpringApplication.run(Ej1SpringApplication.class, args);
    }

    //este metodo le dice a SpringSecurity cual es el servicio
    //que va a utilizar para autentificar al cliente
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clienteServicio).passwordEncoder(new BCryptPasswordEncoder());
    }//y setea un encriptador de claves

}
