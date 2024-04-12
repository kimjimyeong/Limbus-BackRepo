package com.example.limbusDeckMaker.dto.sync3;

import com.example.limbusDeckMaker.domain.Ego;
import com.example.limbusDeckMaker.domain.EgoSkill;
import com.example.limbusDeckMaker.imbeddable.ego.Skill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link EgoSkill}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sync3EgoSkillDto implements Serializable {

    @JsonProperty("name")
    String name;

    String image;

    @JsonProperty("resistance")
    List<String> resistance;

    @Embedded
    @JsonProperty("sync3")
    Skill skill;

    Integer construeLevel = 3;

    Ego ego;
}