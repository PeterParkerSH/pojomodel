package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiOverviewElement;
import de.fh.kiel.advancedjava.pojomodel.rest.service.PackageOverviewService;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Api
@Controller
public class PackageOverviewController {

    final PackageOverviewService packageOverviewService;

    public PackageOverviewController(PackageOverviewService packageOverviewService){
        this.packageOverviewService = packageOverviewService;
    }

    @ApiOperation(value = "Returns a list of elements in a specific packages and its subpackages",
            response = ApiOverviewElement.class,
            responseContainer = "List"
    )

    @GetMapping("/getOverview")
    public List<ApiOverviewElement> getOverview(@ApiParam(value = "package name", required = true) @RequestParam("package") String packageName){
        packageName = packageName.replace(".", "/");
        return packageOverviewService.getOverview(packageName);
    }


}
