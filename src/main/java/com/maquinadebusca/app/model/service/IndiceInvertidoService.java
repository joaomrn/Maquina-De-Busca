package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.IndiceInvertido;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.IndiceInvertidoRepository;

@Service
public class IndiceInvertidoService {

  @Autowired
  IndiceInvertidoRepository iir;

  public IndiceInvertidoService () {
  }

  public List<IndiceInvertido> getEntradasIndiceInvertido (String termo) {
    return iir.getEntradasIndiceInvertido (termo);
  }

}
