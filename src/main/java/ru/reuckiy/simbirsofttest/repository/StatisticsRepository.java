package ru.reuckiy.simbirsofttest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.reuckiy.simbirsofttest.model.Statistics;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<Statistics> findBySiteUrl(String url);
}
