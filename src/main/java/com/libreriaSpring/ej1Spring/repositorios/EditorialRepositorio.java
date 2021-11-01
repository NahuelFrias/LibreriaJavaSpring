package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository <Editorial, String> {
    
    @Query("SELECT e FROM Editorial e")
    public List<Editorial> listarEditoriales();
    
    @Query ("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public List<Editorial> buscarEditorialPorNombre(String nombre);
}
