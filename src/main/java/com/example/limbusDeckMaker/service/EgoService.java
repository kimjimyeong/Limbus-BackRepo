package com.example.limbusDeckMaker.service;

import static com.example.limbusDeckMaker.service.mapper.EgoBuildInfoMapper.countEgoResources;
import static com.example.limbusDeckMaker.service.mapper.EgoListInfoMapper.*;

import com.example.limbusDeckMaker.domain.Ego;
import com.example.limbusDeckMaker.dto.response.*;
import com.example.limbusDeckMaker.repository.EgoRepository;
import com.example.limbusDeckMaker.repository.specification.EgoSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Ego와 관련된 로직을 처리하는 서비스 클래스
 */
@Slf4j
@Service
public class EgoService {

    private final EgoRepository egoRepository;

    public EgoService(EgoRepository egoRepository) {
        this.egoRepository = egoRepository;
    }

    /**
     * 특정 ID에 해당하는 Ego의 상세 정보를 조회
     */
    public Optional<EgoDetailInfoDto> getSpecificEgo(Long id) {
        return egoRepository.findById(id)
            .map(EgoDetailInfoDto::toDto);
    }

    /**
     * 다양한 검색 조건을 통해 Ego 목록을 조회
     */
    public List<EgoListInfoDto> getEgoByCriteria(List<String> sinnerNames, List<Integer> seasons, List<String> grades,
        List<String> keywords, List<String> resources, List<String> types, Integer minWeight,
        Integer maxWeight) {

        Specification<Ego> spec = Specification.where(null);
        if (sinnerNames != null) {
            spec = spec.and(EgoSpecification.hasSinnerNames(sinnerNames));
        }
        if (seasons != null) {
            spec = spec.and(EgoSpecification.hasSeasons(seasons));
        }
        if (grades != null) {
            spec = spec.and(EgoSpecification.hasGrades(grades));
        }

        List<Ego> egos = egoRepository.findAll(spec);

        // EgoContext를 활용하여 추가적인 정보를 계산
        List<EgoContext> egoContexts = egos.stream()
            .map(ego -> new EgoContext(ego,
                findUseResources(ego),
                findUseTypes(ego),
                findMinWeight(ego),
                findMaxWeight(ego)))
            .toList();

        List<EgoListInfoDto> results = egoContexts.stream()
            .filter(context -> matchesKeywords(context, keywords))
            .filter(context -> matchesResources(context, resources))
            .filter(context -> matchesTypes(context, types))
            .filter(context -> matchesWeight(context, minWeight, maxWeight))
            .map(context -> EgoListInfoDto.toDto(context.getEgo(), context))
            .collect(Collectors.toList());

        return results;
    }

    /**
     * 선택된 Ego들의 빌드 정보를 조회
     */
    public Optional<EgoBuildInfoDto> getEgoBuildInfo(List<Long> egoIds){
        List<Ego> egos = egoRepository.findByIdIn(egoIds);

        if(egos.isEmpty()){
            return Optional.empty();
        }

        String characterName = egos.get(0).getSinner().getName();
        List<EgoBuildInfoDto.Ego> dtoEgos = new ArrayList<>();

        for (int i = 0; i < egos.size(); i++) {
            com.example.limbusDeckMaker.domain.Ego domainEgo = egos.get(i);
            List<Integer> resourceCounts = countEgoResources(domainEgo);
            List<String> resourceCountsStr = resourceCounts.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            dtoEgos.add(new EgoBuildInfoDto.Ego(i + 1, resourceCountsStr));
        }

        return Optional.of(EgoBuildInfoDto.toDto(characterName, dtoEgos));
    }

    /**
     * EgoContext를 활용하여 keyword 필터링을 수행
     */
    private boolean matchesKeywords(EgoContext context, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return true;
        }
        return keywords.stream().allMatch(keyword -> context.getEgo().getKeyword().contains(keyword));
    }

    private boolean matchesResources(EgoContext context, List<String> resources) {
        if (resources == null || resources.isEmpty()) {
            return true;
        }
        return resources.stream().allMatch(resource -> context.getResources().contains(resource));
    }

    private boolean matchesTypes(EgoContext context, List<String> types) {
        if (types == null || types.isEmpty()) {
            return true;
        }
        return types.stream().allMatch(type -> context.getTypes().contains(type));
    }

    private boolean matchesWeight(EgoContext context, Integer minWeight, Integer maxWeight) {
        boolean matchesMin = minWeight == null || context.getMinWeight() >= minWeight;
        boolean matchesMax = maxWeight == null || context.getMaxWeight() <= maxWeight;
        return matchesMin && matchesMax;
    }

}
