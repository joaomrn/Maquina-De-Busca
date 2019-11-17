package com.maquinadebusca.app.model;

import java.util.LinkedList;
import java.util.List;

public class EntradaRanking {

  private String url;
  private List<Double> produtoPesos = new LinkedList ();
  private double somaQuadradosPesosDocumento;
  private double somaQuadradosPesosConsulta;
  private double similaridade;

  public EntradaRanking () {
  }

  public EntradaRanking (String url, double produtoPesos, double somaQuadradosPesosDocumento, double somaQuadradosPesosConsulta) {
    this.url = url;
    this.produtoPesos.add (produtoPesos);
    this.somaQuadradosPesosDocumento = somaQuadradosPesosDocumento;
    this.somaQuadradosPesosConsulta = somaQuadradosPesosConsulta;
  }

  public String getUrl () {
    return url;
  }

  public void setUrl (String url) {
    this.url = url;
  }

  public List<Double> getProdutoPesos () {
    return produtoPesos;
  }

  public void setProdutoPesos (List<Double> produtoPesos) {
    this.produtoPesos = produtoPesos;
  }

  public double getSomaQuadradosPesosDocumento () {
    return somaQuadradosPesosDocumento;
  }

  public void setSomaQuadradosPesosDocumento (double somaQuadradosPesosDocumento) {
    this.somaQuadradosPesosDocumento = somaQuadradosPesosDocumento;
  }

  public double getSomaQuadradosPesosConsulta () {
    return somaQuadradosPesosConsulta;
  }

  public void setSomaQuadradosPesosConsulta (double somaQuadradosPesosConsulta) {
    this.somaQuadradosPesosConsulta = somaQuadradosPesosConsulta;
  }

  public double getSimilaridade () {
    return similaridade;
  }

  public void setSimilaridade (double similaridade) {
    this.similaridade = similaridade;
  }

  public void adicionarProdutoPesos (double produtoPesos) {
    this.produtoPesos.add (produtoPesos);
  }

  public void computarSimilaridade () {
    int i;
    double numerador = 0, denominador;

    if ((this.somaQuadradosPesosDocumento > 0) && (this.somaQuadradosPesosConsulta > 0)) {
      for (i = 0; i < this.produtoPesos.size (); i++) {
        numerador += this.produtoPesos.get (i);
      }
      denominador = Math.sqrt (this.somaQuadradosPesosDocumento) * Math.sqrt (this.somaQuadradosPesosConsulta);

      this.similaridade = numerador / denominador;
    } else {
      this.similaridade = 0;
    }
  }
}
