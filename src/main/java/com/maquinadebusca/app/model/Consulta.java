package com.maquinadebusca.app.model;

import java.util.LinkedList;
import java.util.List;

public class Consulta {

  private String texto;
  private String visao;
  private List<TermoConsulta> termosConsulta = new LinkedList ();
  private List<EntradaRanking> ranking = new LinkedList ();
  
  public Consulta () {
  }

  public Consulta (String texto) {
    this.texto = texto;
    this.visao = texto;
  }

  public String getTexto () {
    return texto;
  }

  public void setTexto (String texto) {
    this.texto = texto;
  }

  public String getVisao () {
    return visao;
  }

  public void setVisao (String visao) {
    this.visao = visao;
  }

  public List<TermoConsulta> getTermosConsulta () {
    return termosConsulta;
  }

  public void setTermosConsulta (List<TermoConsulta> termosConsulta) {
    this.termosConsulta = termosConsulta;
  }

  public void adicionarTermoConsulta (TermoConsulta termoConsulta) {
    this.termosConsulta.add (termoConsulta);
  }

  public List<String> getListaTermos () {
    List<String> listaTermos = new LinkedList ();

    String[] termos = this.texto.split (" ");
    for (String termo : termos) {
      listaTermos.add (termo);
    }

    return listaTermos;
  }

  public double getSomaQuadradosPesos () {
    double somaQuadradosPesos = 0;
    List<TermoConsulta> termosConsulta = this.getTermosConsulta ();
    for (TermoConsulta termoConsulta : termosConsulta) {
      somaQuadradosPesos += Math.pow (termoConsulta.getPeso (), 2);
    }
    return somaQuadradosPesos;
  }

  public List<EntradaRanking> getRanking () {
    return ranking;
  }

  public void setRanking (List<EntradaRanking> ranking) {
    this.ranking = ranking;
  }
  
}
