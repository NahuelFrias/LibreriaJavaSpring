package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.mail = :mail")
    public Cliente buscarPorMail(@Param("mail") String mail);
    
    @Query("SELECT c FROM Cliente c WHERE c.nombre = :nombre")
    public Cliente buscarPorNombre(@Param("nombre") String nombre);
}
