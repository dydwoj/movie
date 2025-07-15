package com.example.movie.dto;

import java.time.LocalDateTime;

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
public class ReviewDTO {

    private Long rno;
    private int grade;
    private String text;

    // member 정보
    private Long mid;
    private String email;
    private String nickName;

    // movie 정보
    private Long mno;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
