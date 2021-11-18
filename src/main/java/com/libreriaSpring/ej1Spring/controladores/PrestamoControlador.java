package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.entidades.Prestamo;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.LibroServicio;
import com.libreriaSpring.ej1Spring.servicios.PrestamoServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@RequestMapping("/prestamos")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/carga")
    public String carga(ModelMap modelo) {
        
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "cargarPrestamo.html";
    }

    @PostMapping("/cargaPrestamo")
    public String cargaPrestamo(HttpSession session,
            ModelMap modelo,
            @RequestParam String idLibro,
            @RequestParam String devolucion
    ) throws ParseException {

        Cliente cliente = (Cliente) session.getAttribute("clientesession");
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(devolucion);
            prestamoServicio.carcarPrestamo(cliente.getId(), idLibro,date);
            modelo.put("exito", "Prestamo registrado con exito.");
            return "cargarPrestamo.html";
        } catch (ErrorServicio ex) {
            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
            modelo.put("error", "Falto algun dato.");
            return "cargarPrestamo.html";
        }
    }

    @GetMapping("/listado")
    public String lista(ModelMap modelo) {
        List<Prestamo> prestamos = prestamoServicio.listarPrestamos();
        modelo.put("prestamos", prestamos);
        /*
        tal vez deba hacer una query que busque prestamos
        por id de cliente
        */
        return "";
    }
}
