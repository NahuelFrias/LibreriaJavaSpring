package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String>{
    
}
