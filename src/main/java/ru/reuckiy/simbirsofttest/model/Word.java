package ru.reuckiy.simbirsofttest.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "words")
public class Word implements Comparable<Word> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String word;
    public int count;

    public Word() {
    }

    @Override
    public int compareTo(Word o) {
        return o.count - count;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return word.equals(((Word) obj).word);
    }
}
