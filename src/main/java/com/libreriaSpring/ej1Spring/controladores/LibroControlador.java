package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.AutorServicio;
import com.libreriaSpring.ej1Spring.servicios.EditorialServicio;
import com.libreriaSpring.ej1Spring.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author leonahuel
 */
@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

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
            modelo.put("exito", "El Libro fue registrado con exito!");
            return "cargarLibro.html";
        } catch (ErrorServicio ex) {
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
            //muestro el error
            modelo.put("error", "Falto algun dato.");
            return "cargarLibro.html";
        }
    }

    @GetMapping("/listaLibros")
    public String listaLibros(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "listaLibros.html";
    }

    @GetMapping("/modificarLibro/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id) throws ErrorServicio {
        modelo.put("libro", libroServicio.buscarPorId(id));
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "modificarLibro";
    }

    @PostMapping("/modificarLibro/{id}")
    public String modificar(ModelMap modelo,
            @PathVariable String id,
            @RequestParam @Nullable Long isbn,
            @RequestParam @Nullable String titulo,
            @RequestParam @Nullable Integer anio,
            @RequestParam @Nullable Integer ejemplares,
            @RequestParam @Nullable Integer prestados,
            @RequestParam @Nullable String idAutor,
            @RequestParam @Nullable String idEditorial) {
        try {
            libroServicio.modificarLibro(id, isbn, titulo, anio, ejemplares, prestados, idAutor, idEditorial);

            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
            modelo.put("exito", "El Libro fue modificado con exito!");
            return "listaLibros.html";
        } catch (ErrorServicio ex) {
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
            //muestro el error
            modelo.put("error", "Falto algun dato.");
            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
            return "listaLibros.html";
        }
    }

    @GetMapping("/altaLibro/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {
         try {
            libroServicio.alta(id);
            modelo.put("exito", "Modificacion exitosa");
            return "redirect:/libro/listaLibros";
        } catch (ErrorServicio ex) {
            return "redirect:/";
        }
    }

    @GetMapping("/bajaLibro/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {
        try {
            libroServicio.baja(id);
            modelo.put("exito", "Modificacion exitosa");
            return "redirect:/libro/listaLibros";
        } catch (ErrorServicio ex) {
            return "redirect:/";
        }
    }
}
