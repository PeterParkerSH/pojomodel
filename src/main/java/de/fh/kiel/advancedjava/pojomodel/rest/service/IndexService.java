package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.AttributeRs;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
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
                <p><a href="swagger-ui.html">Swagger API description</a></p>
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
                <p><a href="/jsonExport">Export data</a></p>
                <p><a href="/deleteAll">Delete all</a></p>
                <table>
                    [POJOTABLE]
                </table>
            </div>
                """;
        StringBuilder pojoTable = new StringBuilder();
        for (PojoElement element: pojoElementRepository.findAll()){
            pojoTable.append("<tr>")
                    .append("<td style='background-color:#d3d3d3;'>").append(element.getName()).append("</td>")
                    .append("<td style='background-color:#d3d3d3;'><a href=\"/packageOverview?package=").append(element.getPackageName().replace("/", ".")).append("\">").append(element.getPackageName().replace("/", ".")).append("</a></td>")
                    .append("<td>").append("<a href=\"pojoDelete?name=")
                    .append(element.getName()).append("&package=")
                    .append(element.getPackageName().replace("/", ".")).append("\">Delete</a>")
                    .append("</td>")
                    .append("<td>").append("<a href=\"pojoStatistic?name=")
                    .append(element.getName()).append("&package=")
                    .append(element.getPackageName().replace("/", ".")).append("\">Statistic</a>")
                    .append("</td>");
            if (element instanceof PojoReference || element instanceof PojoClass) {
                pojoTable
                        .append("<form method=\"GET\" action=\"/addAttribute\">")
                        .append("<td>Attribute:</td>")
                        .append("<input type=\"hidden\" name=\"pojoPackage\" value=\"").append(element.getPackageName().replace("/", ".")).append("\"/>")
                        .append("<input type=\"hidden\" name=\"pojoName\" value=\"").append(element.getName()).append("\"/>")
                        .append("<td>type: </td><td><input type=\"text\" name=\"type\"/></td>")
                        .append("<td>package: </td><td><input type=\"text\" name=\"attributePackage\"/></td>")
                        .append("<td>name: </td><td><input type=\"text\" name=\"name\"/></td>")
                        .append("<td>visibility: </td><td><input type=\"text\" name=\"visibility\"/></td>")
                        .append("<td><input type=\"submit\" value=\"Add Attribute\"/></td>")
                        .append("</form>");
            } else {
                pojoTable
                    .append("<td></td>".repeat(6));
            }
            pojoTable.append("</tr>");
            if (element instanceof PojoClass) {
                for (AttributeRs attributeRs : ((PojoClass) element).getHasAttributes()) {
                    pojoTable
                            .append("<tr>")
                            .append("<td>").append(attributeRs.getPojoElement().getName()).append("</td>")
                            .append("<td>").append(attributeRs.getName()).append("</td>")
                            .append("<td>").append("<a href=\"removeAttribute?pojoPackage=")
                            .append(element.getPackageName().replace("/", "."))
                            .append("&pojoName=").append(element.getName())
                            .append("&name=").append(attributeRs.getName())
                            .append("\">Delete</a>")
                            .append("</td>")
                            .append("<td></td>".repeat(7))
                            .append("</tr>");
                }
            }

        }
        answerHtml = answerHtml.replace("[POJOTABLE]", pojoTable.toString());

        return answerHtml;
    }
}
