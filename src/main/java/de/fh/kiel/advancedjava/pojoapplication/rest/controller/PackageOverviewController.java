package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.ApiPackageOverviewElement;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.PackageOverviewService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller for receiving an overview over the contents of a package
 */
@Api(tags = {"Pojo Interface"})
@Controller
public class PackageOverviewController {

    final PackageOverviewService packageOverviewService;

    /**
     * Constructor for PackageOverviewController
     * @param packageOverviewService see {@link PackageOverviewService}
     */
    public PackageOverviewController(PackageOverviewService packageOverviewService){
        this.packageOverviewService = packageOverviewService;
    }

    /**
     * Returns a list of elements in a specific packages and its subpackages
     * @param packageName name of package
     * @return {@code ResponseEntity<List<ApiPackageOverviewElement>>} json result
     */
    @ApiOperation(value = "Returns a list of elements in a specific packages and its subpackages"
    )
    @GetMapping(value = "/packageOverview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApiPackageOverviewElement>> packageOverview(@ApiParam(value = "package name", required = true) @RequestParam("package") String packageName){
        packageName = packageName.replace(".", "/");
        return ResponseEntity.ok(packageOverviewService.packageOverview(packageName));
    }


}
