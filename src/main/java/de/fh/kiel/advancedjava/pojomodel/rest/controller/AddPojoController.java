package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.AddPojoService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import javax.lang.model.SourceVersion;

@Api
@Controller
public class AddPojoController {
    final
    AddPojoService addPojoService;

    public AddPojoController(AddPojoService addPojoService) {
        this.addPojoService = addPojoService;
    }

    @ApiOperation(value = "Add a Pojo with package name and class name",
            notes = "Only adds Pojo if it doesn't exist in the database yet",
            response = RedirectView.class
    )
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Parameter error")})
    @GetMapping("/addPojo")
    public RedirectView addPojo(@ApiParam(value = "package of the class", required = true) @RequestParam("package") String packageName,
                                @ApiParam(value = "name of the class", required = true) @RequestParam("name") String pojoName){
        if (pojoName.isEmpty() || packageName.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pojo name and package are required"
            );
        }
        if (!SourceVersion.isIdentifier(pojoName) || SourceVersion.isKeyword(pojoName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid class name: " + pojoName
            );
        }
        packageName= packageName.replace(".", "/");
        for (String s : packageName.split("/")) {
            if (!SourceVersion.isIdentifier(s) || SourceVersion.isKeyword(s)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid package name: " + s
                );
            }
        }
        addPojoService.addPojo(pojoName, packageName);
        return new RedirectView("/index");
    }

}
