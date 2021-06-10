package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Api
@Controller
public class IndexController {

    @Autowired
    IndexService indexService;

    @ApiOperation(value = "Get index page",
            notes = "Index page lists all existing POJOs",
            response = String.class
    )
    @GetMapping("/index")
    public @ResponseBody String listUploadedFiles() {
        return indexService.listUploadedFiles();
    }
}
