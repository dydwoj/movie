package com.example.movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.movie.dto.ReviewDTO;
import com.example.movie.entity.Member;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;
import com.example.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDTO> getReplies(Long mno) {

        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);

        List<ReviewDTO> list = result.stream().map(review -> entityToDto(review)).collect(Collectors.toList());
        return list;

    }

    public ReviewDTO getReply(Long rno) {
        Review review = reviewRepository.findById(rno).get();
        return entityToDto(review);
    }

    public void removeReply(Long mno) {
        reviewRepository.deleteById(mno);
    }

    public ReviewDTO updateReply(ReviewDTO reviewDTO) {
        // dto => entity
        Review review = reviewRepository.findById(reviewDTO.getRno()).orElseThrow();

        // 수정
        review.changeText(reviewDTO.getText());
        review.changeGrade(reviewDTO.getGrade());
        review = reviewRepository.save(review);
        return entityToDto(review);
    }

    public Long insertReply(ReviewDTO reviewDTO) {
        Review review = dtoToEntity(reviewDTO);
        return reviewRepository.save(review).getRno();
    }

    private ReviewDTO entityToDto(Review review) {

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rno(review.getRno())
                .grade(review.getGrade())
                .text(review.getText())
                .createdDate(review.getCreatedDate())
                .updatedDate(review.getUpdatedDate())
                // 멤버 정보
                .mid(review.getMember().getMid())
                .email(review.getMember().getEmail())
                .nickName(review.getMember().getNickName())
                .build();

        return reviewDTO;
    }

    private Review dtoToEntity(ReviewDTO reviewDTO) {
        Review review = Review.builder()
                .rno(reviewDTO.getRno())
                .grade(reviewDTO.getGrade())
                .text(reviewDTO.getText())
                // 멤버 정보
                .member(Member.builder().mid(reviewDTO.getMid()).build())
                // 영화 정보
                .movie(Movie.builder().mno(reviewDTO.getMno()).build())
                .build();
        return review;
    }

}
