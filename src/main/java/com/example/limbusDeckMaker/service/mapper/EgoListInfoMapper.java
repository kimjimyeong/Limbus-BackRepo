package com.example.limbusDeckMaker.service.mapper;

import com.example.limbusDeckMaker.domain.Ego;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Ego 엔티티를 기반으로 추가 정보를 추출하는 매퍼 클래스
 */
public class EgoListInfoMapper {

    public static List<String> findUseResources(Ego ego) {
        List<String> resourceNames = Arrays.asList("분노", "색욕", "나태", "탐식", "우울", "오만", "질투");
        List<Integer> resourceCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<String> usingResources = new ArrayList<>();

        // 스킬의 비용을 합산하여 사용되는 자원을 계산
        Stream.concat(
                ego.getEgoSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .map(s -> s.getSkill().getCost()),
                ego.getEgoCorSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .map(s -> s.getCorSkill().getCost())
            )
            .forEach(costList -> {
                for (int i = 0; i < costList.size(); i++) {
                    int value = costList.get(i);
                    resourceCounts.set(i, resourceCounts.get(i) + value);
                }
            });

        // 사용된 자원의 이름을 추출하여 정렬 후 반환
        for (int i = 0; i < resourceCounts.size(); i++) {
            if (resourceCounts.get(i) > 0 && !usingResources.contains(resourceNames.get(i))) {
                usingResources.add(resourceNames.get(i));
            }
        }

        usingResources.sort(Comparator.naturalOrder());

        return usingResources;
    }

    /**
     * Ego에서 사용하는 타입 목록을 찾아 반환
     */
    public static List<String> findUseTypes(Ego ego){
        Set<String> uniqueTypes = new HashSet<>();

        // 스킬의 공격 타입을 추출하여 중복 없이 수집
        Stream.concat(
                ego.getEgoSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .map(egoSkill -> egoSkill.getSkill().getSkillInfo().getAtkType()),
                ego.getEgoCorSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .map(corrosionSkill -> corrosionSkill.getCorSkill().getCorrosionSkillInfo().getAtkType())
            )
            .forEach(uniqueTypes::add);

        List<String> usingTypes = new ArrayList<>(uniqueTypes);
        usingTypes.sort(Comparator.naturalOrder());
        return usingTypes;
    }

    /**
     * Ego에서 사용되는 공격 무게의 최소값을 찾아 반환
     */
    public static Integer findMinWeight(Ego ego) {
        return Stream.of(
                ego.getEgoSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .mapToInt(s -> s.getSkill().getSkillInfo().getAtkWeight()),
                ego.getEgoCorSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .mapToInt(s -> s.getCorSkill().getCorrosionSkillInfo().getAtkWeight())
            )
            .flatMapToInt(s -> s)
            .min()
            .orElse(Integer.MAX_VALUE);
    }

    /**
     * Ego에서 사용되는 공격 무게의 최대값을 찾아 반환
     */
    public static Integer findMaxWeight(Ego ego) {
        return Stream.of(
                ego.getEgoSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .mapToInt(s -> s.getSkill().getSkillInfo().getAtkWeight()),
                ego.getEgoCorSkills().stream().filter(s -> s.getConstrueLevel() == 4)
                    .mapToInt(s -> s.getCorSkill().getCorrosionSkillInfo().getAtkWeight())
            )
            .flatMapToInt(s -> s)
            .max()
            .orElse(Integer.MIN_VALUE);
    }

}
