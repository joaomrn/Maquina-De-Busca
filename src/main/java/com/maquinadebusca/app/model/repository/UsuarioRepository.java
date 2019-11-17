package com.maquinadebusca.app.model.repository;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  @Override
    List<Usuario> findAll();

    Usuario findById(long id);

    Usuario findByNome(String nome);

    @Override
    Usuario save(Usuario usuario);

    @Override
    void delete(Usuario usuario);
    
    @Override
    void deleteById(Long id);
    
    List<Usuario> findByNomeIgnoreCaseContaining (String usuario);
    
    @Query (value = "SELECT * FROM usuario JOIN tipousuario WHERE tipousuario.permissao = 'Admin' AND tipousuario.id = usuario.usuario_id ORDER BY usuario.nome", nativeQuery = true)
    List<Usuario> getInLexicalOrder ();
    
    @Query (value = "SELECT * FROM usuario JOIN tipousuario WHERE tipousuario.permissao = 'Comum' AND tipousuario.id = usuario.usuario_id ORDER BY usuario.nome", nativeQuery = true)
    List<Usuario> getInLexicalOrderNome ();
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET nome = :nome WHERE nome = :atual", nativeQuery = true)
    int update(@Param("atual") String atual, @Param("nome") String nome);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET nome = :nome WHERE id = :id", nativeQuery = true)
    int updateById(@Param("id") Long id, @Param("nome") String nome);
  
}
