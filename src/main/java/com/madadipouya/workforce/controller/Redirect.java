package com.madadipouya.workforce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Redirect {

    @RequestMapping(value = "/apidocs", method = RequestMethod.GET)
    public ModelAndView redirectToApiPage() {
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
