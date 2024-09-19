package com.example.limbusDeckMaker.dto.response;

import com.example.limbusDeckMaker.domain.Ego;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Ego 목록 정보를 담는 DTO 클래스
 * Entity를 직접 반환하지 않고 DTO로 변환하여 사용하는 이유는 캡슐화와 보안 때문
 * Entity의 내부 구조를 외부에 노출시키지 않고 필요한 정보만 전달하기 위해 toDto 메서드 사용
 */
@Getter
@Setter
@Builder
public class EgoListInfoDto {

    private Long id;
    private String name;
    private String character;
    private Integer season;
    private String grade;
    private String image;
    private String zoomImage;

    private List<String> keyword;

    private List<String> resources;
    private List<String> types;
    private Integer minWeight;
    private Integer maxWeight;

    public static EgoListInfoDto toDto(Ego ego, EgoContext egoContext){
        return EgoListInfoDto.builder()
            .id(ego.getId())
            .character(ego.getSinner().getName())
            .name(ego.getName())
            .season(ego.getSeason())
            .image(ego.getImage())
            .zoomImage(ego.getZoomImage())
            .grade(ego.getGrade())
            .keyword(ego.getKeyword())
            .resources(egoContext.getResources())
            .types(egoContext.getTypes())
            .minWeight(egoContext.getMinWeight())
            .maxWeight(egoContext.getMaxWeight())
            .build();
    }

}
