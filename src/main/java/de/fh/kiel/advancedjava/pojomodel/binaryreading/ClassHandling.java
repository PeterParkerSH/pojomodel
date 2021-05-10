package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.model.*;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import lombok.NonNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHandling.class);

    private final PojoClassRepository pojoClassRepository;
    private final PojoInterfaceRepository  pojoInterfaceRepository;
    private final PojoElementRepository pojoElementRepository;
    private final PojoReferenceRepository pojoReferenceRepository;

    @Autowired
    public ClassHandling(final PojoClassRepository pojoClassRepository, final PojoInterfaceRepository  pojoInterfaceRepository,
                         final PojoElementRepository  pojoElementRepository, final PojoReferenceRepository pojoReferenceRepository){
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoElementRepository = pojoElementRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    private boolean checkOpcode(int mask, int opcpde){
        return (mask & opcpde) != 0;

    }

    private String parsePackageName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");
        if (packageEnd == -1){
            // No package
            return "";
        }else {
            return completeName.substring(0, packageEnd);
        }

    }

    private String parseClassName(String completeName){
        int packageEnd = completeName.lastIndexOf("/");

        if (packageEnd == -1){
            // No package
            return completeName;
        }else {
            return completeName.substring(packageEnd + 1);
        }
    }

    private boolean isInterface(ClassNode classNode){
        return checkOpcode(classNode.access, Opcodes.ACC_INTERFACE);
        //return  (classNode.access & Opcodes.ACC_INTERFACE) != 0;
    }

    public void handleClassNode(ClassNode classNode) throws ClassHandlingException{
        if (isInterface(classNode)) {
            buildPojoInterface(classNode.name);
        } else {
            buildPojoClass(classNode);
        }

    }


    private void buildPojoClass(ClassNode classNode) throws ClassHandlingException{
        String className = parseClassName(classNode.name);
        String classPackage = parsePackageName(classNode.name);

        PojoClass pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName(className, classPackage);
        if (pojoClass == null){
            pojoClass = getEmptyClassHull(className, classPackage);
        }

        if (!pojoClass.getEmptyHull()){
            throw new ClassHandlingException("Item " + className + " in package \"" + classPackage + "\" is already in the database");
        }

        ExtendsRs extendsRs = buildExtendsRs(classNode);
        List<ImplementsRs> implementsRsSet = buildImplementsRs(classNode);
        List<AttributeRs> attributeRsList = buildAttributeRs(classNode);

        pojoClass.setExtendsClass(extendsRs);
        pojoClass.setImplementsInterfaces(implementsRsSet);
        pojoClass.setHasAttributes(attributeRsList);
        pojoClass.setEmptyHull(false);

        pojoClassRepository.save(pojoClass);
    }

    private PojoInterface buildPojoInterface(String nodeName){
        String interfaceName = parseClassName(nodeName);
        String interfacePackage = parsePackageName(nodeName);
        PojoInterface pojoInterface = pojoInterfaceRepository.getPojoInterfaceByNameAndPackageName(interfaceName, interfacePackage);
        if (pojoInterface == null){
            PojoReference pojoReference = pojoReferenceRepository.getPojoReferenceByNameAndPackageName(interfaceName, interfacePackage);
            if (pojoReference != null) {
                pojoInterface = pojoInterfaceRepository.changeReferenceToInterfaceById(pojoReference.getId());
            } else {
                pojoInterface = PojoInterface.builder().name(interfaceName).packageName(interfacePackage).build();
            }
            pojoInterfaceRepository.save(pojoInterface);
            return pojoInterface;
        }
        return null;
    }

    private ExtendsRs buildExtendsRs(ClassNode classNode){
        String superClassName = parseClassName(classNode.superName);
        String superClassPackage = parsePackageName(classNode.superName);

        // Avoid Object Notes as Parent class
        if (!(superClassName.equals("Object") && superClassPackage.equals("java/lang"))){
            // search for super class, if not existing create empty hull
            PojoClass superClass = pojoClassRepository.getPojoClassByNameAndPackageName(superClassName, superClassPackage);
            if (superClass == null) {
                superClass = getEmptyClassHull(superClassName, superClassPackage);
            }
            return ExtendsRs.builder().pojoClass(superClass).build();
        }
        return null;
    }

    private List<ImplementsRs> buildImplementsRs(@NonNull ClassNode classNode){
        List<ImplementsRs> result = new ArrayList<>();

        for (Object interf: classNode.interfaces) {
            if (interf instanceof String) {
                String interfaceString = (String) interf;
                PojoInterface pojoInterface = buildPojoInterface(interfaceString);
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

                    String access = "private";
                    if (checkOpcode(a.access, Opcodes.ACC_PROTECTED))
                        access = "protected";
                    if (checkOpcode(a.access, Opcodes.ACC_PUBLIC))
                        access = "public";

                    String attributeType = "";
                    boolean isReference = false;

                    switch (a.desc.charAt(0)) {
                        case 'L':
                            attributeType = a.desc.substring(1, a.desc.length()-1);
                            isReference = true;
                            break;
                        case 'Z':
                            attributeType = Boolean.class.getName();
                            break;
                        case 'B':
                            attributeType = Byte.class.getName();
                            break;
                        case 'S':
                            attributeType = Short.class.getName();
                            break;
                        case 'I':
                            attributeType = Integer.class.getName();
                            break;
                        case 'J':
                            attributeType = Long.class.getName();
                            break;
                        case 'F':
                            attributeType = Float.class.getName();
                            break;
                        case 'D':
                            attributeType = Double.class.getName();
                            break;
                        case 'C':
                            attributeType = Character.class.getName();
                            break;
                        default:
                            LOGGER.info("Unsupported attribute prefix: "+ a.desc.charAt(0));
                    };
                    if (!attributeType.equals("")) {
                        attributeType = attributeType.replace(".", "/");

                        String attributeName = parseClassName(attributeType);
                        String attributePackage = parsePackageName(attributeType);
                        PojoElement relatedClass = pojoElementRepository.getPojoElementByNameAndPackageName(attributeName, attributePackage);
                        if (relatedClass == null){
                            if (!isReference) {
                                relatedClass = getEmptyClassHull(attributeName, attributePackage);
                            } else {
                                relatedClass = createPojoReference(attributeName, attributePackage);
                            }
                        }
                        result.add(AttributeRs.builder().visibility(access).name(a.name).pojoElement(relatedClass).build());
                    }
                }
            }
        }
        return result;
    }


    private PojoClass getEmptyClassHull(String className, String packageName){
        PojoClass emptyHull;
        PojoReference pojoReference = pojoReferenceRepository.getPojoReferenceByNameAndPackageName(className, packageName);
        if (pojoReference != null) {
            emptyHull = pojoClassRepository.changeReferenceToClassById(pojoReference.getId());
            emptyHull.setEmptyHull(true);
        } else {
            emptyHull = PojoClass.builder().name(className).packageName(packageName).emptyHull(true).build();
        }
        pojoClassRepository.save(emptyHull);
        return emptyHull;
    }

    private PojoReference createPojoReference(String className, String packageName){
        PojoReference pojoReference = PojoReference.builder().name(className).packageName(packageName).build();
        pojoReferenceRepository.save(pojoReference);
        return pojoReference;
    }
}
