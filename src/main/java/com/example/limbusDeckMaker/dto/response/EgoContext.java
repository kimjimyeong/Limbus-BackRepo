package com.example.limbusDeckMaker.dto.response;

import com.example.limbusDeckMaker.domain.Ego;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ego의 추가적인 정보를 담는 컨텍스트 클래스
 * 필터링과 매핑 과정에서 효율성을 높이기 위해 사용
 */
@Getter
@Setter
@AllArgsConstructor
public class EgoContext {
    private final Ego ego;
    private final List<String> resources;
    private final List<String> types;
    private final Integer minWeight;
    private final Integer maxWeight;

}