package com.example.limbusDeckMaker.controller;

import com.example.limbusDeckMaker.dto.response.EgoDetailInfoDto;
import com.example.limbusDeckMaker.dto.response.EgoListInfoDto;
import com.example.limbusDeckMaker.dto.response.IdentityDetailInfoDto;
import com.example.limbusDeckMaker.dto.response.IdentityListInfoDto;
import com.example.limbusDeckMaker.exception.NoEgoFoundException;
import com.example.limbusDeckMaker.exception.NoIdentityFoundException;
import com.example.limbusDeckMaker.service.EgoService;
import com.example.limbusDeckMaker.service.IdentityService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final EgoService egoService;
    private final IdentityService identityService;

    /**
     * EgoService와 IdentityService를 주입받는 생성자
     *
     * @param egoService      에고 관련 서비스
     * @param identityService 인격 관련 서비스
     */
    public DictionaryController(EgoService egoService, IdentityService identityService) {
        this.egoService = egoService;
        this.identityService = identityService;
    }

    /**
     * 특정 ID에 해당하는 에고의 상세 정보를 조회
     *
     * @param id 에고의 ID
     * @return 에고의 상세 정보
     */
    @GetMapping("/ego/{id}")
    public ResponseEntity<EgoDetailInfoDto> searchEgoById(@PathVariable("id") Long id) {
        return egoService.getSpecificEgo(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoEgoFoundException("해당 ID를 가진 에고가 없습니다."));
    }

    /**
     * 특정 ID에 해당하는 인격의 상세 정보를 조회
     *
     * @param id 인격의 ID
     * @return 인격의 상세 정보
     */
    @GetMapping("/identity/{id}")
    public ResponseEntity<IdentityDetailInfoDto> searchIdentityById(@PathVariable("id") Long id) {
        return identityService.getSpecificIdentity(id)
            .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoIdentityFoundException("해당 ID를 가진 인격이 없습니다."));
    }

    /**
     * 다양한 검색 조건을 통해 에고 목록을 조회
     *
     * @param names     죄인 이름 목록 (옵션)
     * @param seasons   시즌 목록 (옵션)
     * @param grades    등급 목록 (옵션)
     * @param keywords  키워드 목록 (옵션)
     * @param resources 자원 목록 (옵션)
     * @param types     타입 목록 (옵션)
     * @param minWeight 최소 무게 (옵션)
     * @param maxWeight 최대 무게 (옵션)
     * @return 에고 목록
     */
    @GetMapping("/ego")
    public ResponseEntity<List<EgoListInfoDto>> searchEgosByCriteria(
        @RequestParam(value = "sinner", required = false) List<String> names,
        @RequestParam(value = "season", required = false) List<Integer> seasons,
        @RequestParam(value = "grade", required = false) List<String> grades,
        @RequestParam(value = "keyword", required = false) List<String> keywords,
        @RequestParam(value = "resources", required = false) List<String> resources,
        @RequestParam(value = "types", required = false) List<String> types,
        @RequestParam(value = "minWeight", required = false) Integer minWeight,
        @RequestParam(value = "maxWeight", required = false) Integer maxWeight) {

        List<EgoListInfoDto> results = egoService.getEgoByCriteria(names, seasons, grades, keywords,
            resources, types, minWeight, maxWeight);

        if (results.isEmpty()) {
            throw new NoEgoFoundException("해당하는 에고가 없습니다.");
        }
        return ResponseEntity.ok(results);
    }

    /**
     * 다양한 검색 조건을 통해 인격 목록을 조회
     *
     * @param names       죄인 이름 목록 (옵션)
     * @param seasons     시즌 목록 (옵션)
     * @param grades      등급 목록 (옵션)
     * @param affiliations 소속 목록 (옵션)
     * @param keywords    키워드 목록 (옵션)
     * @param resources   자원 목록 (옵션)
     * @param types       타입 목록 (옵션)
     * @param minWeight   최소 무게 (옵션)
     * @param maxWeight   최대 무게 (옵션)
     * @param minSpeed    최소 속도 (옵션)
     * @param maxSpeed    최대 속도 (옵션)
     * @return 인격 목록
     */
    @GetMapping("/identity")
    public ResponseEntity<List<IdentityListInfoDto>> searchIdentitiesByCriteria(
        @RequestParam(value = "sinner", required = false) List<String> names,
        @RequestParam(value = "season", required = false) List<Integer> seasons,
        @RequestParam(value = "grade", required = false) List<Integer> grades,
        @RequestParam(value = "affiliation", required = false) List<String> affiliations,
        @RequestParam(value = "keyword", required = false) List<String> keywords,
        @RequestParam(value = "resources", required = false) List<String> resources,
        @RequestParam(value = "types", required = false) List<String> types,
        @RequestParam(value = "minWeight", required = false) Integer minWeight,
        @RequestParam(value = "maxWeight", required = false) Integer maxWeight,
        @RequestParam(value = "minSpeed", required = false) Integer minSpeed,
        @RequestParam(value = "maxSpeed", required = false) Integer maxSpeed
    ) {

        List<IdentityListInfoDto> results = identityService.getIdentityByCriteria(names, seasons, grades,
            affiliations, keywords, resources, types, minWeight, maxWeight, minSpeed, maxSpeed);

        if (results.isEmpty()) {
            throw new NoIdentityFoundException("해당하는 인격이 없습니다.");
        }
        return ResponseEntity.ok(results);
    }

}