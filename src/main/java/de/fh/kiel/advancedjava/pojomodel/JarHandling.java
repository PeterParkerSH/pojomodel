package de.fh.kiel.advancedjava.pojomodel;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarHandling {
    public List<ClassNode> loadClasses(MultipartFile mpFile) throws IOException{
        List<ClassNode> classes = new ArrayList<>();
        JarInputStream jar = new JarInputStream(mpFile.getInputStream());
        JarEntry entry;
        do {
            entry = jar.getNextJarEntry();
            if (entry != null){
                if (entry.getName().endsWith(".class")) {
                    //JarInputStream jip = new JarInputStream(jar);
                    byte[] bytes = IOUtils.toByteArray(jar);
                    ClassReader cr = new ClassReader(bytes);
                    ClassNode cn = new ClassNode();
                    try {
                        cr.accept(cn, ClassReader.EXPAND_FRAMES);
                        System.out.println(cn.name);
                        classes.add(cn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } while (entry != null);
        return classes;
    }


    public ClassNode readClassFile(MultipartFile mpFile){
        try {
            byte[] cfarray = mpFile.getBytes();
            ClassReader cr = new ClassReader(cfarray);
            ClassNode cn = new ClassNode();
            try {
                cr.accept(cn, ClassReader.EXPAND_FRAMES);
                return cn;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
