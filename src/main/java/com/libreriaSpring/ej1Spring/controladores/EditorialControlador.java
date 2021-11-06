package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Editorial;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/cargarEditorial")
    public String cargarEditorial() {
        return "cargarEditorial.html";
    }
    
    @PostMapping("/registrandoEditorial")
    public String registrandoEditorial(ModelMap modelo, @RequestParam String nombre) {
        try {
            editorialServicio.registrarEditorial(nombre);
            modelo.put("exito", "La editorial fue registrada con exito!");
            return "cargarEditorial.html";
        } catch (ErrorServicio ex) {
            //muestro el error
            modelo.put("error", "Falto algun dato.");
            modelo.put("nombre", nombre);
            return "cargarEditorial.html";
        }
        
    }
    
    @GetMapping("/listaEditoriales")
    public String listaEditoriales(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.put("editoriales", editoriales);
        return "listaEditoriales.html";
    }
    
}
