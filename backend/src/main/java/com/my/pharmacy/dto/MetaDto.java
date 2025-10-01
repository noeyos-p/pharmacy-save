package com.my.pharmacy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MetaDto {
    @JsonProperty("total_count")
    /* json key, total_count를 totalCount로 받음 */
    private Integer totalCount;


}
