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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            prestamoServicio.carcarPrestamo(cliente.getId(), idLibro, date);
            modelo.put("exito", "Prestamo registrado con exito.");
            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
            return "cargarPrestamo.html";
        } catch (ErrorServicio ex) {
            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
            modelo.put("error", "Falto algun dato.");
            return "cargarPrestamo.html";
        }
    }

    @GetMapping("/listado")
    public String lista(HttpSession session, ModelMap modelo) {
        //recupero el usuario logueado
        Cliente login = (Cliente) session.getAttribute("clientesession");
        if (login == null ) {
            return "redirect:/login";
        }
        List<Prestamo> prestamos = prestamoServicio.listarPrestamosPorCliente(login.getId());
        modelo.put("prestamos", prestamos);
        /*
        tal vez deba hacer una query que busque prestamos
        por id de cliente
         */
        return "misPrestamos";
    }

    @GetMapping("/modificarPrestamo/{id}")
    public String modificar(HttpSession session, ModelMap modelo, @PathVariable String id) throws ErrorServicio {
        Cliente login = (Cliente) session.getAttribute("clientesession");
        if (login == null ) {
            return "redirect:/login";
        }
        modelo.put("prestamo", prestamoServicio.buscarPorId(id));
        return "modificarPrestamo";
    }

    @PostMapping("/modificarPrestamo/{id}")
    public String modificar(HttpSession session,
            ModelMap modelo,
            @PathVariable String id,
            @RequestParam String devolucion) throws ParseException {
        try {
            Cliente login = (Cliente) session.getAttribute("clientesession");
            if (login == null ) {
                return "redirect:/login";
            }
            Cliente cliente = (Cliente) session.getAttribute("clientesession");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(devolucion);
            prestamoServicio.modificarPrestamo(cliente.getId(), id, date);
            return "redirect:/prestamos/listado";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "listaPrestamos";
        }
    }
}
