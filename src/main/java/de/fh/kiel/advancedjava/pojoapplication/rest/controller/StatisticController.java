package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.PojoStatistic;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = {"Pojo Interface"})
@Controller
public class StatisticController {

    final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @ApiOperation(value = "Get POJO statistics",
            notes = "Gets statics of POJO given by package and class name"
    )
    @GetMapping(value = "/pojoStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PojoStatistic> pojoStatistic(@ApiParam(value = "package name of POJO", required = true) @RequestParam("package") String packageName,
                                               @ApiParam(value = "class name of POJO", required = true) @RequestParam("name") String className) {
        return ResponseEntity.ok(statisticService.pojoStatistic(packageName, className));
    }

}