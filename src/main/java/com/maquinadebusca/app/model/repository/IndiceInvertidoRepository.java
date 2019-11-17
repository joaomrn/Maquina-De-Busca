package com.maquinadebusca.app.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.IdIndiceInvertido;
import com.maquinadebusca.app.model.IndiceInvertido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndiceInvertidoRepository extends JpaRepository<IndiceInvertido, IdIndiceInvertido> {

  @Query (value = "select  i.* " +
                              "from TermoDocumento t, IndiceInvertido i, Documento d " +
                              "where t.id = i.termo_id and " +
                              "          i.documento_id = d.id and " +
                              "          t.texto = :termoConsulta ", nativeQuery = true)
  List<IndiceInvertido> getEntradasIndiceInvertido (@Param ("termoConsulta") String termo);

}
