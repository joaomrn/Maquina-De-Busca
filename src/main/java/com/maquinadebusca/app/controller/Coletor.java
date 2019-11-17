package com.maquinadebusca.app.controller;


import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.UrlsSemente;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.maquinadebusca.app.model.service.ColetorService;
import com.maquinadebusca.app.model.service.HostService;
import com.maquinadebusca.app.model.service.UsuarioService;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

    @Autowired
    ColetorService cs;
    
    @Autowired
    UsuarioService us;
    
    @Autowired
    HostService hs;
    
    private final ResponseEntity mensagemErroLink = new ResponseEntity(new Mensagem("Erro", "Os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
    private final ResponseEntity mensagemErro = new ResponseEntity(new Mensagem("Erro", "Usuário Inválido"), HttpStatus.INTERNAL_SERVER_ERROR);

    // URL: http://localhost:8080/coletor/iniciar
    @PostMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity iniciar() throws MalformedURLException {
        URL valorPadrao = new URL("https://www.google.com");
        List<URL> urlPadrao = new LinkedList();
        urlPadrao.add(valorPadrao);
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.executar(urlPadrao), HttpStatus.OK);
        }else
            return mensagemErro;
    }

    // URL: http://localhost:8080/coletor/documento
    @GetMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumento() {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.getDocumento(), HttpStatus.OK);
        }
        else
            return mensagemErro;
    }
    // Request for: http://localhost:8080/coletor/documento/{id}
    @GetMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumentoId(@PathVariable(value = "id") long id) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.getDocumento(id), HttpStatus.OK);
        }
        else
            return mensagemErro;
    }
    
    // Request for: http://localhost:8080/coletor/documento/encontrar/{documento}
    @GetMapping(value = "/documento/encontrar/{documento}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarDocumento(@PathVariable(value = "url") String url) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.encontrarDocumento(url), HttpStatus.OK);
        }
        else
            return mensagemErro;
    }
     
    // Request for: http://localhost:8080/coletor/documento/ordemAlfabetica
    @GetMapping(value = "/documento/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity documentoEmOrdemAlfabetica() {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.documentoOrdemAlfabetica(), HttpStatus.OK);
        }
        else
            return mensagemErro;
    }

    // URL: http://localhost:8080/coletor/link
    @GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLink() {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.getLink(), HttpStatus.OK);
        } else
            return mensagemErro;
    }
    
    // Request for: http://localhost:8080/coletor/link/{id}
    @GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLinkId(@PathVariable(value = "id") long id, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErroLink;
        }
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.getLink(id), HttpStatus.OK);
        } else
            return mensagemErro;
    }

    // Parametro => {"url":"https://www.una.br","ultimaColeta": "2018-11-19T17:13:43"}
    // Request for: http://localhost:8080/coletor/link
    @PostMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
        String msg = "";
        if (resultado.hasErrors()) {
            return mensagemErroLink;
        } else {
            if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
                link = cs.salvarLink(link);
                if ((link != null) && (link.getId() > 0)) {
                    return new ResponseEntity(link, HttpStatus.OK);                
                }else
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível inserir o link informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
            }else
                return mensagemErro;
        }
    }
    
    // Parametro => {"id": 2003,"url":"https://www.una.br","ultimaColeta": "2018-11-19T17:13:43"}
    // Request for: http://localhost:8080/coletor/link
    @PutMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity atualizarLink(@RequestBody @Valid Link link, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return mensagemErroLink;
        } else {
            if(us.getPermissao().equals("Admin")){
                int resp = cs.atualizarLink(link);
                if (resp == 1) {
                    return new ResponseEntity(link, HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível atualizar o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
                }
            }else
                return mensagemErro;
        }
    }

    // Parametro => {"id": 24,"url":"https://www.una.br"}
    // Request for: http://localhost:8080/coletor/link
    @DeleteMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerLink(@RequestBody @Valid Link link, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return mensagemErroLink;
        } else {
            if(us.getPermissao().equals("Admin")){
                link = cs.removerLink(link);
                if (link != null) {
                    return new ResponseEntity(new Mensagem("Sucesso", "Link removido com suceso"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
                }
            }else
                return mensagemErro;
        }
    }

    // Request for: http://localhost:8080/coletor/link/{id}
    @DeleteMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerLinkId(@PathVariable(value = "id") Long id) {
        if ((id != null) && (id <= 0)) {
            return mensagemErroLink;
        } else {
            if(us.getPermissao().equals("Admin")){
                boolean resp = cs.removerLink(id);
                if (resp == true) {
                    return new ResponseEntity(new Mensagem("Sucesso", "Link removido com suceso"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
                }
            }else
                return mensagemErro;
        } 
    }
    
    // Parametro => {"id": 780, "texto":"Nada","visao":"Nada","url":"https://www.una.br"}
    // Request for: http://localhost:8080/coletor/documento
    @DeleteMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerDocumento(@RequestBody @Valid Documento documento, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Mensagem("Erro", "Os dados sobre o documento não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            if(us.getPermissao().equals("Admin")){
                documento = cs.removerDocumento(documento);
                if (documento != null) {
                    return new ResponseEntity(new Mensagem("Sucesso", "Documento removido com suceso"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o documento informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
                }
            }else
                return mensagemErro;
        }
    }
    
    // Request for: http://localhost:8080/coletor/documento/{id}
    @DeleteMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerDocumentoId(@PathVariable(value = "id") Long id) {
        if ((id != null) && (id <= 0)) {
            return new ResponseEntity(new Mensagem("Erro", "Os dados sobre o documento não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {    
            if(us.getPermissao().equals("Admin")){
                boolean resp = cs.removerDocumento(id);
                if (resp == true) {
                    return new ResponseEntity(new Mensagem("Sucesso", "Documento removido com suceso"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o documento informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
                }
            }else
                return mensagemErro;
        }
    }
    
    // Request for: http://localhost:8080/coletor/encontrar/{url}
    @GetMapping (value = "/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarLink (@PathVariable (value = "url") String url) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity (cs.encontrarLinkUrl (url), HttpStatus.OK);
        }else
            return mensagemErro; 
    }
    
    // Request for: http://localhost:8080/coletor/link/ordemAlfabetica
    @GetMapping(value = "/link/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarEmOrdemAlfabetica() {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.listarEmOrdemAlfabetica(), HttpStatus.OK);
        }else
            return mensagemErro;
    }

    // Request for: http://localhost:8080/coletor/link/pagina
    @GetMapping(value = "/link/pagina", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarPagina() {      
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.buscarPagina(), HttpStatus.OK);
        }else
          return mensagemErro;            
    }
    
    // Request for: http://localhost:8080/coletor/link/intervalo/{id1}/{id2}
    @GetMapping(value = "/link/intervalo/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarLinkPorIntervaloDeId(@PathVariable(value = "id1") Long id1, @PathVariable(value = "id2") Long id2) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.pesquisarLinkPorIntervaloDeIdentificacao(id1, id2), HttpStatus.OK);
        }else
            return mensagemErro;     
    }
    
    // Request for: http://localhost:8080/coletor/link/intervalo/contar/{id1}/{id2}
    @GetMapping(value = "/link/intervalo/contar/{id1}/{id2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity contarLinkPorIntervaloDeId(@PathVariable(value = "id1") Long id1, @PathVariable(value = "id2") Long id2) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.contarLinkPorIntervaloDeIdentificacao(id1, id2), HttpStatus.OK);
        }else
            return mensagemErro;      
    }
    
    // Url parametro => http://localhost:8080/coletor/link/data/2018-11-18T12:15:43/2018-11-18T13:13:43
    // Request for: http://localhost:8080/coletor/link/data/{data1}/{data2}
    @GetMapping(value = "/link/data/{data1}/{data2}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarLinkPorIntervaloData(@PathVariable(value = "data1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data1,
            @PathVariable(value = "data2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data2) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(cs.pesquisarLinkPorIntervaloDeData(data1, data2), HttpStatus.OK);
        }else
            return mensagemErro;     
    }

    // Parametro => {"urls": ["https://www.band.uol.com.br","https://www.yahoo.com"]}
    // Request for: http://localhost:8080/coletor/urlsSemente
    @PostMapping(value = "/urlsSemente", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inserirUrl(@RequestBody UrlsSemente urls, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Mensagem("Erro", "Os dados sobre as urls não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            if(us.getPermissao().equals("Admin")){
                List<URL> sementes = new LinkedList();
                try {
                    while (!urls.getUrls().isEmpty()) {
                        URL u = new URL(urls.getUrls().remove(0));
                        sementes.add(u);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cs.executar(sementes);
                if (urls != null) {
                    return new ResponseEntity(urls, HttpStatus.OK);
                } else {
                    return new ResponseEntity(new Mensagem("Erro", "Não foi possível inserir as Urls informadas no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }else
                return mensagemErro;
        }
    } 
    
    // Request for: http://localhost:8080/coletor/host/encontrar/{host}
    @GetMapping(value = "/host/encontrar/{host}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarHost(@PathVariable(value = "host") String host) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(hs.encontrarHost(host), HttpStatus.OK);
        }else
            return mensagemErro;
    }
    
    // Request for: http://localhost:8080/coletor/host/ordemAlfabetica
    @GetMapping(value = "/host/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity hostOrdemAlfabetica(){ 
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(hs.ordemAlfabetica(), HttpStatus.OK);
        }else
            return mensagemErro;
    }
    
    // Request for: http://localhost:8080/coletor/link/ultima/coleta/{host}/{data}
    @PutMapping(value = "/link/ultima/coleta/{host}/{data}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity atualizarUltimaColeta(@PathVariable(value = "host") String host, @PathVariable(value = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        if(us.getPermissao().equals("Admin")){
            int n = cs.atualizarDataUltimaColeta(host, data);
            ResponseEntity resposta = new ResponseEntity(new Mensagem("sucesso", "número de registros atualizados: " + n), HttpStatus.OK);
            return resposta;
        }else
            return mensagemErro;
    }

    /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>> O que falta <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        Lista 13:
            Altere o projeto para que ele retorne as páginas para a aplicação cliente, no caso o Postman.
            Crie uma API que permita à aplicação cliente especificar qual página deve ser retornada. 
            O número da página a ser retornada deve ser informado na URL de requisição.
    */

}
