package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Pojo Interface"})
@Controller
public class IndexController {

    final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @ApiOperation(value = "Get index page",
            notes = "Index page lists all existing POJOs"
            //, response = String.class
    )
    @GetMapping("/index")
    public ResponseEntity<String> listUploadedFiles() {
        return ResponseEntity.ok(indexService.listUploadedFiles());
    }

    @ApiIgnore
    @GetMapping("/")
    public RedirectView redirectIndex() {
        return new RedirectView("/index");
    }

}
