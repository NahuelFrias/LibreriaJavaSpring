package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.AutorServicio;
import com.libreriaSpring.ej1Spring.servicios.EditorialServicio;
import com.libreriaSpring.ej1Spring.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author leonahuel
 */
@Controller("")
@RequestMapping("/")
/*url que escucha este controlador!
cuando entra a la barra de mi servidor ejecuta todos estos metodos
y renderiza el html*/
public class PortalControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/cargas")
    public String cargas() {
        return "cargas.html";
    }

    @GetMapping("/listas")
    public String listas() {
        return "listas.html";
    }

    @GetMapping("/cargarLibro")
    public String cargarLibro(ModelMap modelo) {
        //listo los autores y los paso  las vistas para el select de autores
        //cuando estoy carganto los libros
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "cargarLibro.html";
    }

    @GetMapping("/cargarAutor")
    public String cargarAutor() {
        return "cargarAutor.html";
    }

    @GetMapping("/cargarEditorial")
    public String cargarEditorial() {
        return "cargarEditorial.html";
    }

    @PostMapping("/registrandoLibro")
    /*ModelMap sirve para insertar toda la info que vamos a mostrar en las interfaces de usuario o pantalla
cuando en tu controlador pones un @RequestParam y vos lo pones vacio, cuando va al controlador espera a recibir ese parametro,
    y como es nulo no recibe nada,
    eso lo podes evitar poniendo como lo puso Gustavo o usando dentro de @RequestParam(required = false)
     */
    public String registrandoLibro(ModelMap modelo,
            @RequestParam @Nullable Long isbn,
            @RequestParam @Nullable String titulo,
            @RequestParam @Nullable Integer anio,
            @RequestParam @Nullable Integer ejemplares,
            @RequestParam @Nullable Integer prestados,
            @RequestParam @Nullable String idAutor,
            @RequestParam @Nullable String idEditorial) {
        try {
            libroServicio.registrarLibro(isbn, titulo, anio, ejemplares, prestados, idAutor, idEditorial);
        } catch (ErrorServicio ex) {
            //muestro el error
            modelo.put("error", ex.getMessage());
            //muestro los campos que si estaban llenos antes del error
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("año", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("prestados", prestados);
            List<Autor> autores = autorServicio.listarAutores();
            modelo.put("autores", autores);
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.put("editoriales", editoriales);
            return "cargarLibro.html";
        }
        modelo.put("titulo", "Registro de Libros");
        modelo.put("descripcion", "El Libro fue registrado con exito!");
        return "exito.html";
    }

    @PostMapping("/registrandoAutor")
    public String registrandoAutor(ModelMap modelo, @RequestParam String nombre) {
        try {
            autorServicio.registrarAutor(nombre);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "cargarAutor.html";
        }
        modelo.put("titulo", "Registro de Autores");
        modelo.put("descripcion", "El Autor fue registrado con exito!");
        return "exito.html";
    }

    @PostMapping("/registrandoEditorial")
    public String registrandoEditorial(ModelMap modelo, @RequestParam String nombre) {
        try {
            editorialServicio.registrarEditorial(nombre);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "cargarEditorial.html";
        }
        modelo.put("titulo", "Registro de Editoriales");
        modelo.put("descripcion", "La Editorial fue registrada con exito!");
        return "exito.html";
    }

    @GetMapping("/listaAutores")
    public String listaAutores(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);

        return "listaAutores.html";
    }

    @GetMapping("/listaEditoriales")
    public String listaEditoriales(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "listaEditoriales.html";
    }

    @GetMapping("/listaLibros")
    public String listaLibros(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "listaLibros.html";
    }

}
