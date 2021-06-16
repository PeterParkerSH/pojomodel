package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ErrorMessage;
import de.fh.kiel.advancedjava.pojomodel.rest.service.AddPojoService;
import de.fh.kiel.advancedjava.pojomodel.rest.service.RedirectPageContentService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.lang.model.SourceVersion;

@Api(tags = {"Pojo Interface"})
@Controller
public class AddPojoController {

    final AddPojoService addPojoService;

    final RedirectPageContentService redirectPageContentService;

    public AddPojoController(AddPojoService addPojoService, RedirectPageContentService redirectPageContentService) {
        this.redirectPageContentService = redirectPageContentService;
        this.addPojoService = addPojoService;
    }

    @ApiOperation(value = "Add a Pojo with package name and class name",
            notes = "Only adds Pojo if it doesn't exist in the database yet"
    )
    @ApiResponses(value = { @ApiResponse(code = 200, response = void.class, message = "Redirection to Index"),
                         //   @ApiResponse(code = 300, response = void.class, message = "Redirect to index"),
                            @ApiResponse(code = 400, response = ErrorMessage.class, message = "Parameter error")})
    @GetMapping(value = "/addPojo")

    public ResponseEntity<String> addPojo(@ApiParam(value = "package of the class", required = true) @RequestParam("package") String packageName,
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
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }

}
