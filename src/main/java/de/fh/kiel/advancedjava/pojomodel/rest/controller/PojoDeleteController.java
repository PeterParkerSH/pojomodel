package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.PojoDeleteService;
import de.fh.kiel.advancedjava.pojomodel.rest.service.RedirectPageContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = {"Pojo Interface"})
@Controller
public class PojoDeleteController {

    final PojoDeleteService pojoDeleteService;
    final RedirectPageContentService redirectPageContentService;

    public PojoDeleteController(PojoDeleteService pojoDeleteService,
                                RedirectPageContentService redirectPageContentService) {
        this.pojoDeleteService = pojoDeleteService;
        this.redirectPageContentService = redirectPageContentService;
    }

    @ApiOperation(value = "Delete all POJOs",
            notes = "Clears database of all POJOs"
    )
    @GetMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
        pojoDeleteService.deleteAll();
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }

    @ApiOperation(value = "Delete POJO by package and class name",
            notes = "Deletes POJO given by package and class name, if existing"
    )
    @GetMapping("/pojoDelete")
    public ResponseEntity<String> pojoDelete(@ApiParam(value = "package name of POJO", required = true) @RequestParam("package") String packageName,
                                             @ApiParam(value = "class name of POJO", required = true) @RequestParam("name") String className){
        pojoDeleteService.pojoDelete(packageName, className);
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }
}
