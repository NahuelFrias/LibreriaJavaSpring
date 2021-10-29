package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.EditorialRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {
    
    @Autowired
    //no hace falta inicializar esta variable
    //ya que con autowired lo incializa el servidor de aplicaciones
    private EditorialRepositorio editorialRepositorio;

    public void registrarEditorial(String nombre) throws ErrorServicio {

        //el id se generaba automaticamente
        //si se dispara algun error de servicio no se crea la entidad!
        validar(nombre);

        Editorial editorial= new Editorial();
        editorial.setNombre(nombre);
        //consultar!!
        editorial.setAlta(true);

        //el repositorio almacena el objeto
        editorialRepositorio.save(editorial);
    }

    public void modificarEditorial(String id, String nombre) throws ErrorServicio {

        //validamos si los datos son correctos
        validar(nombre);

        //buscamos el usuario por id y lo devolvemos
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial= respuesta.get();
            editorial.setNombre(nombre);
            //actualizamos
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }
    
    public void eliminarEditorial(String id) throws ErrorServicio{
        
         Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            //actualizamos
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
        
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

    }
    
}
