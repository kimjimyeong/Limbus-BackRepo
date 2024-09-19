package com.example.limbusDeckMaker.repository;

import com.example.limbusDeckMaker.domain.Ego;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Ego 엔티티를 위한 Repository 인터페이스
 */
public interface EgoRepository extends JpaRepository<Ego, Long>, JpaSpecificationExecutor<Ego> {

    @Override
    Optional<Ego> findById(Long aLong);

    /**
     * Sinner 이름과 Ego 이름으로 Ego를 조회
     */
    Optional<Ego> findBySinner_NameAndName(String sinnerName, String egoName);

    /**
     * ID 목록에 해당하는 Ego들을 조회
     */
    List<Ego> findByIdIn(List<Long> ids);
}
