package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.repository.HostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostService {
    
    @Autowired
    HostRepository hr;
    
    public List<Host> encontrarHost(String host) {
        System.out.println("Verificar.................................. "+host);
        return hr.findByHostIgnoreCaseContaining(host);
    }
    
    public Host salvar(Host host){
        return hr.save(host);
    }
    
    public List<Host> ordemAlfabetica(){
        return hr.getInLexicalOrder();
    }
    
}
