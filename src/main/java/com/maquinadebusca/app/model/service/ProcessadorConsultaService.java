package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Consulta;
import com.maquinadebusca.app.model.EntradaRanking;
import com.maquinadebusca.app.model.IndiceInvertido;
import com.maquinadebusca.app.model.TermoConsulta;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProcessadorConsultaService {

  @Autowired
  TermoDocumentoService ts;

  @Autowired
  DocumentoService ds;

  @Autowired
  IndiceInvertidoService iis;

  @Autowired
  IndexadorService is;

  private Map<String, EntradaRanking> mergeListasInvertidas = new Hashtable ();

  public ProcessadorConsultaService () {
  }

  public Consulta processarConsulta (String textoConsulta) {
    Consulta consulta = new Consulta (textoConsulta);
    this.iniciarTermosConsulta (consulta);
    this.processarListasInvertidas (consulta);
    this.computarSimilaridade ();
    consulta.setRanking (this.getRanking ());
    return consulta;
  }

  public void iniciarTermosConsulta (Consulta consulta) {
    String visaoConsulta = consulta.getVisao ();
    String[] termos = visaoConsulta.split (" ");
    for (String termo : termos) {
      if (!termo.equals ("")) {
        int f = is.frequencia (termo, termos);
        double idf = ts.getIdf (termo);
        TermoConsulta termoConsulta = new TermoConsulta (termo, f, idf);
        consulta.adicionarTermoConsulta (termoConsulta);
      }
    }
  }

  public void processarListasInvertidas (Consulta consulta) {
    List<TermoConsulta> termosConsulta = consulta.getTermosConsulta ();
    for (TermoConsulta termoConsulta : termosConsulta) {
      List<IndiceInvertido> entradasIndiceInvertido = iis.getEntradasIndiceInvertido (termoConsulta.getTexto ());
      for (IndiceInvertido entradaIndiceInvertido : entradasIndiceInvertido) {
        if (this.mergeListasInvertidas.containsKey (entradaIndiceInvertido.getDocumento ().getUrl ())) {
          EntradaRanking entradaRanking = this.mergeListasInvertidas.get (entradaIndiceInvertido.getDocumento ().getUrl ());
          entradaRanking.adicionarProdutoPesos (termoConsulta.getPeso () * entradaIndiceInvertido.getPeso ());
        } else {
          EntradaRanking entradaRanking = new EntradaRanking ();
          entradaRanking.setUrl (entradaIndiceInvertido.getDocumento ().getUrl ());
          entradaRanking.adicionarProdutoPesos (termoConsulta.getPeso () * entradaIndiceInvertido.getPeso ());
          entradaRanking.setSomaQuadradosPesosDocumento (entradaIndiceInvertido.getDocumento ().getSomaQuadradosPesos ());
          entradaRanking.setSomaQuadradosPesosConsulta (consulta.getSomaQuadradosPesos ());
          this.mergeListasInvertidas.put (entradaIndiceInvertido.getDocumento ().getUrl (), entradaRanking);
        }
      }
    }
  }

  public void computarSimilaridade () {
    Collection<EntradaRanking> ranking = this.mergeListasInvertidas.values ();
    for (EntradaRanking entradaRanking : ranking) {
      entradaRanking.computarSimilaridade ();
    }
  }

  public List<EntradaRanking> getRanking () {
    List<EntradaRanking> resp = new LinkedList ();
    Collection<EntradaRanking> ranking = this.mergeListasInvertidas.values ();
    for (EntradaRanking entradaRanking : ranking) {
      resp.add (entradaRanking);
    }
    return resp;
  }
}
