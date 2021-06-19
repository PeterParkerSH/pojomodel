package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.rest.service.PojoDeleteService;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.RedirectPageContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for deleting Pojos
 */
@Api(tags = {"Pojo Interface"})
@Controller
public class PojoDeleteController {

    final PojoDeleteService pojoDeleteService;
    final RedirectPageContentService redirectPageContentService;

    /**
     * Constructor for PojoDeleteController
     * @param pojoDeleteService see {@link PojoDeleteService}
     * @param redirectPageContentService see {@link RedirectPageContentService}
     */
    public PojoDeleteController(PojoDeleteService pojoDeleteService,
                                RedirectPageContentService redirectPageContentService) {
        this.pojoDeleteService = pojoDeleteService;
        this.redirectPageContentService = redirectPageContentService;
    }

    /**
     * Delete all POJOs
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "Delete all POJOs",
            notes = "Clears database of all POJOs"
    )
    @GetMapping(value = "/deleteAll", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> deleteAll() {
        pojoDeleteService.deleteAll();
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }

    /**
     * Delete POJO by package and class name
     * @param packageName package name of Pojo
     * @param className class name of Pojo
     * @return ResponseEntity<String>
     */
    @ApiOperation(value = "Delete POJO by package and class name",
            notes = "Deletes POJO given by package and class name, if existing"
    )
    @GetMapping(value ="/pojoDelete", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> pojoDelete(@ApiParam(value = "package name of POJO", required = true) @RequestParam("package") String packageName,
                                             @ApiParam(value = "class name of POJO", required = true) @RequestParam("name") String className){
        pojoDeleteService.pojoDelete(packageName, className);
        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }
}
