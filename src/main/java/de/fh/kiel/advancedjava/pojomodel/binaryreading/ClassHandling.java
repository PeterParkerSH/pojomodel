package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.model.ExtendsRs;
import de.fh.kiel.advancedjava.pojomodel.model.ImplementsRs;
import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClassHandling {
    private final PojoClassRepository pojoClassRepository;
    private final PojoInterfaceRepository  pojoInterfaceRepository;

    @Autowired
    public ClassHandling(final PojoClassRepository pojoClassRepository, final PojoInterfaceRepository  pojoInterfaceRepository){
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
    }

    private String parsePackageName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");
        return completeName.substring(0, packageEnd);

    }

    private String parseClassName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");
        return completeName.substring(packageEnd+1);
    }

    private boolean isInterface(ClassNode classNode){
        return (classNode.access & Opcodes.ACC_INTERFACE) != 0;
    }

    public void handleClassNode(ClassNode classNode){
        if (isInterface(classNode)) {
            buildPojoInterface(classNode);
        } else {
            buildPojoClass(classNode);
        }

    }


    private void buildPojoClass(ClassNode classNode){
        // TODO: How to determine if a class is an interface?
        // TODO: Still a TODO?
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

    private void buildPojoInterface(ClassNode classNode){
        String interfaceName = parseClassName(classNode.name);
        String interfacePackage = parsePackageName(classNode.name);
        PojoInterface pojoInterface = pojoInterfaceRepository.getPojoInterfaceByInterfaceNameAndPackageName(interfaceName, interfacePackage);
        if (pojoInterface == null){
            pojoInterface = PojoInterface.builder().interfaceName(interfaceName).packageName(interfacePackage).build();
            pojoInterfaceRepository.save(pojoInterface);
        }
    }

    private ExtendsRs buildExtendsRs(ClassNode classNode){
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

    private Set<ImplementsRs> buildImplementsRs(ClassNode classNode){
        HashSet<ImplementsRs> result = new HashSet<>();

        classNode.interfaces.forEach(interf -> {
            if (interf instanceof String) {
                String interfaceString = (String) interf;
                String interfaceName = parseClassName(interfaceString);
                String interfacePackage = parsePackageName(interfaceString);
                PojoInterface pojoInterface = pojoInterfaceRepository.getPojoInterfaceByInterfaceNameAndPackageName(interfaceName, interfacePackage);
                if (pojoInterface == null){
                    pojoInterface = PojoInterface.builder().interfaceName(interfaceName).packageName(interfacePackage).build();
                }
                result.add(ImplementsRs.builder().pojoInterface(pojoInterface).build());
            }
        });
        return result;
    }

    private PojoClass createEmptyClassHull(String className, String packageName){
        PojoClass emptyHull = PojoClass.builder().className(className).packageName(packageName).build();
        pojoClassRepository.save(emptyHull);
        return emptyHull;
    }
}
