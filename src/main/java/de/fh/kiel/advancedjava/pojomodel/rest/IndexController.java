package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
public class IndexController {

    @Autowired
    PojoElementRepository pojoElementRepository;

    @GetMapping("/index")
    public @ResponseBody String listUploadedFiles(Model model) throws IOException {
        String answerHtml = """
   
            
            <div>
                <form method="POST" enctype="multipart/form-data" action="/upload">
                    <table>
                        <tr>
                            <td>JAR/Class to upload:</td>
                            <td><input type="file" name="file" accept=".class,.jar"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="Upload"/></td>
                        </tr>
                    </table>
                </form>
            
                <form method="POST" enctype="multipart/form-data" action="/jsonImport">
                    <table>
                        <tr>
                            <td>Json File to upload:</td>
                            <td><input type="file" name="json" accept=".json"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="Upload"/></td>
                        </tr>
                    </table>
                </form>
                <a href="/jsonExport">Export data</a>
                <a href="/deleteAll">Delete all</a>
                <table>
                    [POJOTABLE]
                </table>
            </div>
             
                """;
        String pojoTable = "";
        for (PojoElement element: pojoElementRepository.findAll()){
           pojoTable = pojoTable + "<tr>" +
                       "<td>" + element.getName() + "</td>" +
                       "<td>" + element.getPackageName() + "</td>" +
                       "<td>" + "<a href=\"pojoDelete/"+element.getName()+"/" + element.getPackageName().replace("/",".")+ "\">Delete" + "</td>" +
                   "</tr>";
        }
        answerHtml = answerHtml.replace("[POJOTABLE]", pojoTable);

        return answerHtml;
    }
}
