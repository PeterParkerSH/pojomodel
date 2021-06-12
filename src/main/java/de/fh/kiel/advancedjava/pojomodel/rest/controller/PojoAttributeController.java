package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.AddPojoService;
import de.fh.kiel.advancedjava.pojomodel.rest.service.PojoAttributeService;
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
public class PojoAttributeController {
    final
    PojoAttributeService pojoAttributeService;

    public PojoAttributeController(PojoAttributeService pojoAttributeService) {
        this.pojoAttributeService = pojoAttributeService;
    }

    @ApiOperation(value = "Add an attribute to an existing POJO",
            notes = "Only adds the attribute if it doesn't exist in the POJO yet",
            response = RedirectView.class
    )
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Parameter error")})
    @GetMapping("/addAttribute")
    public RedirectView addAttribute(@ApiParam(value = "id of POJO", required = true) @RequestParam("id") long id,
                                     @ApiParam(value = "type of attribute", required = true) @RequestParam("type") String type,
                                     @ApiParam(value = "name of attribute", required = true) @RequestParam("name") String name,
                                     @ApiParam(value = "visibility of attribute", required = true) @RequestParam("visibility") String visibility,
                                     @ApiParam(value = "package of attribute", required = true) @RequestParam("package") String packageName) {
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
        pojoAttributeService.addAttribute(id, type, name, visibility, packageName);
        return new RedirectView("index");
    }

}