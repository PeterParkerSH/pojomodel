package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.PojoDeleteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Api
@Controller
public class PojoDeleteController {

    @Autowired
    PojoDeleteService pojoDeleteService;

    @ApiOperation(value = "Delete all POJOs",
            notes = "Clears database of all POJOs",
            response = RedirectView.class
    )
    @GetMapping("/deleteAll")
    public RedirectView deleteAll() {
        pojoDeleteService.deleteAll();
        return new RedirectView("/index");
    }

    @ApiOperation(value = "Delete POJO by package and class name",
            notes = "Deletes POJO given by package and class name, if existing",
            response = RedirectView.class
    )
    @GetMapping("/pojoDelete")
    public RedirectView pojoDelete(@ApiParam(value = "package name of POJO", required = true) @RequestParam("package") String packageName,
                                   @ApiParam(value = "class name of POJO", required = true) @RequestParam("name") String className){
        pojoDeleteService.pojoDelete(packageName, className);
        return new RedirectView("/index");
    }
}
