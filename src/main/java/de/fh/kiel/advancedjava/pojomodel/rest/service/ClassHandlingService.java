package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.rest.exceptions.ClassHandlingException;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.*;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassHandlingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHandlingService.class);

    private final PojoClassRepository pojoClassRepository;
    private final PojoInterfaceRepository  pojoInterfaceRepository;
    private final PojoElementRepository pojoElementRepository;
    private final PojoReferenceRepository pojoReferenceRepository;

    public ClassHandlingService(final PojoClassRepository pojoClassRepository, final PojoInterfaceRepository  pojoInterfaceRepository,
                                final PojoElementRepository  pojoElementRepository, final PojoReferenceRepository pojoReferenceRepository){
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoElementRepository = pojoElementRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    @Transactional
    public void handleClassNodes(List<ClassNode> classNodes) throws ClassHandlingException {
        // Check if elements already exist
        for (ClassNode classNode: classNodes) {
            if (checkClassNodeAlreadyExists(classNode)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Element " + classNode.name + " already exists in database");
            }
        }
        for (ClassNode classNode: classNodes) {
            handleClassNode(classNode);
        }
    }

    private boolean checkClassNodeAlreadyExists(ClassNode classNode){
        String className = parseClassName(classNode.name);
        String classPackage = parsePackageName(classNode.name);
        PojoElement pojoElement= pojoElementRepository.getPojoElementByNameAndPackageName(className, classPackage);
        if (pojoElement == null){
            return false;
        } else {
            // The element exists if it is a PojoClass or PojoElement
            return (!(pojoElement instanceof PojoReference));
        }

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
    }


    private void handleClassNode(ClassNode classNode) throws ClassHandlingException{
        if (isInterface(classNode)) {
            buildPojoInterface(classNode.name);
        } else {
            buildPojoClass(classNode);
        }

    }

    private void buildPojoClass(ClassNode classNode) throws ClassHandlingException{
        String className = parseClassName(classNode.name);
        String classPackage = parsePackageName(classNode.name);

        PojoElement pojoElement = getOrCreatePojoElement(className, classPackage);
        if (!(pojoElement instanceof PojoReference)){
            throw new ClassHandlingException("Item " + className + " in package '" + classPackage + "' is already in the database");
        }

        PojoClass pojoClass = pojoClassRepository.changeReferenceToClassById(pojoElement.getId());

        ExtendsRs extendsRs = buildExtendsRs(classNode);
        List<ImplementsRs> implementsRsSet = buildImplementsRs(classNode);
        List<AttributeRs> attributeRsList = buildAttributeRs(classNode);

        pojoClass.setExtendsClass(extendsRs);
        pojoClass.setImplementsInterfaces(implementsRsSet);
        pojoClass.setHasAttributes(attributeRsList);

        pojoClassRepository.save(pojoClass);
    }

    private void buildPojoInterface(String nodeName) throws ClassHandlingException{
        String interfaceName = parseClassName(nodeName);
        String interfacePackage = parsePackageName(nodeName);

        PojoElement pojoElement = getOrCreatePojoElement(interfaceName, interfacePackage);
        if (!(pojoElement instanceof PojoReference)){
            throw new ClassHandlingException("Item " + interfaceName + " in package '" + interfacePackage + "' is already in the database");
        }
        PojoInterface pojoInterface = pojoInterfaceRepository.changeReferenceToInterfaceById(pojoElement.getId());
        pojoInterfaceRepository.save(pojoInterface);
    }

    private ExtendsRs buildExtendsRs(ClassNode classNode){
        String superClassName = parseClassName(classNode.superName);
        String superClassPackage = parsePackageName(classNode.superName);

        // Avoid Object Notes as Parent class
        if (!(superClassName.equals("Object") && superClassPackage.equals("java/lang"))){
            // search for super class, if not existing create empty hull
            PojoElement pojoElement = getOrCreatePojoElement(superClassName, superClassPackage);
            return ExtendsRs.builder().pojoClass(pojoElement).build();
        }
        return null;
    }

    private List<ImplementsRs> buildImplementsRs(ClassNode classNode) {
        List<ImplementsRs> result = new ArrayList<>();

        for (String interfaceString: classNode.interfaces) {
            String interfaceName = parseClassName(interfaceString);
            String interfacePackage = parsePackageName(interfaceString);
            PojoElement pojoElement = getOrCreatePojoElement(interfaceName, interfacePackage);
            result.add(ImplementsRs.builder().pojoInterface(pojoElement).build());
        }
        return result;
    }

    private String attributeTypeFromFieldNode(FieldNode a){
        return switch (a.desc.charAt(0)) {
            case 'L': yield a.desc.substring(1, a.desc.length() - 1);
            case 'Z': yield Boolean.class.getName();
            case 'B': yield Byte.class.getName();
            case 'S': yield Short.class.getName();
            case 'I': yield Integer.class.getName();
            case 'J': yield Long.class.getName();
            case 'F': yield Float.class.getName();
            case 'D': yield Double.class.getName();
            case 'C': yield Character.class.getName();
            default:
                LOGGER.info("Unsupported attribute prefix: {}", a.desc.charAt(0));
                yield "";
        };
    }

    private List<AttributeRs> buildAttributeRs(ClassNode classNode) {
        ArrayList<AttributeRs> result = new ArrayList<>();

        if (classNode.fields != null) {
            for (FieldNode field: classNode.fields){

                String access = "private";
                if (checkOpcode(field.access, Opcodes.ACC_PROTECTED))
                    access = "protected";
                if (checkOpcode(field.access, Opcodes.ACC_PUBLIC))
                    access = "public";

                String attributeType = attributeTypeFromFieldNode(field);

                if (!attributeType.isEmpty()) {
                    attributeType = attributeType.replace(".", "/");

                    String attributeName = parseClassName(attributeType);
                    String attributePackage = parsePackageName(attributeType);
                    PojoElement relatedClass = getOrCreatePojoElement(attributeName, attributePackage);
                    result.add(AttributeRs.builder().visibility(access).name(field.name).pojoElement(relatedClass).build());
                }

            }
        }
        return result;
    }


    public PojoElement getOrCreatePojoElement(String className, String packageName){
        PojoElement pojoElement = pojoElementRepository.findByPackageNameAndName(packageName, className);
        if (pojoElement == null) {
            PojoReference pojoReference = PojoReference.builder().name(className).packageName(packageName).build();
            pojoReferenceRepository.save(pojoReference);
            pojoElement = pojoReference;
        }
        return pojoElement;
    }
}
