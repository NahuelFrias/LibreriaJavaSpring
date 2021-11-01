package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository <Libro, String> {
    
    @Query("SELECT l FROM Libro l")
    public List<Libro> listarLibros();
    
    @Query ("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public List<Libro> buscarLibroPorTitulo(@Param ("titulo") String titulo);
    
    @Query ("SELECT l FROM Libro l WHERE l.autor.nombre = :autor")
    public List<Libro> buscarLibroPorAutor(@Param ("autor") String autor);
    
    @Query ("SELECT l FROM Libro l WHERE l.editorial.nombre = :editorial")
    public List<Libro> buscarLibroPorEditorial(@Param ("editorial") String editorial);
}
