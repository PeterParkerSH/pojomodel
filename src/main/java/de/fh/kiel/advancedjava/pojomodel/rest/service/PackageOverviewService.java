package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiOverviewElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageOverviewService {

    final PojoElementRepository pojoElementRepository;

    public PackageOverviewService(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }

    @Transactional
    public List<ApiOverviewElement> getOverview(String packageName) {
        List<PojoElement> elements;
        if (packageName.isEmpty()){
            elements = pojoElementRepository.findAll();
        }else {
            elements = pojoElementRepository.getPojoElementsWithPackageName(packageName);
            elements.addAll(pojoElementRepository.getPojoElementStartsWithByPackageName(packageName + '/'));
        }

        List<ApiOverviewElement> result = elements.stream()
                .map(ApiOverviewElement::new)
                .collect(Collectors.toList());
        return result.stream()
                    .sorted(Comparator
                            .comparing(ApiOverviewElement::getPackageName)

                            .thenComparing(ApiOverviewElement::getType)
                            .thenComparing(ApiOverviewElement::getClassName))
                            .collect(Collectors.toList());
    }
}
