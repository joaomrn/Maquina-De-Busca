package com.maquinadebusca.app.model.service;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.StopWord;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maquinadebusca.app.model.repository.DocumentoRepository;
import com.maquinadebusca.app.model.repository.LinkRepository;
import org.springframework.data.domain.Pageable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@Service
public class ColetorService {

    Scanner sc = new Scanner(System.in);
    Hashtable historico = new Hashtable();
    @Autowired
    private DocumentoRepository dr;

    @Autowired
    private LinkRepository lr;

    @Autowired
    HostService hs;

    public boolean removerLink(Long id) {
        boolean resp = false;
        try {
            lr.deleteById(id);
            resp = true;
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
            e.printStackTrace();
        }
        return resp;
    }

    public Link removerLink(Link link) {
        try {
            lr.delete(link);
        } catch (Exception e) {
            link = null;
            System.out.println("\n>>> Não foi possível remover o link informado no banco de dados.\n");
            e.printStackTrace();
        }
        return link;
    }

    public Link salvarLink(Link link) {
        Link l = null;
        try {
            l = lr.save(link);
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível salvar o link informado no banco de dados.\n");
            e.printStackTrace();
        }
        return l;
    }

    public int atualizarLink(Link link) {
        int resposta = 0;
        try {
            String hora = link.getUtimaColeta().toString();
            String url = link.getUrl();
            Long id = link.getId();
            resposta = lr.update(id, hora, url);
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível atualizar o link informado no banco de dados.\n");
            e.printStackTrace();
        }
        return resposta;
    }

    public List<Documento> executar(List sementes) {

        List<Documento> documentos = new LinkedList();
        try {
            while (!sementes.isEmpty()) {
                URL url = (URL) sementes.remove(0);
                if (this.protocoloDeExclusao(url) == true) {
                    documentos.add(this.coletar(url));
                } else {
                    sementes.add(url);
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n\n Erro ao executar o serviço de coleta! \n\n\n");
            e.printStackTrace();
        }
        return documentos;
    }

    public Documento coletar(URL urlDocumento) {
        Documento documento = new Documento();
        Host host = new Host();

        try {
            Link link = new Link();
            Document d = Jsoup.connect(urlDocumento.toString()).get();
            Elements urls = d.select("a[href]");

            documento.setUrl(urlDocumento.toString());
            documento.setTexto(retirarStopwords(d.html()));
            documento.setVisao(retirarStopwords(d.text()));

            link.setUrl(urlDocumento.toString());
            link.setUltimaColeta(LocalDateTime.now());
            link.addDocumento(documento);
            documento.addLink(link);
            int i = 0;
            for (Element url : urls) {
                i++;
                String u = url.attr("abs:href");
                if (removeIguais(link, u)) {
                    if ((!u.equals("")) && (u != null)) {
                        link = lr.findByUrl(u);
                        if (link == null) {
                            link = new Link();
                            link.setUrl(u);
                            link.setUltimaColeta(LocalDateTime.now());
                        }

                        link.addDocumento(documento);
                        documento.addLink(link);
                    }
                }
            }
            System.out.println("Número de links coletados: " + i);
            System.out.println("Tamanho da lista links: " + documento.getLinks().size());
            host.setHost(urlDocumento.getHost());
            host.setQuantidadeUrls(documento.getLinks().size());
            host.setDocumento_id(documento);
            documento = dr.save(documento);
            hs.salvar(host);
        } catch (Exception e) {
            System.out.println("\n\n\n Erro ao coletar a página! \n\n\n");
            e.printStackTrace();
        }
        return documento;
    }

    public boolean protocoloDeExclusao(URL url) {
        boolean resp = false;
        String host = url.getHost();
        Long tempoAnterior = (Long) this.historico.get(host);
        protocoloDePermissao(url);
        if (tempoAnterior == null) {
            resp = true;
            this.historico.put(host, Instant.now().toEpochMilli());
            protocoloDePermissao(url);
        } else {
            Long tempoCorrente = Instant.now().toEpochMilli();
            if ((tempoCorrente - tempoAnterior) >= 10000) {
                resp = true;
                this.historico.put(host, Instant.now().toEpochMilli());
                protocoloDePermissao(url);
            } else {
                resp = false;
                long sec = tempoCorrente - tempoAnterior;
            }
        }
        return resp;
    }

    public void protocoloDePermissao(URL url) {
        StringBuilder pagina = new StringBuilder();
        try {
            String host = "https://" + url.getHost() + "/robots.txt";
            URL urlRobotsTxt = new URL(host);
            URLConnection url_connection = urlRobotsTxt.openConnection();
            InputStream is = url_connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader buffer = new BufferedReader(reader);
            String linha;
            while ((linha = buffer.readLine()) != null) {
                if (linha.toLowerCase().contains("disallow")) {
                    String[] partes = linha.split(": ");
                    if (partes.length > 1) {
                        System.out.println("https://" + url.getHost() + partes[1]);
                    }
                }
            }
        } catch (IOException e) {
            pagina.append("Erro: não foi possível coletar a página.");
        }
    }

    public boolean removeIguais(Link lista, String url) {
        if (lista.equals(url)) {
            return false;
        } else {
            return true;
        }
    }

    public String retirarStopwords(String html) {
        StopWord stopWords = new StopWord(StopWord.readFileInList("C:\\Users\\Eduardo\\Downloads\\app_2018_09_17_ate_a_parte_2\\app_2018_09_12_22-16\\stopwords.txt"));
        String texto = "";
        String pontuacoes = "[,.;:?!|@-_+=%#\"\'ºª<>]*";
        String[] arrayTexto;
        html = html.toLowerCase();
        html = html.replaceAll(pontuacoes, "");
        arrayTexto = html.split(" ");

        for (String word : arrayTexto) {
            for (String stopWord : stopWords.getStopWords()) {
                String strRegEx = "\\b" + stopWord + "\\b";
                if (word.matches(strRegEx)) {
                    word = word.replace(stopWord, "");
                }
            }
            if (!word.isEmpty()) {
                texto += word + " ";
            }
        }
        return texto;
    }

    public List<Documento> getDocumento() {
        Iterable<Documento> documentos = dr.findAll();
        List<Documento> resposta = new LinkedList();
        for (Documento documento : documentos) {
            resposta.add(documento);
        }
        return resposta;
    }

    public Documento getDocumento(long id) {
        Documento documento = dr.findById(id);
        return documento;
    }

    public Documento encontrarDocumento(String documento) {
        return dr.findByUrlIgnoreCaseContaining(documento);
    }

    public List<Documento> documentoOrdemAlfabetica() {
        return dr.getInLexicalOrder();
    }

    public Documento removerDocumento(Documento documento) {
        try {
            dr.delete(documento);
        } catch (Exception e) {
            documento = null;
            System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
            e.printStackTrace();
        }
        return documento;
    }

    public boolean removerDocumento(Long id) {
        boolean resp = false;
        try {
            dr.deleteById(id);
            resp = true;
        } catch (Exception e) {
            System.out.println("\n>>> Não foi possível remover o documento informado no banco de dados.\n");
            e.printStackTrace();
        }
        return resp;
    }

    public List<Link> getLink() {
        Iterable<Link> links = lr.findAll();
        List<Link> resposta = new LinkedList();
        for (Link link : links) {
            resposta.add(link);
        }
        return resposta;
    }

    public Link getLink(long id) {
        Link link = lr.findById(id);
        return link;
    }

    public Object executar() {
        throw new UnsupportedOperationException();
    }

    public List<Link> encontrarLinkUrl(String url) {
        return lr.findByUrlIgnoreCaseContaining(url);
    }

    public List<Link> listarEmOrdemAlfabetica() {
        return lr.getInLexicalOrder();
    }

    public List<Slice<Link>> buscarPagina() {      
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "url"));
        Slice<Link> pagina = lr.getPage(pageable);       
        List<Slice<Link>> lista = new ArrayList<Slice<Link>>();

        while (pagina.hasNext()) {
            lista.add(pagina);
            pageable = pagina.nextPageable();
            pagina = lr.getPage(pageable);
        }
        return lista;
    }

    public List<Link> pesquisarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
        return lr.findLinkByIdRange(id1, id2);
    }

    public Long contarLinkPorIntervaloDeIdentificacao(Long id1, Long id2) {
        return lr.countLinkByIdRange(id1, id2);
    }

    public List<Link> pesquisarLinkPorIntervaloDeData(LocalDateTime data1, LocalDateTime data2) {
        return lr.findLinkByData(data1, data2);
    }

    public int atualizarDataUltimaColeta(String host, LocalDateTime dataUltimaColeta) {
        return lr.updateLastCrawlingDate(dataUltimaColeta, host);
    }
}
