package com.example.demo.dto.sync3;

import com.example.demo.domain.Identity;
import com.example.demo.domain.IdentityDefSkill;
import com.example.demo.imbeddable.identity.DefSkill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link IdentityDefSkill}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sync3IdentityDefSkillDto implements Serializable {

    @JsonProperty("name")
    String identityName;

    @JsonProperty("sync3")
    @Embedded
    DefSkill defSkill;

    Integer level = 3;

    Identity identity;
}