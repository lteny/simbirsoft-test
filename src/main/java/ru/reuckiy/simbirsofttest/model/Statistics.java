package ru.reuckiy.simbirsofttest.model;

import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String siteUrl;

    @OneToMany(targetEntity = Word.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "w_fk", referencedColumnName = "id")
    private List<Word> statistic;

    public Statistics() {
    }

    public Statistics(String siteUrl, List<Word> statistic) {
        this.siteUrl = siteUrl;
        this.statistic = statistic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Statistics that = (Statistics) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
