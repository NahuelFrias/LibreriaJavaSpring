package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.LibroRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    public void registrarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados);

        Libro libro = new Libro();
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAlta(Boolean.TRUE);
        //debo setear Autor y Editorial!!!

        libroRepositorio.save(libro);
    }

    public void modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes) throws ErrorServicio {

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados);

        Optional<Libro> respuesta = libroRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }
    
    public void eliminarLibro(String id) throws ErrorServicio{
        
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.FALSE);
            libroRepositorio.save(libro);
        }else{
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados) throws ErrorServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo.");
        }
        if (isbn == null) {
            throw new ErrorServicio("El isbn no puede ser nulo.");
        }
        if (anio == null) {
            throw new ErrorServicio("El año no puede ser nulo.");
        }
        if (ejemplares == null) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser nula.");
        }
        if (ejemplaresPrestados == null) {
            throw new ErrorServicio("La cantidad de ejemplares prestados no puede ser nula.");
        }
    }

}
