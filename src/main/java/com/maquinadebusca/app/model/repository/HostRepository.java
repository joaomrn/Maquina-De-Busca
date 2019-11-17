package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.Host;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HostRepository extends JpaRepository<Host, Long>{
    @Override
    List<Host> findAll();
    
    @Override
    Host save(Host host);
    
    @Override
    void deleteById(Long id);

    List<Host> findByHostIgnoreCaseContaining(String host);
    
    @Query (value = "SELECT * FROM host ORDER BY host", nativeQuery = true)
    List<Host> getInLexicalOrder ();
}
