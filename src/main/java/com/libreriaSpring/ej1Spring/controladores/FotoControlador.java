package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/fotoCliente/{id}")
    public ResponseEntity<byte[]> fotoCliente(@PathVariable String id) {
        try {
            Cliente cliente = clienteServicio.buscarPorId(id);
            //el usuario puede no tener foto
            if(cliente.getFoto() == null){
                throw new ErrorServicio("El cliente no tiene una foto asignada");
            }
            byte[] foto = cliente.getFoto().getContenido();

            //esta cabecera le dice al navegador que estoy devolviendo una imagen
            HttpHeaders headers = new HttpHeaders();
            //le digo al navegador que tipo de headers es
            headers.setContentType(MediaType.IMAGE_JPEG);
            //ResponseEntity recibe el contenido, el headers o cabecera y el estado
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            //devuelvo un not found si hay errores
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
