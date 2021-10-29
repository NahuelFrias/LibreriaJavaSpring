package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.AutorRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    //no hace falta inicializar esta variable
    //ya que con autowired lo incializa el servidor de aplicaciones
    private AutorRepositorio autorRepositorio;

    public void registrarAutor(String nombre) throws ErrorServicio {

        //el id se generaba automaticamente
        //si se dispara algun error de servicio no se crea la entidad!
        validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        //consultar!!
        autor.setAlta(Boolean.TRUE);

        //el repositorio almacena el objeto
        autorRepositorio.save(autor);
    }

    public void modificarAutor(String id, String nombre) throws ErrorServicio {

        //validamos si los datos son correctos
        validar(nombre);

        //buscamos el usuario por id y lo devolvemos
        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            //actualizamos
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
    }
    
    public void eliminarAutor(String id) throws ErrorServicio{
        
         Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.FALSE);
            //actualizamos
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
        
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }
    }
}
