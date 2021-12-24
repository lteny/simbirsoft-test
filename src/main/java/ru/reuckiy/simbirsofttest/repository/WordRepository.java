package ru.reuckiy.simbirsofttest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.reuckiy.simbirsofttest.model.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
}
