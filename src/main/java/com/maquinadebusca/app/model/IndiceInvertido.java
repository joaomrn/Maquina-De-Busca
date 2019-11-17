package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@JsonIdentityInfo (
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class IndiceInvertido implements Serializable {

  static final long serialVersionUID = 1L;

  @EmbeddedId
  private IdIndiceInvertido id;

  private int frequencia;

  private double peso;

  @ManyToOne (fetch = FetchType.LAZY)
  @MapsId ("idTermo")
  private TermoDocumento termo;

  @ManyToOne (fetch = FetchType.LAZY)
  @MapsId ("idDocumento")
  private Documento documento;

  public IndiceInvertido () {
  }

  public IndiceInvertido (TermoDocumento termo, Documento documento) {
    this.termo = termo;
    this.documento = documento;
    this.id = new IdIndiceInvertido (termo.getId (), documento.getId ());
  }
  
   public IndiceInvertido (TermoDocumento termo, Documento documento, int frequencia) {
    this.termo = termo;
    this.documento = documento;
    this.frequencia = frequencia;
    this.id = new IdIndiceInvertido (termo.getId (), documento.getId ());
  }

  public IdIndiceInvertido getId () {
    return id;
  }

  public void setId (IdIndiceInvertido id) {
    this.id = id;
  }

  public int getFrequencia () {
    return frequencia;
  }

  public void setFrequencia (int frequencia) {
    this.frequencia = frequencia;
  }

  public double getPeso () {
    return peso;
  }

  public void setPeso (double peso) {
    this.peso = peso;
  }

  public TermoDocumento getTermo () {
    return termo;
  }

  public void setTermo (TermoDocumento termo) {
    this.termo = termo;
  }

  public Documento getDocumento () {
    return documento;
  }

  public void setDocumento (Documento documento) {
    this.documento = documento;
  }

  @Override
  public int hashCode () {
    int hash = 3;
    hash = 83 * hash + Objects.hashCode (this.id.getIdTermo ());
    hash = 83 * hash + Objects.hashCode (this.id.getIdDocumento ());
    return hash;
  }
    
  @Override
  public boolean equals (Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass () != obj.getClass ()) {
      return false;
    }
    final IndiceInvertido other = (IndiceInvertido) obj;
    if (!Objects.equals (this.id.getIdTermo (), other.id.getIdTermo ())) {
      return false;
    }
    if (!Objects.equals (this.id.getIdDocumento (), other.id.getIdDocumento ())) {
      return false;
    }
    return true;
  }
}
