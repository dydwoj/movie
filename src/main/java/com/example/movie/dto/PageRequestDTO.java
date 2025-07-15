package com.example.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class PageRequestDTO {

    // 리스트 페이지 나누기
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    // 검색 기능 만들기
    @Builder.Default
    private String type = "";
    @Builder.Default
    private String keyword = "";

}
