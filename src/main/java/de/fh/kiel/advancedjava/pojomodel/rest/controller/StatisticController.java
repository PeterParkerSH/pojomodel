package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import de.fh.kiel.advancedjava.pojomodel.rest.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = {"Pojo Interface"})
@Controller
public class StatisticController {

    final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @ApiOperation(value = "Get POJO statistics",
            notes = "Gets statics of POJO given by package and class name",
            response = PojoStatistic.class
    )
    @GetMapping(value = "/pojoStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String pojoStatistic(@ApiParam(value = "package name of POJO", required = true) @RequestParam("package") String packageName,
                                @ApiParam(value = "class name of POJO", required = true) @RequestParam("name") String className) {
        return statisticService.pojoStatistic(packageName, className);
    }

}
