package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.maquinadebusca.app.model.repository.DocumentoRepository;

@Service
public class DocumentoService {

  @Autowired
  DocumentoRepository dr;

  public DocumentoService () {
  }

  public Documento save (Documento documento) {
    return dr.save (documento);
  }

}
