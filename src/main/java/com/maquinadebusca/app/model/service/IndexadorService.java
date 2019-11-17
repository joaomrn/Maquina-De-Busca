package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.IndiceInvertido;
import com.maquinadebusca.app.model.TermoDocumento;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import static java.lang.Math.log;
import java.util.Hashtable;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexadorService {

    private Hashtable hashTermos;

    @Autowired
    DocumentoService ds;
    
    @Autowired
    DocumentoRepository dr;

    @Autowired
    TermoDocumentoService ts;

    public IndexadorService() {
        this.hashTermos = new Hashtable();
    }

    @Transactional
    public boolean criarIndice() {
        List<Documento> documentos = this.lerDocumentos();
        for (Documento documento : documentos) {
            //documento.setFrequenciaMaxima(0L); 
            documento.setSomaQuadradosPesos(0L);  
            documento = ds.save(documento);        
            this.indexar(documento);            
            
        }
        return true;
    }

    public void indexar (Documento documento) {
        int i;
        String visaoDocumento = documento.getVisao();
        String[] termos = visaoDocumento.split(" ");
        for (i = 0; i < termos.length; i++) {
            if (!termos[i].equals("")) {
                TermoDocumento termo = this.getTermo(termos[i]);
                int f = this.frequencia(termo.getTexto(), termos);
                //if (f > documento.getFrequenciaMaxima()) {
                //    documento.setFrequenciaMaxima(f);
               // }
                termo.inserirEntradaIndiceInvertido(documento, f, termo);
            }
        }
        //dr.save (documento);
        //for (TermoDocumento termo : listaTermos) {
        //  termo.setFrequenciaNormalizada (documento);
        //  tr.save (termo);
        //}
    }
    
    public TermoDocumento getTermo (String texto) {
        TermoDocumento termo;
        if (this.hashTermos.containsKey(texto)) {
            termo = (TermoDocumento) this.hashTermos.get(texto);
            termo.setN((long) termo.getIndiceInvertido().size()+1);
            
        } else {
            termo = new TermoDocumento();
            termo.setTexto(texto);
            termo.setN(1L);
            termo = ts.save(termo);
            this.hashTermos.put(texto, termo);
        }
        return termo;
    }

    public int frequencia (String termo, String[] termos) {
        int i, contador = 0;

        for (i = 0; i < termos.length; i++) {
          if (!termos[i].equals ("")) {
            if (termos[i].equalsIgnoreCase (termo)) {
              contador++;
              termos[i] = "";
            }
          }
        }
        return contador;
    }

    public List<Documento> lerDocumentos() {
        Documento documento;
        List<Documento> documentos = new LinkedList();
        documentos = dr.getInLexicalOrder();
        return documentos;
    }
}
