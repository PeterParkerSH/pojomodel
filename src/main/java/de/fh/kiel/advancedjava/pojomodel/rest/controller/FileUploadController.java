package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.binaryreading.BinaryHandling;
import de.fh.kiel.advancedjava.pojomodel.binaryreading.ClassHandling;
import de.fh.kiel.advancedjava.pojomodel.binaryreading.ClassHandlingException;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

	private final BinaryHandling binaryHandling;

	private final ClassHandling classHandling;

	@Autowired
	FileUploadController(final BinaryHandling binaryHandling, /*final PojoClassRepository pojoClassRepository,*/
						 final ClassHandling classHandling){
		this.binaryHandling = binaryHandling;
		//this.pojoClassRepository = pojoClassRepository;
		this.classHandling = classHandling;
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			List<ClassNode> classNodeList= binaryHandling.readFile(file);

			classHandling.handleClassNodes(classNodeList);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());

			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
			);
		} catch (ClassHandlingException e) {
			LOGGER.error(e.getMessage());

			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage()
			);
		}
		return "redirect:/index";
	}

}