package com.example.limbusDeckMaker.repository.specification;

import com.example.limbusDeckMaker.domain.Ego;
import com.example.limbusDeckMaker.domain.Sinner;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ego 엔티티에 대한 동적 쿼리를 생성하는 클래스
 */
public class EgoSpecification {

    /**
     * Sinner 이름 목록에 해당하는 Ego를 조회
     */
    public static Specification<Ego> hasSinnerNames(List<String> sinnerNames) {
        return (root, query, cb) -> {
            if (sinnerNames == null || sinnerNames.isEmpty()) {
                return null;
            }
            Join<Ego, Sinner> sinnerJoin = root.join("sinner", JoinType.INNER);
            return sinnerJoin.get("name").in(sinnerNames.stream().map(String::toLowerCase).collect(Collectors.toList()));
        };
    }

    /**
     * Season 목록에 해당하는 Ego를 조회
     */
    public static Specification<Ego> hasSeasons(List<Integer> seasons) {
            return (Root<Ego> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (seasons == null || seasons.isEmpty()) {
                return null;
            }
            return root.get("season").in(seasons);
        };
    }

    /**
     * Grade 목록에 해당하는 Ego를 조회
     */
    public static Specification<Ego> hasGrades(List<String> grades) {
        return (Root<Ego> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (grades == null || grades.isEmpty()) {
                return null;
            }
            return root.get("grade").in(grades);
        };
    }
}
