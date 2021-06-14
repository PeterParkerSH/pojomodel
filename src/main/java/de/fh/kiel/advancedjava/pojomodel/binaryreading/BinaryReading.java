package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@Service
@Slf4j
public class BinaryReading {

    public List<ClassNode> readFile(MultipartFile file) throws BinaryReadingException, IOException {
        if (file.getOriginalFilename().endsWith(".jar")) {
            return readJarFile(file);
        } else if (file.getOriginalFilename().endsWith(".class")){
            List<ClassNode> classes = new ArrayList<>();
            classes.add(readClassFile(file));
            return classes;
        } else {
            throw new BinaryReadingException("Unhandled file type");
        }
    }

    private List<ClassNode> readJarFile(MultipartFile mpFile) throws IOException, BinaryReadingException{
        List<ClassNode> classes = new ArrayList<>();
        JarInputStream jar = new JarInputStream(mpFile.getInputStream());
        JarEntry entry;
        do {

            entry = jar.getNextJarEntry();
            if (entry != null){
                if (entry.getName().endsWith(".class")) {
                    byte[] byteArray = IOUtils.toByteArray(jar);
                    try {
                        ClassNode cn = readClassNode(byteArray);
                        classes.add(cn);
                    }catch (BinaryReadingException e){
                        throw new BinaryReadingException("Could not read ClassNode: " + entry.getName());
                    }
                }
            }
        } while (entry != null);
        return classes;
    }


    private ClassNode readClassFile(MultipartFile mpFile) throws BinaryReadingException {
        try {
            byte[] byteArray = mpFile.getBytes();
            return readClassNode(byteArray);
        } catch (IOException | BinaryReadingException e){
            throw new BinaryReadingException("Could not read ClassNode: "+mpFile.getName());
        }
    }

    private ClassNode readClassNode(byte[] classByteArray) throws BinaryReadingException {
        ClassReader cr = new ClassReader(classByteArray);
        ClassNode classNode = new ClassNode();
        try {
            cr.accept(classNode, ClassReader.EXPAND_FRAMES);
            return classNode;
        } catch (Exception e) {
            log.error("Error decoding binaries", e);
            throw new BinaryReadingException("Error decoding binaries");
        }
    }
}
