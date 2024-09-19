package com.example.limbusDeckMaker.service;

import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.findMaxWeight;
import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.findMinWeight;
import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.findUseResources;
import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.findUseTypes;
import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.parseMaxSpeed;
import static com.example.limbusDeckMaker.service.mapper.IdentityListInfoMapper.parseMinSpeed;

import com.example.limbusDeckMaker.domain.Identity;
import com.example.limbusDeckMaker.dto.response.IdentityBuildInfoDto;
import com.example.limbusDeckMaker.dto.response.IdentityContext;
import com.example.limbusDeckMaker.dto.response.IdentityDetailInfoDto;
import com.example.limbusDeckMaker.dto.response.IdentityListInfoDto;
import com.example.limbusDeckMaker.repository.IdentityRepository;
import com.example.limbusDeckMaker.repository.specification.IdentitySpecification;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IdentityService {

    private final IdentityRepository identityRepository;

    public IdentityService(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    public Optional<IdentityDetailInfoDto> getSpecificIdentity(Long id) {
        return identityRepository.findById(id)
            .map(IdentityDetailInfoDto::toDto);
    }

    public List<IdentityListInfoDto> getIdentityByCriteria(List<String> sinnerNames, List<Integer> seasons,
        List<Integer> grades,
        List<String> affiliations, List<String> keywords, List<String> resources, List<String> types,
        Integer minWeight, Integer maxWeight, Integer minSpeed, Integer maxSpeed) {


        Specification<Identity> spec = Specification.where(null);

        if (sinnerNames != null) {
            spec = spec.and(IdentitySpecification.hasSinnerNames(sinnerNames));
        }
        if (seasons != null) {
            spec = spec.and(IdentitySpecification.hasSeasons(seasons));
        }
        if (grades != null) {
            spec = spec.and(IdentitySpecification.hasGrades(grades));
        }
        if (affiliations != null) {
            log.info("log -> : {}", affiliations.get(0));
            log.info("TEST");
            spec = spec.and(IdentitySpecification.hasAffiliations(affiliations));
        }

        List<Identity> identities = identityRepository.findAll(spec);
        List<IdentityContext> identityContexts = identities.stream()
            .map(identity -> new IdentityContext(identity,
                findUseResources(identity),
                findUseTypes(identity),
                findMinWeight(identity),
                findMaxWeight(identity),
                parseMinSpeed(identity.getStatus().getSpeed()),
                parseMaxSpeed(identity.getStatus().getSpeed())))
            .toList();

        return identityContexts.stream()
            .filter(context -> matchesKeywords(context, keywords))
            .filter(context -> matchesResources(context, resources))
            .filter(context -> matchesTypes(context, types))
            .filter(context -> matchesWeight(context, minWeight, maxWeight))
            .filter(context -> matchesSpeed(context, minSpeed, maxSpeed))
            .map(context -> IdentityListInfoDto.toDto(context.getIdentity(), context))
            .collect(Collectors.toList());
    }

    public Optional<IdentityBuildInfoDto> getIdentityBuildInfo(Long identityId){
        return identityRepository.findById(identityId)
                .map(IdentityBuildInfoDto::toDto);
    }

    private boolean matchesKeywords(IdentityContext context, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return true;
        }
        return keywords.stream()
            .allMatch(keyword -> context.getIdentity().getKeyword().contains(keyword));
    }

    private boolean matchesResources(IdentityContext context, List<String> resources) {
        if (resources == null || resources.isEmpty()) {
            return true;
        }
        return resources.stream().allMatch(resource -> context.getResources().contains(resource));
    }

    private boolean matchesTypes(IdentityContext context, List<String> types) {
        if (types == null || types.isEmpty()) {
            return true;
        }
        return types.stream().allMatch(type -> context.getTypes().contains(type));
    }

    private boolean matchesWeight(IdentityContext context, Integer minWeight, Integer maxWeight) {
        boolean matchesMin = minWeight == null || context.getMinWeight() >= minWeight;
        boolean matchesMax = maxWeight == null || context.getMaxWeight() <= maxWeight;
        return matchesMin && matchesMax;
    }

    private boolean matchesSpeed(IdentityContext context, Integer minSpeed, Integer maxSpeed) {
        boolean matchesMin = minSpeed == null || context.getMinSpeed() >= minSpeed;
        boolean matchesMax = maxSpeed == null || context.getMaxSpeed() <= maxSpeed;

        return matchesMin && matchesMax;
    }

}
