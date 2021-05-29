package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.AddPojoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.lang.model.SourceVersion;
import java.util.Arrays;

@Controller
public class AddPojoController {
    final
    AddPojoService addPojoService;

    public AddPojoController(AddPojoService addPojoService) {
        this.addPojoService = addPojoService;
    }

    @GetMapping("/addPojo")
    public String addPojo(@RequestParam("package") String packageName, @RequestParam("name") String pojoName){
        if (pojoName.equals("") || packageName.equals("")){
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
        return "redirect:/index";
    }

}
