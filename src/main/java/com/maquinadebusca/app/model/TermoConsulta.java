package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class TermoConsulta {

  @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  private String texto;
  private int frequencia;
  private double tf;
  private double idf;
  private double peso;

  public TermoConsulta () {
  }

  public TermoConsulta (String texto, int frequencia, double idf) {
    this.texto = texto;
    this.frequencia = frequencia;
    this.tf = 1 + Math.log (this.frequencia) / Math.log (2);
    this.idf = idf;
    this.peso = this.tf * this.idf;
  }

  public String getTexto () {
    return texto;
  }

  public void setTexto (String texto) {
    this.texto = texto;
  }

  public int getFrequencia () {
    return frequencia;
  }

  public void setFrequencia (int frequencia) {
    this.frequencia = frequencia;
  }

  public double getTf () {
    return tf;
  }

  public void setTf (double tf) {
    this.tf = tf;
  }

  public double getIdf () {
    return idf;
  }

  public void setIdf (double idf) {
    this.idf = idf;
  }

  public double getPeso () {
    return peso;
  }

  public void setPeso (double peso) {
    this.peso = peso;
  }

}
