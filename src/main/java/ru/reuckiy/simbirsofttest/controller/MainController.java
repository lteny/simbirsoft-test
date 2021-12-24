package ru.reuckiy.simbirsofttest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.reuckiy.simbirsofttest.service.HtmlParserServiceImpl;

import java.io.IOException;

//@Log4j2
@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger log = LogManager.getLogger(MainController.class);

    private final HtmlParserServiceImpl parserService;

    @Autowired
    public MainController(HtmlParserServiceImpl parserService) {
        this.parserService = parserService;
    }

    @GetMapping
    public String showWord(Model model) {
        model.addAttribute("wordList", parserService.findStatistic());
        model.addAttribute("title", "TEST");
        return "index";
    }

    @PostMapping
    public String addWord(@RequestParam String url, Model model){
        try {
            parserService.htmlParser(url);
        }catch (IllegalArgumentException | IOException e){
            log.error("IllegalArgumentException!", e);
        }
        return "redirect:";
    }
}
