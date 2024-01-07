package com.example.demo.domain.imbeddable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
@Data
public class SkillInfo {

    @JsonProperty("attack")
    private String power;

    @JsonProperty("mental")
    private Integer mentalConsume;

    @JsonProperty("type")
    private String atkType;

    @JsonProperty("prop")
    private String resource;

    @JsonProperty("power")
    private Integer skillPower;

    @JsonProperty("coinpower")
    private Integer coinPower;

    @JsonProperty("weight")
    private Integer atkWeight;

    @Embedded
    private CoinEffectInfo coinHitInfo;
}
