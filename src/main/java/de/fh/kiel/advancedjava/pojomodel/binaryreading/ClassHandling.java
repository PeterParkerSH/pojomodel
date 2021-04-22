package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.model.ExtendsRs;
import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClassHandling {
    private PojoClassRepository pojoClassRepository;

    @Autowired
    public ClassHandling(final PojoClassRepository pojoClassRepository){
        this.pojoClassRepository = pojoClassRepository;
    }

    public void buildPojoClass(ClassNode classNode){
        PojoClass pojoClass = pojoClassRepository.getPojoClassByClassNameAndPackageName(classNode.sourceFile, classNode.name);
        if (pojoClass == null){
            ExtendsRs extendsRs = buildExtendsRs(classNode);
            Set<ExtendsRs> extendsRsSet = null;
            if (extendsRs != null) {
                extendsRsSet = new HashSet<>(Arrays.asList(extendsRs));
            }
            pojoClass = PojoClass.builder()
                    .className(classNode.sourceFile)
                    .packageName(classNode.name)
                    .extendsClasses(extendsRsSet)
                    .build();
        }else{

            // Add members etc.
        }
        pojoClassRepository.save(pojoClass);
    }

    public ExtendsRs buildExtendsRs(ClassNode classNode){
        ExtendsRs extendsRs = null;
        // TODO: Mehrfachvererbung
        String superClassName = classNode.superName;
        if (!superClassName.equals("java/lang/Object")){
            // search for super class, if not existing create empty hull
            PojoClass superClass = pojoClassRepository.getPojoClassByClassNameAndPackageName(classNode.sourceFile, classNode.name);
            if (superClass == null) {
                superClass = createEmptyHull(superClassName);
            }
            extendsRs = ExtendsRs.builder().pojoClass(superClass).build();
        }
        return extendsRs;
    }

    public PojoClass createEmptyHull(String className){
        PojoClass emptyHull = PojoClass.builder().className(className).build();
        pojoClassRepository.save(emptyHull);
        return emptyHull;
    }
}
