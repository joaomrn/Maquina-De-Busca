package com.maquinadebusca.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Documento implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String url;

    @Lob
    @NotBlank
    private String texto;

    @Lob
    @NotBlank
    private String visao;

    private double frequenciaMaxima;

    private double somaQuadradosPesos;

    @Transient
    private double similaridade;
    
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    
    @JsonIgnore
    @JoinTable(
            name = "documento_link",
            joinColumns = @JoinColumn(name = "documento_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"))
    private Set<Link> links;

    @JsonIgnore
    @OneToMany(
            mappedBy = "documento", // Nome do atributo na classe IndiceInvertido.
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<IndiceInvertido> indiceInvertido;

    public Documento() {
        this.indiceInvertido = new LinkedList();
        links = new HashSet();
    }

    public Documento(String url, String texto, String visao) {
        this.url = url;
        this.texto = texto;
        this.visao = visao;
        this.links = new HashSet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getVisao() {
        return visao;
    }

    public void setVisao(String visao) {
        this.visao = visao;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void removeLink(Link link) {
        links.remove(link);
    }

    public double getFrequenciaMaxima() {
        return frequenciaMaxima;
    }

    public void setFrequenciaMaxima(double frequenciaMaxima) {
        this.frequenciaMaxima = frequenciaMaxima;
    }

    public double getSomaQuadradosPesos() {
        return somaQuadradosPesos;
    }

    public void setSomaQuadradosPesos(double somaQuadradosPesos) {
        this.somaQuadradosPesos = somaQuadradosPesos;
    }

    public List<IndiceInvertido> getIndiceInvertido() {
        return indiceInvertido;
    }

    public void setIndiceInvertido(List<IndiceInvertido> indiceInvertido) {
        this.indiceInvertido = indiceInvertido;
    }

    public void inserirTermo(TermoDocumento termo) {
        IndiceInvertido entradaIndiceInvertido = new IndiceInvertido(termo, this); // Cria uma nova entrada para o índice invertido com o termo informado como parâmetro e com o documento corrente.
        this.indiceInvertido.add(entradaIndiceInvertido); // Insere a nova entrada no índice invertido do documento corrente.
        termo.getIndiceInvertido().add(entradaIndiceInvertido); // Insere a nova entrada no índice invertido do termo que foi informado como parâmetro.
    }

    public void removeTermo(TermoDocumento termo) {
        Iterator<IndiceInvertido> iterator = this.indiceInvertido.iterator();
        while (iterator.hasNext()) {
            IndiceInvertido entradaIndiceInvertido = iterator.next();
            if (entradaIndiceInvertido.getTermo().equals(termo) && entradaIndiceInvertido.getDocumento().equals(this)) {
                iterator.remove(); // Remoção no Banco de Dados a partir da tabela Documento.
                entradaIndiceInvertido.getTermo().getIndiceInvertido().remove(entradaIndiceInvertido); // Remoção no Banco de Dados a partir da tabela Termo.
                entradaIndiceInvertido.setDocumento(null); // Remoção na memória RAM.
                entradaIndiceInvertido.setTermo(null); // Remoção na memória RAM.
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.url);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documento other = (Documento) obj;
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(double similaridade) {
        this.similaridade = similaridade;
    }

}
