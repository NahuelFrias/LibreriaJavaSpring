package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorServicio as;
    @Autowired
    private EditorialServicio es;

    /*
    Si el metodo se ejecuta correctamente se hace un commit a la base de datos
    Si salta una excepcion se vuelve atras y no se realizan los cambios
     */
    @Transactional
    public void registrarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares,
            Integer ejemplaresPrestados, String idAutor, String idEditorial) throws ErrorServicio {

        Autor autor = as.buscarPorId(idAutor);
        Editorial editorial = es.buscarPorId(idEditorial);

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);

        Libro libro = new Libro();
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(as.registrarAutor(autor.getNombre()));
        libro.setEditorial(es.registrarEditorial(editorial.getNombre()));
        libro.setAlta(Boolean.TRUE);

        libroRepositorio.save(libro);
    }

    @Transactional
    public void modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            String idAutor, String idEditorial) throws ErrorServicio {

        Autor autor = as.buscarPorId(idAutor);
        Editorial editorial = es.buscarPorId(idEditorial);

        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);

        Optional<Libro> respuesta = libroRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);

        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    @Transactional
    public Libro alta(String id) throws ErrorServicio {
        Libro libro = libroRepositorio.getById(id);
        libro.setAlta(true);
        return libroRepositorio.save(libro);
    }

    @Transactional
    public Libro baja(String id) throws ErrorServicio {
        Libro libro = libroRepositorio.getById(id);
        libro.setAlta(false);
        return libroRepositorio.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    /*
    Se puede hacer de esta manera
    o con lenguaje sql en el repositorio
     */
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            return libro;
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarLibroPorTitulo(String titulo) {
        return libroRepositorio.buscarLibroPorTitulo(titulo);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarLibroPorEditorial(String editorial) {
        return libroRepositorio.buscarLibroPorEditorial(editorial);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarLibroPorAutor(String autor) {
        return libroRepositorio.buscarLibroPorAutor(autor);
    }

    @Transactional
    public void bajaLibro(String id) throws ErrorServicio {

        Optional<Libro> respuesta = libroRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.FALSE);
            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro.");
        }
    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares,
            Integer ejemplaresPrestados, Autor autor, Editorial editorial) throws ErrorServicio {

        if (isbn == null) {
            throw new ErrorServicio("El isbn no puede ser nulo.");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo.");
        }
        if (anio == null) {
            throw new ErrorServicio("El año no puede ser nulo.");
        }
        if (autor == null) {
            throw new ErrorServicio("El autor no puede ser nulo.");
        }
        if (editorial == null) {
            throw new ErrorServicio("La editorial no puede ser nula.");
        }
        if (ejemplares == null) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser nula.");
        }
        if (ejemplaresPrestados == null) {
            throw new ErrorServicio("La cantidad de ejemplares prestados no puede ser nula.");
        }
    }
}
