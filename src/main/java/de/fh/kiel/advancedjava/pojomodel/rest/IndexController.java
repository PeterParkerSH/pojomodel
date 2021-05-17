package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {

    final PojoElementRepository pojoElementRepository;

    public IndexController(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }

    @GetMapping("/index")
    public @ResponseBody String listUploadedFiles() {
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
        StringBuilder pojoTable = new StringBuilder();
        for (PojoElement element: pojoElementRepository.findAll()){
           pojoTable.append("<tr>")
                        .append("<td>").append(element.getName()).append("</td>")
                        .append("<td>").append(element.getPackageName()).append("</td>")
                        .append("<td>").append("<a href=\"pojoDelete?name=")
                            .append(element.getName()).append("&package=")
                            .append(element.getPackageName().replace("/", ".")).append("\">Delete")
                            .append("</td>")
                    .append("</tr>");
        }
        answerHtml = answerHtml.replace("[POJOTABLE]", pojoTable.toString());

        return answerHtml;
    }
}
