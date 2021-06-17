package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.PojoAttributeService;
import de.fh.kiel.advancedjava.pojomodel.rest.service.RedirectPageContentService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


import javax.lang.model.SourceVersion;

@Api(tags = {"Pojo Interface"})
@Controller
public class PojoAttributeController {
    final PojoAttributeService pojoAttributeService;
    final RedirectPageContentService redirectPageContentService;

    public PojoAttributeController(PojoAttributeService pojoAttributeService,
                                   RedirectPageContentService redirectPageContentService) {
        this.pojoAttributeService = pojoAttributeService;
        this.redirectPageContentService = redirectPageContentService;
    }

    @ApiOperation(value = "Add an attribute to an existing POJO",
            notes = "Only adds the attribute if it doesn't exist in the POJO yet"

    )
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Parameter error")})
    @GetMapping(value = "/addAttribute", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> addAttribute(@ApiParam(value = "package of POJO", required = true) @RequestParam("pojoPackage") String pojoPackage,
                                               @ApiParam(value = "name of POJO", required = true) @RequestParam("pojoName") String pojoName,
                                               @ApiParam(value = "type of attribute", required = true) @RequestParam("type") String type,
                                               @ApiParam(value = "name of attribute", required = true) @RequestParam("name") String name,
                                               @ApiParam(value = "visibility of attribute", required = true) @RequestParam("visibility") String visibility,
                                               @ApiParam(value = "package of attribute", required = true) @RequestParam("attributePackage") String attributePackage) {
        if (type.isEmpty() || name.isEmpty() || visibility.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Attribute type, name and visibility are required"
            );
        }
        if (!SourceVersion.isIdentifier(name) || SourceVersion.isKeyword(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid attribute name: " + name
            );
        }
        if (!(visibility.equals("private") || visibility.equals("protected") || visibility.equals("public"))){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Visibility must be one of: private, protected, public"
            );
        }
        pojoPackage = pojoPackage.replace(".", "/");
        attributePackage = attributePackage.replace(".", "/");

        pojoAttributeService.addAttribute(pojoPackage, pojoName, type, name, visibility, attributePackage);
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }

    @ApiOperation(value = "Remove an attribute from an existing POJO",
            notes = "Removes an attribute if it exists in the given POJO"
    )

    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
                            @ApiResponse(code = 400, message = "Parameter error")})
    @GetMapping(value = "/removeAttribute", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> removeAttribute(@ApiParam(value = "package of POJO", required = true) @RequestParam("pojoPackage") String pojoPackage,
                                     @ApiParam(value = "name of POJO", required = true) @RequestParam("pojoName") String pojoName,
                                     @ApiParam(value = "name of attribute", required = true) @RequestParam("name") String name) {
        if (name.isEmpty() || pojoName.isEmpty() ){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO name and attribute name are required"
            );
        }
        pojoPackage = pojoPackage.replace(".", "/");
        pojoAttributeService.removeAttribute(pojoPackage, pojoName, name);

        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }
}