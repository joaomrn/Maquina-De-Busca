package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.LoginApiModel;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.service.TipoUsuarioService;
import com.maquinadebusca.app.model.service.UsuarioService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario") // URL: http://localhost:8080/usuario
public class UsuarioController{ 
    
    @Autowired
    UsuarioService us;
    
    @Autowired
    TipoUsuarioService tus;
    
    private final ResponseEntity mensagemErro = new ResponseEntity(new Mensagem("Erro", "Os dados sobre o usuário não forma informados corrretamente"), HttpStatus.BAD_REQUEST);
    private final ResponseEntity mensagemErroAdmin =  new ResponseEntity(new Mensagem("Erro", "Permissão negada, apenas Administradores acessar este método"), HttpStatus.INTERNAL_SERVER_ERROR);
    private final ResponseEntity mensagemErroUser = new ResponseEntity(new Mensagem("Erro", "Permissão negada, apenas Administradores e Usuários podem acessar este método"), HttpStatus.INTERNAL_SERVER_ERROR);
    
    // Parametro => nome
    //Request for: http://localhost:8080/usuario/comum
    @PostMapping(value = "/comum", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid String nome, BindingResult resultado) {
        if(resultado.hasErrors()){
            return new ResponseEntity (new Mensagem ("Erro", "Não foi possivél cadastrar o novo usuário!"), HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity(us.cadastrar(nome), HttpStatus.OK);
    }
    
    // Parametro => ["nome","Admin"] *Admin ou Comum*
    //Request for: http://localhost:8080/usuario/cadastrar
    @PostMapping(value = "/cadastrar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity cadastrar(@RequestBody @Valid List<String> cadastro, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErro;
        } else 
            return new ResponseEntity(us.cadastrarTipo(cadastro), HttpStatus.OK);
    }
    
    // Parametro => ["nome a ser atualizado","novo nome"]
    //URL: http://localhost:8080/usuario/atualizar
    @PutMapping(value = "/atualizar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity atualizarUsuario(@RequestBody List<String> atualizar, BindingResult resultado){
        if(resultado.hasErrors()){
            return mensagemErro;
        } else
            return new ResponseEntity(us.atualizar(atualizar), HttpStatus.OK);
    }
    
    //URL: http://localhost:8080/usuario/atualizarDados/{id}/{nome}
    @PutMapping(value = "/atualizarDados/{id}/{nome}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity atualizarDados(@PathVariable(value = "id") Long id, @PathVariable(value="nome") String nome){
        if(us.getPermissao().equals("Admin")){
            return new ResponseEntity(us.atualizarDados(id,nome), HttpStatus.OK);           
        }else{
            return mensagemErroAdmin;
        }        
    }
    
    //Request for: http://localhost:8080/usuario/remover/{id}
    @DeleteMapping(value = "/remover/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity excluirUsuarioId(@PathVariable(value = "id") Long id){
        ResponseEntity resposta = null;
        if ((id != null) && (id <= 0)) {
            return mensagemErro;
        }else{
            if(us.getPermissao().equals("Admin") || id.toString() == us.getNomeUser()){
               resposta = new ResponseEntity(us.remover(id), HttpStatus.OK);
            }
            else resposta = new ResponseEntity (new Mensagem ("Erro", "Não foi possível remover o usuário! Apenas Administradores e o próprio usuário possuem permissões para remoção de usuários."), HttpStatus.INTERNAL_SERVER_ERROR);           
        }
        return resposta;
    }
    
    // Parametro:  {"id": 315,"nome":"Outro"}
    //Request for: http://localhost:8080/usuario/remover
    @DeleteMapping(value = "/remover", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity excluirUsuario(@RequestBody Usuario userExcluir, BindingResult resultado){
        ResponseEntity resposta = null;
       if(resultado.hasErrors()){
            return mensagemErro;
        }else{
           System.out.println("Verificando........ "+userExcluir.getNome()+" Banco... "+ us.getNomeUser());
            if(us.getPermissao().equals("Admin") || userExcluir.getNome().equals(us.getNomeUser())){
               resposta = new ResponseEntity(us.remover(userExcluir), HttpStatus.OK);
            }
            else resposta = new ResponseEntity (new Mensagem ("Erro", "Não foi possível remover o usuário! Apenas Administradores e o próprio usuário possuem permissões para remoção de usuários."), HttpStatus.INTERNAL_SERVER_ERROR);           
        }
        return resposta;
    }
    
    // Parametro => nome do usuário
    // Request for: http://localhost:8080/usuario/encontrar
    @GetMapping(value = "/encontrar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarUsuario(@RequestBody @Valid String user, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErro;
        }else{
            if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
                return new ResponseEntity(us.encontrarUser(user), HttpStatus.OK);       
            }
            return mensagemErroUser;
        }
    }
    
    // Request for: http://localhost:8080/usuario/encontrar/{usuario}
    @GetMapping(value = "/encontrar/{usuario}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarUsuarioParametro(@PathVariable(value = "usuario") String user) {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
            return new ResponseEntity(us.encontrarUser(user), HttpStatus.OK); 
        }
        return mensagemErroUser;
    }
    
    // Parametro => nome do usuário
    // Request for: http://localhost:8080/usuario/buscar
    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity buscar(@RequestBody @Valid String user, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErro;
        }else{
            if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
                return new ResponseEntity(us.buscarUser(user), HttpStatus.OK);               
            }
            return mensagemErroUser;
        }
    }
    
    // Parametro => parte do nome de algum usuário
    // Request for: http://localhost:8080/usuario/buscarOrdemAlfabetica
    @GetMapping(value = "/buscarOrdemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity buscarOrdemAlfabetica(@RequestBody @Valid String parametroBusca, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErro;        
        }else{
            if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
                return new ResponseEntity(us.ordemAlfabetica(parametroBusca), HttpStatus.OK);
            }
        }
        return mensagemErroUser;
    }
    
    // Parametro => id do usuário
    // Request for: http://localhost:8080/usuario/logar
    @PostMapping(value = "/logar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity logar(@RequestBody @Valid LoginApiModel model, BindingResult resultado) {
        if(resultado.hasErrors()){
            return mensagemErro;
        }else{
            if(us.buscarUsuario(model.id)){
                if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){
                    String sucesso = "Usuário logado - "+ us.getPermissao();
                   return new ResponseEntity(new Mensagem("Sucesso", sucesso), HttpStatus.OK);       
                }
            } 
        }
        return new ResponseEntity(new Mensagem("Erro", "Permissão negada, não foi possível efetuar o Login."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Request for: http://localhost:8080/usuario/sair
    @GetMapping(value = "/sair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity sair() {
        if(us.getPermissao().equals("Admin") || us.getPermissao().equals("Comum")){            
           us.setPermissao("Nenhuma");
           us.setId(null);
           us.setNomeUser(null);
           return new ResponseEntity(new Mensagem("Sucesso", "Logout efetuado"), HttpStatus.OK);       
        }         
        return new ResponseEntity(new Mensagem("Erro", "Não foi possível efetuar logout do sistema"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
