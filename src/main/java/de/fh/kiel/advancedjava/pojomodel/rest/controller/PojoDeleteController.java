package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.service.PojoDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PojoDeleteController {

    @Autowired
    PojoDeleteService pojoDeleteService;

    @GetMapping("/deleteAll")
    public String deleteAll() {
        pojoDeleteService.deleteAll();
        return "redirect:/index";
    }

    @GetMapping("/pojoDelete")
    public String pojoDelete(@RequestParam("package") String packageName, @RequestParam("name") String className){
        pojoDeleteService.pojoDelete(packageName, className);
        return "redirect:/index";
    }
}
