package com.example.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class MovieDTO {

    private Long mno;
    private String title;

    @Builder.Default
    private List<MovieImageDTO> movieImages = new ArrayList<>();

    // 평점
    private double avg;
    // 리뷰수
    private Long reviewCnt;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
