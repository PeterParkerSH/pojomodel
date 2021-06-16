package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPackageOverviewElement;
import de.fh.kiel.advancedjava.pojomodel.rest.service.PackageOverviewService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = {"Pojo Interface"})
@Controller
public class PackageOverviewController {

    final PackageOverviewService packageOverviewService;

    public PackageOverviewController(PackageOverviewService packageOverviewService){
        this.packageOverviewService = packageOverviewService;
    }

    @ApiOperation(value = "Returns a list of elements in a specific packages and its subpackages"
//            , response = ApiPackageOverviewElement.class,
//            responseContainer = "List"
    )

    @GetMapping("/packageOverview")
    public ResponseEntity<List<ApiPackageOverviewElement>> packageOverview(@ApiParam(value = "package name", required = true) @RequestParam("package") String packageName){
        packageName = packageName.replace(".", "/");
        return ResponseEntity.ok(packageOverviewService.packageOverview(packageName));
    }


}
