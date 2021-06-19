package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.rest.service.AddPojoService;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.RedirectPageContentService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.lang.model.SourceVersion;

/**
 * Controller for adding Pojos to the neo4j database
 */
@Api(tags = {"Pojo Interface"})
@Controller
public class AddPojoController {

    final AddPojoService addPojoService;

    final RedirectPageContentService redirectPageContentService;

    /**
     * Constructor for AppPojoController
     * @param addPojoService see {@link AddPojoService}
     * @param redirectPageContentService see {@link RedirectPageContentService}
     */
    public AddPojoController(AddPojoService addPojoService, RedirectPageContentService redirectPageContentService) {
        this.redirectPageContentService = redirectPageContentService;
        this.addPojoService = addPojoService;
    }

    /**
     * Add a Pojo with package name and class name
     * @param packageName package of the class
     * @param pojoName name of the class
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "Add a Pojo with package name and class name",
            notes = "Only adds Pojo if it doesn't exist in the database yet"
    )
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Redirection to Index"),
                            @ApiResponse(code = 400, message = "Parameter error")})
    @GetMapping(value = "/addPojo", produces = MediaType.TEXT_HTML_VALUE)
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
