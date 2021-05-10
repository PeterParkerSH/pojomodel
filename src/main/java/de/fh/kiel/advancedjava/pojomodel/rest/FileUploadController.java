package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.binaryreading.ClassHandling;
import de.fh.kiel.advancedjava.pojomodel.binaryreading.ClassHandlingException;
import de.fh.kiel.advancedjava.pojomodel.binaryreading.JarHandling;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * In order to upload class and jar files you may choose to either encode binary data in base64
 * or instead upload this jar's / classes using a form. This class shows you the server side - the "client"
 * part is located in the upload.html file. In order to upload a file you have to start the application
 * and hit http://localhost:8080/upload and choose a file for upload and upload it. This class is only an example
 * and not feature complete.
 */
@Controller
public class FileUploadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
	private JarHandling jarHandling;
	//private PojoClassRepository pojoClassRepository;
	private ClassHandling classHandling;
	@Autowired
	FileUploadController(final JarHandling jarHandling, /*final PojoClassRepository pojoClassRepository,*/
						 final ClassHandling classHandling){
		this.jarHandling = jarHandling;
		//this.pojoClassRepository = pojoClassRepository;
		this.classHandling = classHandling;
	}

	@GetMapping("/upload")
	public String listUploadedFiles(Model model) throws IOException {

		return "upload";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			for (ClassNode classNode: jarHandling.readFile(file)) {
				classHandling.handleClassNode(classNode);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
			);
		} catch (ClassHandlingException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage()
			);

		}


		return "redirect:/upload";
	}

}