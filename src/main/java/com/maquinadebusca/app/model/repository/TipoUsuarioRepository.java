package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.TipoUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    @Override
    List<TipoUsuario> findAll();
    
    @Override
    TipoUsuario save (TipoUsuario tipoUsuario);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE tipousuario SET permissao = :permissao WHERE id = 3", nativeQuery = true)
    int update(@Param("permissao") String permissao);
}
