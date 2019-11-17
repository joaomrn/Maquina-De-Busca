package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.Documento;
import org.springframework.data.jpa.repository.Query;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Override
    List<Documento> findAll ();

    Documento findById (long id);
    
    @Override
    Documento save (Documento documento);

    Documento findByUrlIgnoreCaseContaining (String url);

    @Override
    void delete(Documento documento);
    
    @Override
    void deleteById(Long id);
    
    @Query (value = "SELECT * FROM documento ORDER BY url", nativeQuery = true)
    List<Documento> getInLexicalOrder ();
    
}
