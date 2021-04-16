package de.fh.kiel.advancedjava.pojomodel.upload;

import de.fh.kiel.advancedjava.pojomodel.binaryreading.JarHandling;
import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * In order to upload class and jar files you may choose to either encode binary data in base64
 * or instead upload this jar's / classes using a form. This class shows you the server side - the "client"
 * part is located in the upload.html file. In order to upload a file you have to start the application
 * and hit http://localhost:8080/upload and choose a file for upload and upload it. This class is only an example
 * and not feature complete.
 */
@Controller
public class FileUploadController {

	private JarHandling jarHandling;
	private PojoClassRepository pojoClassRepository;
	@Autowired
	FileUploadController(final JarHandling jarHandling, final PojoClassRepository pojoClassRepository){
		this.jarHandling = jarHandling;
		this.pojoClassRepository = pojoClassRepository;
	}

	@GetMapping("/upload")
	public String listUploadedFiles(Model model) throws IOException {

		return "upload";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			jarHandling.readFile(file).forEach(classNode -> {


				PojoClass pojoClass = pojoClassRepository.getPojoClassByClassNameAndPackageName(classNode.sourceFile, classNode.name);
				if (pojoClass == null){
					pojoClass = PojoClass.builder().className(classNode.sourceFile).packageName(classNode.name).build();
				}else{

					// Add members etc.
				}
				pojoClassRepository.save(pojoClass);
			});
		}catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(file.getOriginalFilename());

		return "redirect:/upload";
	}

}