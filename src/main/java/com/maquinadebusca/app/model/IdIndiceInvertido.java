package com.maquinadebusca.app.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable //pode inserir em outros
public class IdIndiceInvertido implements Serializable {

  private Long idTermo;
  private Long idDocumento;

  public IdIndiceInvertido () {
  }

  public IdIndiceInvertido (Long idTermo, Long idDocumento) {
    this.idTermo = idTermo;
    this.idDocumento = idDocumento;
  }

  public Long getIdTermo () {
    return idTermo;
  }

  public void setIdTermo (Long idTermo) {
    this.idTermo = idTermo;
  }

  public Long getIdDocumento () {
    return idDocumento;
  }

  public void setIdDocumento (Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  @Override
  public int hashCode () {
    int hash = 7;
    hash = 43 * hash + Objects.hashCode (this.idTermo);
    hash = 43 * hash + Objects.hashCode (this.idDocumento);
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
    final IdIndiceInvertido other = (IdIndiceInvertido) obj;
    if (!Objects.equals (this.idTermo, other.idTermo)) {
      return false;
    }
    if (!Objects.equals (this.idDocumento, other.idDocumento)) {
      return false;
    }
    return true;
  }
}
