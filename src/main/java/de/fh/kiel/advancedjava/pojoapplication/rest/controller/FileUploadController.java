package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.binaryreading.BinaryReading;
import de.fh.kiel.advancedjava.pojoapplication.binaryreading.BinaryReadingException;
import de.fh.kiel.advancedjava.pojoapplication.rest.exceptions.ClassHandlingException;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.ClassHandlingService;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.RedirectPageContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Api(tags = {"Pojo Interface"})
@Controller
public class FileUploadController {
	private final BinaryReading binaryReading;
	private final ClassHandlingService classHandlingService;
	private final RedirectPageContentService redirectPageContentService;

	FileUploadController(BinaryReading binaryReading, /*final PojoClassRepository pojoClassRepository,*/
						 ClassHandlingService classHandlingService,
						 RedirectPageContentService redirectPageContentService){
		this.binaryReading = binaryReading;
		this.classHandlingService = classHandlingService;
		this.redirectPageContentService = redirectPageContentService;
	}

	@ApiOperation(value = "Upload a JAR or Class file",
			notes = "Does not add duplicates to the database"
			)
	@PostMapping(value = "/upload", produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<String> uploadFile(@ApiParam(value = "File to be uploaded", required = true) @RequestParam("file") MultipartFile file) {
		try {
			List<ClassNode> classNodeList= binaryReading.readFile(file);

			classHandlingService.handleClassNodes(classNodeList);
		} catch (IOException e) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
			);
		} catch (BinaryReadingException | ClassHandlingException e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage()
			);
		}
		return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
	}

}