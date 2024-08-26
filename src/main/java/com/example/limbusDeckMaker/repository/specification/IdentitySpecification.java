package com.example.limbusDeckMaker.repository.specification;

import com.example.limbusDeckMaker.domain.Identity;
import com.example.limbusDeckMaker.domain.Sinner;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class IdentitySpecification {

    public static Specification<Identity> hasSinnerNames(List<String> sinnerNames) {
        return (root, query, cb) -> {
            if (sinnerNames == null || sinnerNames.isEmpty()) {
                return null;
            }
            Join<Identity, Sinner> sinnerJoin = root.join("sinner", JoinType.INNER);
            return sinnerJoin.get("name").in(sinnerNames.stream().map(String::toLowerCase).collect(Collectors.toList()));
        };
    }

    public static Specification<Identity> hasSeasons(List<Integer> seasons) {
        return (Root<Identity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (seasons == null || seasons.isEmpty()) {
                return null;
            }
            return root.get("season").in(seasons);
        };
    }

    public static Specification<Identity> hasGrades(List<Integer> grades) {
        return (Root<Identity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (grades == null || grades.isEmpty()) {
                return null;
            }
            return root.get("grade").in(grades);
        };
    }

    public static Specification<Identity> hasAffiliations(List<String> affiliations) {
        return (Root<Identity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (affiliations == null || affiliations.isEmpty()) {
                return null;
            }
            return root.get("affiliation").in(affiliations);
        };
    }
}
