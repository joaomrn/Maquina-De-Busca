package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.TipoUsuario;
import com.maquinadebusca.app.model.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoUsuarioService {
    @Autowired
    TipoUsuarioRepository tur;
    
    public TipoUsuario salvar (TipoUsuario tipoUsuario) {
       return tur.save (tipoUsuario);
    }
}
