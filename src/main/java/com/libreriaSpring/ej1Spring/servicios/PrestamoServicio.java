package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.entidades.Libro;
import com.libreriaSpring.ej1Spring.entidades.Prestamo;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private LibroServicio libroServicio;

    @Transactional
    public void carcarPrestamo(String idCliente, String idLibro, Date devolucion) throws ErrorServicio {

        Cliente cliente = clienteServicio.buscarPorId(idCliente);
        Libro libro = libroServicio.buscarPorId(idLibro);
        int cantidad = libro.getEjemplares();
        int prestados = libro.getEjemplaresPrestados();
        if (cantidad > 0) {

            libro.setEjemplares(cantidad -1);
            libro.setEjemplaresPrestados(prestados +1);
            
            Prestamo prestamo = new Prestamo();
            prestamo.setCliente(cliente);
            prestamo.setLibro(libro);
            prestamo.setAlta(true);
            prestamo.setFechaPrestamo(new Date());
            prestamo.setFechaDevolucion(devolucion);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("No hay libros disponibles.");
        }

    }

    @Transactional
    public void modificarPrestamo(String idCliente, String idPrestamo, Date devolucion) throws ErrorServicio {

        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);

        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (prestamo.getCliente().getId().equals(idCliente)) {
                prestamo.setFechaDevolucion(devolucion);
                prestamoRepositorio.save(prestamo);
            } else {
                throw new ErrorServicio("No tiene permisos para modificar este prestamo.");
            }
        } else {
            throw new ErrorServicio("No se encontro el prestamo solicitado.");
        }
    }

    @Transactional
    public void eliminar(String idCliente, String idPrestamo) throws ErrorServicio {
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (prestamo.getCliente().getId().equals(idCliente)) {
                prestamo.setAlta(false);
                prestamo.setFechaDevolucion(null);
                prestamoRepositorio.save(prestamo);
            } else {
                throw new ErrorServicio("No tiene permisos para modificar este prestamo.");
            }
        } else {
            throw new ErrorServicio("No se encontro el prestamo solicitado.");
        }
    }

    @Transactional(readOnly = true)
    public Prestamo buscarPorId(String id) throws ErrorServicio {

        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            return prestamo;
        } else {
            throw new ErrorServicio("No se encontro el prestamo solicitado.");
        }
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosPorCliente(String id) {
        return prestamoRepositorio.listarPrestamosPorCliente(id);
    }
}
