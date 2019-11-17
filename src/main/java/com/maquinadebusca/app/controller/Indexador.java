package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.IndexadorService;
import com.maquinadebusca.app.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/indexador") // URL: http://localhost:8080/indexador
public class Indexador {

    @Autowired
    IndexadorService is;

    @Autowired
    UsuarioService us;
    
    private final ResponseEntity mensagemErro = new ResponseEntity(new Mensagem("Erro", "Usuário Inválido, apenas Administradores podem efetuar esta ação."), HttpStatus.INTERNAL_SERVER_ERROR);


    // URL: http://localhost:8080/indexador/indice
    @PostMapping(value = "/indice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity criarIndice() {
        if(us.getPermissao().equals("Admin")){
            boolean confirmacao = is.criarIndice();
            ResponseEntity resp;
            if (confirmacao) {
                resp = new ResponseEntity(new Mensagem("Sucesso", "O índice invertido foi criado com sucesso"), HttpStatus.CREATED);
            } else {
                resp = new ResponseEntity(new Mensagem("Erro", "O índice invertido não pode ser criado"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return resp;
        }else
            return mensagemErro;
    }

    // URL: http://localhost:8080/indexador/documento
    @GetMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getDocumento() {
        if(us.getPermissao().equals("Admin")){
            return new ResponseEntity(is.lerDocumentos(), HttpStatus.CREATED);
        }else
            return mensagemErro;
    }
}
