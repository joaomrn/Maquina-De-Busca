package com.maquinadebusca.app.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class StopWord {
    private String[] stopWords;

    public StopWord() {
        
    }
    
    public StopWord(String[] stopWords) {
        this.stopWords = stopWords;
    }   
    
    public static String[] readFileInList(String fileName) { 
        List<String> linhas = Collections.emptyList();
        String[] words;
        
        try { 
            linhas = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        }
        
        words = new String[linhas.size()];
        words = linhas.toArray(words);
        
        return words; 
    }
    
    public String[] getStopWords() {
        return stopWords;
    }

    public void setStopWords(String[] stopWords) {
        this.stopWords = stopWords;
    }   
    
    public String getStopWords(int posicao) {
        return stopWords[posicao];
    }
}
