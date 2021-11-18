package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
}
