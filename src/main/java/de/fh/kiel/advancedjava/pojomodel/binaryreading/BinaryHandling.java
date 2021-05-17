package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import org.apache.commons.io.IOUtils;
import org.neo4j.driver.util.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@Service
public class BinaryHandling {
    public List<ClassNode> readFile(MultipartFile file) throws ClassHandlingException, IOException {
        if (file.getOriginalFilename().endsWith(".jar")) {
            return readJarFile(file);
        } else if (file.getOriginalFilename().endsWith(".class")){
            List<ClassNode> classes = new ArrayList<>();
            classes.add(readClassFile(file));
            return classes;
        } else {
            throw new ClassHandlingException("Unhandled file type");
        }
    }

    private List<ClassNode> readJarFile(MultipartFile mpFile) throws IOException{
        List<ClassNode> classes = new ArrayList<>();
        JarInputStream jar = new JarInputStream(mpFile.getInputStream());
        JarEntry entry;
        do {
            entry = jar.getNextJarEntry();
            if (entry != null){
                if (entry.getName().endsWith(".class")) {
                    //JarInputStream jip = new JarInputStream(jar);
                    byte[] byteArray = IOUtils.toByteArray(jar);
                    classes.add(readClassNode(byteArray));
                }
            }

        } while (entry != null);
        return classes;
    }


    private ClassNode readClassFile(MultipartFile mpFile){
        try {
            byte[] byteArray = mpFile.getBytes();
            return readClassNode(byteArray);

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private ClassNode readClassNode(byte[] classByteArray){
        ClassReader cr = new ClassReader(classByteArray);
        ClassNode classNode = new ClassNode();
        try {
            cr.accept(classNode, ClassReader.EXPAND_FRAMES);
            return classNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}