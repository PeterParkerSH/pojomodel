package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    final PojoElementRepository pojoElementRepository;

    public IndexService(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }


    public String listUploadedFiles() {
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
                
                Add class with class name and package name:
                <form method="GET" action="/addPojo">
                    <table>
                        <tr>
                            <td>Class Name:</td>
                            <td><input type="text" name="name"/></td>
                        </tr>
                        <tr>
                            <td>Package Name:</td>
                            <td><input type="text" name="package"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="Add POJO"/></td>
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
                    .append("<td>").append("<a href=\"pojoStatistic?name=")
                    .append(element.getName()).append("&package=")
                    .append(element.getPackageName().replace("/", ".")).append("\">Statistic")
                    .append("</td>")
                    .append("<form method=\"GET\" action=\"/addAttribute\">")
                    .append("<td>Attribute: id: </td><td><input type=\"text\" name=\"id\" value=\"")
                    .append(element.getId())
                    .append("\"/></td>")
                    .append("<td>type: </td><td><input type=\"text\" name=\"type\"/></td>")
                    .append("<td>name: </td><td><input type=\"text\" name=\"name\"/></td>")
                    .append("<td>visibility: </td><td><input type=\"text\" name=\"visibility\"/></td>")
                    .append("<td>package: </td><td><input type=\"text\" name=\"package\"/></td>")
                    .append("<td><input type=\"submit\" value=\"Add Attribute\"/></td>")
                    .append("</form>")
                    .append("</tr>");

        }
        answerHtml = answerHtml.replace("[POJOTABLE]", pojoTable.toString());

        return answerHtml;
    }
}
