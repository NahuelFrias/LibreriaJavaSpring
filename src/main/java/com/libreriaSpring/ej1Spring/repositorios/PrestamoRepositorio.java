package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
    @Query("SELECT p FROM  Prestamo p WHERE p.cliente.id = :id")
    public List<Prestamo>listarPrestamosPorCliente(@Param("id") String id);
}
