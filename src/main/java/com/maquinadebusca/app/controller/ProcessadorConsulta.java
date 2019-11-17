package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Consulta;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ProcessadorConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping ("/processador") // URL: http://localhost:8080/processador
public class ProcessadorConsulta {

  @Autowired
  ProcessadorConsultaService pcs;

  // URL: http://localhost:8080/processador/consulta/{consultaDoUsuario}
  @GetMapping (value = "/consulta/{consultaDoUsuario}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity consultar (@PathVariable ("consultaDoUsuario") String textoConsulta) {
    Consulta consulta = pcs.processarConsulta (textoConsulta);
    ResponseEntity resp;

    if (!consulta.getRanking ().isEmpty ()) {
      resp = new ResponseEntity (consulta, HttpStatus.OK);
    } else {
      resp = new ResponseEntity (new Mensagem ("erro", "o índice invertido não pode ser criado"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return resp;
  }

}
