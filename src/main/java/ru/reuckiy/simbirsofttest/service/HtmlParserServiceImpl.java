package ru.reuckiy.simbirsofttest.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.reuckiy.simbirsofttest.model.Statistics;
import ru.reuckiy.simbirsofttest.model.Word;
import ru.reuckiy.simbirsofttest.repository.StatisticsRepository;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HtmlParserServiceImpl implements HtmlParserService{
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public HtmlParserServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public void htmlParser(String url) throws IOException {
        Document htmlPage = Jsoup.connect(url)
                .userAgent("Chrome/96.0.4664.93")
                .timeout(6000)
                .sslSocketFactory(socketFactory())
                .get();

        //String textPage = htmlPage.text();
        String textPage = htmlPage.select("p").text();
        String separators = String.join("|\\", listSeparators());
        Map<String, Word> wordMap = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(textPage.getBytes(StandardCharsets.UTF_8))));
        String line;
        Word wordOb = null;
        while ((line = br.readLine()) != null) {
            String[] words = line.split(separators);
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                wordOb = wordMap.get(word);
                if (wordOb == null) {
                    wordOb = new Word();
                    wordOb.word = word;
                    wordOb.count = 0;
                    wordMap.put(word, wordOb);
                }
                wordOb.count++;
            }
        }
        br.close();

        List<Word> wordList = new ArrayList<>(wordMap.values());
        saveWord(url, wordList);
    }

    @Override
    public void saveWord(String url, List<Word> list) {
        if (statisticsRepository.findBySiteUrl(url).isEmpty()){
            Statistics statistics = new Statistics(url, list);
            statisticsRepository.save(statistics);
        }
    }

    @Override
    public List<Statistics> findStatistic() {
        return statisticsRepository.findAll();
    }

    private List<String> listSeparators (){
        List<String> listSep = new ArrayList<>();
        listSep.add(" ");
        listSep.add(",");
        listSep.add(".");
        listSep.add("!");
        listSep.add("?");
        listSep.add("(");
        listSep.add(")");
        listSep.add("[");
        listSep.add("]");
        listSep.add("-");
        listSep.add(";");
        listSep.add(":");
        listSep.add("\n");
        listSep.add("\r");
        listSep.add("\t");
        return listSep;
    }

    // Вариант для теста !!!
    private SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed SSL socket factory", e);
        }
    }
}
