package org.code_revue.knavery.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Collections;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin/logging")
public class LoggerController {

    @Autowired
    private View jsonView;

    // Not great - is there some better way to get all of the Logback Classic levels?
    public static final Level[] LEVELS = new Level[]
            { Level.OFF, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE, Level.ALL };

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        model.addAttribute("loggers", context.getLoggerList());
        model.addAttribute("levels", LEVELS);
        return "logging";
    }

    @RequestMapping(value = "/level", method = RequestMethod.POST)
    public ModelAndView level(@RequestParam String logger, @RequestParam String level) {
        Logger log = (Logger) LoggerFactory.getLogger(logger);
        log.setLevel(Level.toLevel(level));
        return new ModelAndView(jsonView, Collections.singletonMap("status", "success"));
    }

}
