package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Foto;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    //este metodo devuelve la foto creada y persistida
    //MultipartFile es el archivo donde se almacena la foto
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                //getContentType devuelve el tipo mime
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                //getBytes puede fallar por eso el try catch
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    //este metodo actualizar sirve tanto para el caso de que
    //el cliente tenga foto o no
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {
        if(archivo != null){
            try {                
                Foto foto = new Foto();
                
                if(idFoto != null){
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if(respuesta.isPresent()){
                        foto = respuesta.get();
                    }
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);            
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }   return null;        
    }
}
