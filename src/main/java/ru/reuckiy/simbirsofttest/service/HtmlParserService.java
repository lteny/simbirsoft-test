package ru.reuckiy.simbirsofttest.service;

import ru.reuckiy.simbirsofttest.model.Statistics;
import ru.reuckiy.simbirsofttest.model.Word;

import java.util.List;

public interface HtmlParserService {
     void saveWord(String url, List<Word> list);
     List<Statistics> findStatistic();
}
