package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.model.*;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import lombok.NonNull;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
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

    public void handleClassNode(ClassNode classNode) throws ClassHandlingException{
        if (isInterface(classNode)) {
            buildPojoInterface(classNode);
        } else {
            buildPojoClass(classNode);
        }

    }


    private void buildPojoClass(ClassNode classNode) throws ClassHandlingException{
        // TODO: How to determine if a class is an interface?
        // TODO: Still a TODO?
        String className = parseClassName(classNode.name);
        String classPackage = parsePackageName(classNode.name);

        PojoClass pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName(className, classPackage);
        if (pojoClass == null){
            // Class is not known in database
            pojoClass = createEmptyClassHull(className, classPackage);
        }

        if (!pojoClass.getEmptyHull()){
            throw new ClassHandlingException("Class " + className + "is already in the Database");
        }

        ExtendsRs extendsRs = buildExtendsRs(classNode);
        Set<ImplementsRs> implementsRsSet = buildImplementsRs(classNode);
        List<AttributeRs> attributeRsList = buildAttributeRs(classNode);

        pojoClass.setExtendsClass(extendsRs);
        pojoClass.setImplementsInterfaces(implementsRsSet);
        pojoClass.setEmptyHull(false);

        pojoClassRepository.save(pojoClass);
    }

    private void buildPojoInterface(ClassNode classNode){
        String interfaceName = parseClassName(classNode.name);
        String interfacePackage = parsePackageName(classNode.name);
        PojoInterface pojoInterface = pojoInterfaceRepository.getPojoInterfaceByNameAndPackageName(interfaceName, interfacePackage);
        if (pojoInterface == null){
            pojoInterface = PojoInterface.builder().name(interfaceName).packageName(interfacePackage).build();
            pojoInterfaceRepository.save(pojoInterface);
        }
    }

    private ExtendsRs buildExtendsRs(ClassNode classNode){
        String superClassName = parseClassName(classNode.superName);
        String superClassPackage = parsePackageName(classNode.superName);

        // Avoid Object Notes as Parent class
        if (!(superClassName.equals("Object") && superClassPackage.equals("java/lang"))){
            // search for super class, if not existing create empty hull
            PojoClass superClass = pojoClassRepository.getPojoClassByNameAndPackageName(superClassName, superClassPackage);
            if (superClass == null) {
                superClass = createEmptyClassHull(superClassName, superClassPackage);
            }
            return ExtendsRs.builder().pojoClass(superClass).build();
        }
        return null;
    }

    private Set<ImplementsRs> buildImplementsRs(@NonNull ClassNode classNode){
        HashSet<ImplementsRs> result = new HashSet<>();



        for (Object interf: classNode.interfaces) {
            if (interf instanceof String) {
                String interfaceString = (String) interf;
                String interfaceName = parseClassName(interfaceString);
                String interfacePackage = parsePackageName(interfaceString);
                PojoInterface pojoInterface = pojoInterfaceRepository.getPojoInterfaceByNameAndPackageName(interfaceName, interfacePackage);
                if (pojoInterface == null) {
                    pojoInterface = PojoInterface.builder().name(interfaceName).packageName(interfacePackage).build();
                }
                result.add(ImplementsRs.builder().pojoInterface(pojoInterface).build());
            }
        };

        return result;
    }

    private List<AttributeRs> buildAttributeRs(ClassNode classNode) {
        ArrayList<AttributeRs> result = new ArrayList<>();

        if (classNode.fields != null) {
            for (Object field: classNode.fields){
                if(field instanceof FieldNode) {
                    FieldNode a = (FieldNode) field;

                }

            }
        }
        return result;
    }

    private PojoClass createEmptyClassHull(String className, String packageName){
        PojoClass emptyHull = PojoClass.builder().name(className).packageName(packageName).emptyHull(true).build();
        pojoClassRepository.save(emptyHull);
        return emptyHull;
    }
}
