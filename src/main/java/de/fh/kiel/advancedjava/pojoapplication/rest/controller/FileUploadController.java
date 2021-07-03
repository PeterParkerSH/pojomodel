package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.binaryreading.BinaryReading;
import de.fh.kiel.advancedjava.pojoapplication.binaryreading.BinaryReadingException;
import de.fh.kiel.advancedjava.pojoapplication.rest.exceptions.ClassHandlingException;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.ClassHandlingService;
import de.fh.kiel.advancedjava.pojoapplication.rest.service.RedirectPageContentService;
import io.swagger.annotations.*;
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
 * Controller for uploading Pojos as a JAR or class file. In order to upload a file you have to start the application and hit http://localhost:8080/index
 */
@Api(tags = {"Pojo Interface"})
@Controller
public class FileUploadController {
	private final BinaryReading binaryReading;
	private final ClassHandlingService classHandlingService;
	private final RedirectPageContentService redirectPageContentService;

	/**
	 * Constructor for FileUploadController
	 * @param binaryReading see {@link BinaryReading}
	 * @param classHandlingService see {@link ClassHandlingService}
	 * @param redirectPageContentService see {@link RedirectPageContentService}
	 */
	FileUploadController(BinaryReading binaryReading,
						 ClassHandlingService classHandlingService,
						 RedirectPageContentService redirectPageContentService){
		this.binaryReading = binaryReading;
		this.classHandlingService = classHandlingService;
		this.redirectPageContentService = redirectPageContentService;
	}

	/**
	 * Upload a JAR or Class file
	 * @param file File to be uploaded, must be .jar or .class file
	 * @return {@code ResponseEntity<String>} HTML response
	 */
	@ApiOperation(value = "Upload a JAR or Class file",
			notes = "Does not add duplicates to the database"
			)
	@ApiResponses(value = { @ApiResponse(code = 415, message = "Invalid data format")})
	@PostMapping(value = "/upload", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> uploadFile(@ApiParam(value = "File to be uploaded", required = true) @RequestParam("file") MultipartFile file) {
		try {
			List<ClassNode> classNodeList= binaryReading.readFile(file);

			classHandlingService.handleClassNodes(classNodeList);
		} catch (IOException e) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file"
			);
		} catch (BinaryReadingException | ClassHandlingException e) {
			throw new ResponseStatusException(
					HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage()
			);
		}
		return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
	}

}