package com.libreriaSpring.ej1Spring.repositorios;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String>{ //tipo Autor, clave primaria String
    
    @Query ("SELECT a FROM Autor a")
    public List<Autor> listarAutores();
    
    @Query ("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public List<Autor> buscarAutorPorNombre(String nombre);
}