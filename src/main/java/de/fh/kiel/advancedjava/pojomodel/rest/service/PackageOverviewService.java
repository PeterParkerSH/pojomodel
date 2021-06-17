package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPackageOverviewElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PackageOverviewService provides a list of all Pojos in a given package, including subpackages of the same package
 */
@Service
public class PackageOverviewService {

    final PojoElementRepository pojoElementRepository;

    public PackageOverviewService(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }

    @Transactional
    public List<ApiPackageOverviewElement> packageOverview(String packageName) {
        List<PojoElement> elements;
        if (packageName.isEmpty()){
            elements = pojoElementRepository.findAll();
        }else {
            elements = pojoElementRepository.getPojoElementsWithPackageName(packageName);
            elements.addAll(pojoElementRepository.getPojoElementStartsWithByPackageName(packageName + '/'));
        }

        return elements.stream()
                .map(ApiPackageOverviewElement::new).sorted(Comparator
                        .comparing(ApiPackageOverviewElement::getPackageName)
                        .thenComparing(ApiPackageOverviewElement::getType)
                        .thenComparing(ApiPackageOverviewElement::getClassName)).collect(Collectors.toList());
    }
}
