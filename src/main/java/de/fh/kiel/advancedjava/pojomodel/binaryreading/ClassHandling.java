package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.model.ExtendsRs;
import de.fh.kiel.advancedjava.pojomodel.model.ImplementsRs;
import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClassHandling {
    private PojoClassRepository pojoClassRepository;

    @Autowired
    public ClassHandling(final PojoClassRepository pojoClassRepository){
        this.pojoClassRepository = pojoClassRepository;
    }

    private String parsePackageName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");
        return completeName.substring(0, packageEnd);

    }

    private String parseClassName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");
        return completeName.substring(packageEnd+1);
    }

    public void buildPojoClass(ClassNode classNode){

        String className = parseClassName(classNode.name);
        String classPackage = parsePackageName(classNode.name);

        PojoClass pojoClass = pojoClassRepository.getPojoClassByClassNameAndPackageName(className, classPackage);
        if (pojoClass == null){
            // Class is not known in database

            pojoClass = PojoClass.builder()
                    .className(className)
                    .packageName(classPackage).build();
        }


        ExtendsRs extendsRs = buildExtendsRs(classNode);
        Set<ImplementsRs> implementsRsSet = buildImplementsRs(classNode);

        pojoClass.setExtendsClass(extendsRs);
        pojoClass.setImplementsInterfaces(implementsRsSet);

        pojoClassRepository.save(pojoClass);
    }

    public ExtendsRs buildExtendsRs(ClassNode classNode){
        String superClassName = parseClassName(classNode.superName);
        String superClassPackage = parsePackageName(classNode.superName);

        if (!(superClassName.equals("Object") && superClassPackage.equals("java/lang"))){
            // search for super class, if not existing create empty hull
            PojoClass superClass = pojoClassRepository.getPojoClassByClassNameAndPackageName(superClassName, superClassPackage);
            if (superClass == null) {
                superClass = createEmptyClassHull(superClassName, superClassPackage);
            }
            return ExtendsRs.builder().pojoClass(superClass).build();
        }
        return null;
    }

    public Set<ImplementsRs> buildImplementsRs(ClassNode classNode){
        HashSet<ImplementsRs> result = new HashSet<>();

        classNode.interfaces.forEach(interf -> {
            if (interf instanceof String) {
                String interfaceString = (String) interf;
                String interfaceName = parseClassName(interfaceString);
                String interfacePackage = parsePackageName(interfaceString);
                // TODO: PojoClass repo cannot return type PojoInterface
                PojoInterface pojoInterface = pojoClassRepository.getPojoInterfaceByInterfaceNameAndPackageName(interfaceName, interfacePackage);
                if (pojoInterface == null){
                    pojoInterface = PojoInterface.builder().interfaceName(interfaceName).packageName(interfacePackage).build();
                }
                result.add(ImplementsRs.builder().pojoInterface(pojoInterface).build());
            }
        });
        return result;
    }

    public PojoClass createEmptyClassHull(String className, String packageName){
        PojoClass emptyHull = PojoClass.builder().className(className).packageName(packageName).build();
        pojoClassRepository.save(emptyHull);
        return emptyHull;
    }
}
