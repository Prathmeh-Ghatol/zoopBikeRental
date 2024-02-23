package com.zoopbike.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenricPage <T> {
    private List<T> content;

    private Integer pageNo;

    private Integer pageSize;

    private Boolean isLastPage;

    private Integer totalPage;

    private Long totalElement;

}
