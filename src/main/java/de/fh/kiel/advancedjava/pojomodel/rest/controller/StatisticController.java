package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPojoElement;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import de.fh.kiel.advancedjava.pojomodel.rest.service.StatisticService;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Controller
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @GetMapping(value = "/pojoStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String pojoStatistic(@RequestParam("package") String packageName, @RequestParam("name") String className) {
        return statisticService.pojoStatistic(packageName, className);
    }

}
