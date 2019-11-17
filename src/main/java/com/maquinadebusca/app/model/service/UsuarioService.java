package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.controller.UsuarioController;
import com.maquinadebusca.app.mensagem.Mensagem;
import com.maquinadebusca.app.model.TipoUsuario;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.model.repository.TipoUsuarioRepository;
import com.maquinadebusca.app.model.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository ur;
    
    @Autowired
    TipoUsuarioService tus;
    
    @Autowired
    TipoUsuarioRepository tur;
    
    UsuarioController uc;
    
    String permissao = "Nenhum";
    String nomeUser = "";
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getPermissao() {
        return permissao;
    }

    public void setPermissao(String permissao) {
        this.permissao = permissao;
    }
    
    public Usuario salvar (Usuario usuario) {
       return ur.save (usuario);
    }
    
    public ResponseEntity cadastrar (String nome){
        ResponseEntity resposta;    
        if ((nome != null) && (nome != "")) { 
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            TipoUsuario tipoUsuario = new TipoUsuario ();
            tipoUsuario.setPermissao("comum");
            tus.salvar(tipoUsuario);
            usuario.setTipoUsuario(tipoUsuario);
            salvar(usuario);
            resposta = new ResponseEntity("Sucesso em cadastrar usuário", HttpStatus.OK);
        } else {
            resposta = new ResponseEntity(new Mensagem("erro", "Não foi possível cadastrar o usuário informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resposta;
    }
    
    public ResponseEntity cadastrarTipo (List<String> cadastro){
        String nome = cadastro.remove(0);
        String permissaoUser = cadastro.remove(0);
        ResponseEntity resposta;         
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        TipoUsuario tipoUsuario = new TipoUsuario ();
        usuario.setTipoUsuario(tipoUsuario);
        if ( (nome != null) && (nome != "") && (permissaoUser.equals("Admin"))) {
            tipoUsuario.setPermissao(permissaoUser);
            tus.salvar (tipoUsuario);
            salvar (usuario);
            resposta = new ResponseEntity(usuario, HttpStatus.OK);
        }
        else if(permissaoUser != ""){                
            tipoUsuario.setPermissao("Comum");
            tus.salvar (tipoUsuario);
            salvar (usuario);
            resposta = new ResponseEntity(usuario, HttpStatus.OK);
        } else {
            resposta = new ResponseEntity(new Mensagem("Erro", "Houver algum erro na inserção do usuário no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resposta;
    }
    
    public ResponseEntity atualizar(List<String> atualizar){
        String userAtualizar = atualizar.remove(0);
        String nvNome = atualizar.remove(0);
        int verificar = 0;
        ResponseEntity resposta;
        if(!getPermissao().equals("Admin")){
            resposta = new ResponseEntity(new Mensagem("Erro", "Permissão negada, apenas Administradores podem atualizar registros"),  HttpStatus.OK);
        }
        else{
            verificar = ur.update(userAtualizar, nvNome);
            if(verificar == 1){
                resposta = new ResponseEntity("Sucesso em atualizar", HttpStatus.OK);
            }else
                resposta = new ResponseEntity(new Mensagem("Erro", "Não foi possível atualizar o registro"),  HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resposta;
    }
    
    public ResponseEntity atualizarDados(Long id, String nome){
        int verificar = 0;
        ResponseEntity resposta;
        if(!getPermissao().equals("Admin")){
            resposta = new ResponseEntity(new Mensagem("Erro", "Permissão negada, apenas Administradores podem atualizar registros"),  HttpStatus.OK);
        }
        else{
            verificar = ur.updateById(id, nome);
            if(verificar == 1){
                resposta = new ResponseEntity("Sucesso em atualizar", HttpStatus.OK);
            }else
                resposta = new ResponseEntity(new Mensagem("Erro", "Não foi possível atualizar o registro"),  HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resposta;
    }
    
    public ResponseEntity remover(Long id){
        ResponseEntity resposta;
        try{
            ur.deleteById(id);
            tur.deleteById(id - 1);
            resposta = new ResponseEntity(new Mensagem("Sucesso", "Usuário removido com suceso"), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            resposta = new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o usuário informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
        }    
        return resposta;
    }
    
    public ResponseEntity remover(Usuario usuario){
        ResponseEntity resposta;
        try {
            System.out.println("Excluindo..."+usuario);
            ur.deleteById(usuario.getId());
            tur.deleteById(usuario.getId()-1);
            resposta = new ResponseEntity(new Mensagem("Sucesso", "Usuário removido com suceso"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resposta = new ResponseEntity(new Mensagem("Erro", "Não foi possível remover o usuário informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
        }     
        return resposta;
    }
    
    public List<Usuario> ordemAlfabetica(String parametro){
        if(parametro.equals("Admin")){
            System.out.println(".........................../////////// "+parametro);
            return ur.getInLexicalOrder();
        }else
            return ur.getInLexicalOrderNome();
    }
    
    public boolean buscarUsuario(long id){
        Usuario usuario;
        usuario = ur.findById(id);
        setPermissao(usuario.getTipoUsuario().getPermissao());
        setNomeUser(usuario.getNome());
        setId(usuario.getId());
        return true;
    }
    
    public List<Usuario> encontrarUser (String usuario) {
        return ur.findByNomeIgnoreCaseContaining(usuario);
    }
    
    public Usuario buscarUser (String usuario) {
        return ur.findByNome(usuario);
    }
    
}
