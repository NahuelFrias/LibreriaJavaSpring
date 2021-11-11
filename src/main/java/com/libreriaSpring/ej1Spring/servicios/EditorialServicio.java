package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {

    @Autowired
    //no hace falta inicializar esta variable
    //ya que con autowired lo incializa el servidor de aplicaciones
    private EditorialRepositorio editorialRepositorio;

    /*
    Si el metodo se ejecuta correctamente se hace un commit a la base de datos
    Si salta una excepcion se vuelve atras y no se realizan los cambios
     */
    @Transactional
    public Editorial registrarEditorial(String nombre) throws ErrorServicio {

        //el id se generaba automaticamente
        //si se dispara algun error de servicio no se crea la entidad!
        validar(nombre);

        Editorial e = buscarEditorialPorNombre(nombre);

        if (e != null) {
            return e;
        } else {

            Editorial editorial = new Editorial();
            editorial.setNombre(nombre);
            //consultar!!
            editorial.setAlta(true);

            //el repositorio almacena el objeto
            editorialRepositorio.save(editorial);

            return editorial;
        }
    }

    @Transactional
    public Editorial modificarEditorial(String id, String nombre) throws ErrorServicio {

        //validamos si los datos son correctos
        validar(nombre);

        //buscamos el usuario por id y lo devolvemos
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            //actualizamos
            editorialRepositorio.save(editorial);
            return editorial;
        } else {
            throw new ErrorServicio("No se encontro la editorial.");
        }
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        return editorialRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            return editorial;
        } else {
            throw new ErrorServicio("No se encontro la Editorial.");
        }
    }

    @Transactional(readOnly = true)
    public Editorial buscarEditorialPorNombre(String nombre) {
        return editorialRepositorio.buscarEditorialPorNombre(nombre);
    }

    @Transactional
    public Editorial alta(String id) throws ErrorServicio {

        Editorial editorial = editorialRepositorio.getById(id);
        editorial.setAlta(true);
        return editorialRepositorio.save(editorial);
    }

    @Transactional
    public Editorial baja(String id) throws ErrorServicio {

        Editorial editorial = editorialRepositorio.getById(id);
        editorial.setAlta(false);
        return editorialRepositorio.save(editorial);
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }
    }
}
