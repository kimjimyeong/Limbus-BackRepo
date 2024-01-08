package com.example.demo.domain.identity.imbeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Embeddable
public class Passive {

    @JsonProperty("pass1")
    private PassiveInfo passiveInfo;

}
