package com.example.movie.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.movie.entity.Member;
import com.example.movie.entity.MemberRole;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.entity.Review;

@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Test
    // public void insertMovieTest() {
    // IntStream.rangeClosed(1, 200).forEach(i -> {
    // Movie movie = Movie.builder()
    // .title("Movie" + i)
    // .build();
    // movieRepository.save(movie);
    // // 임의의 이미지
    // int count = (int) (Math.random() * 5) + 1;
    // for (int j = 0; j < count; j++) {
    // MovieImage movieImage = MovieImage.builder()
    // .uuid(UUID.randomUUID().toString())
    // .ord(j)
    // .imgName("test" + j + ".jpg")
    // .movie(movie)
    // .build();
    // // movie.addImage(movieImage);
    // movieImageRepository.save(movieImage);
    // }
    // });
    // }

    // @Test
    // public void insertMemberTest() {
    // IntStream.rangeClosed(1, 50).forEach(i -> {
    // Member member = Member.builder()
    // .email("user" + i + "@gmail.com")
    // .password(passwordEncoder.encode("1111"))
    // .memberRole(MemberRole.MEMBER)
    // .nickName("viewer" + i)
    // .build();
    // memberRepository.save(member);
    // });
    // }

    // // 리뷰 삽입
    // @Test
    // public void insertReviewTest() {
    // // 리뷰 200개 / 영화 100 개 무작위로 추출 / 멤버 무작위 추출
    // IntStream.rangeClosed(1, 1000).forEach(i -> {
    // // 멤버 아이디 무작위
    // Long mid = (long) (Math.random() * 50) + 1;
    // // 영화 아이디 무작위
    // Long mno = (long) (Math.random() * 200) + 1;

    // int count = (int) (Math.random() * 5) + 1;
    // Review review = Review.builder()
    // .grade(count)
    // .text("Movie Review" + i)
    // .member(Member.builder().mid(mid).build())
    // .movie(Movie.builder().mno(mno).build())
    // .build();
    // reviewRepository.save(review);
    // });

    // }

    // @Test
    // public void listTest() {
    // Pageable pageable = PageRequest.of(0, 20);
    // Page<Object[]> result = movieImageRepository.getTotalList(null, null,
    // pageable);
    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    // @Test
    // public void getMovieTest() {
    // List<Object[]> result = movieImageRepository.getMovieRow(2L);
    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // // Movie movie = (Movie) result.get(0)[0];
    // // MovieImage movieImage = (MovieImage) result.get(0)[1];
    // // Long cnt = (Long) result.get(0)[2];
    // // Double avg = (double) result.get(0)[3];
    // }

    // // 리뷰 조회 테스트
    // @Test
    // public void findByMovieTest() {

    // System.out.println(reviewRepository.findByMovie(Movie.builder().mno(2L).build()));

    // }

    // // @Transactional // select 구문 2개 사용
    // @Test
    // public void findByMovieTest2() {

    // List<Review> list =
    // reviewRepository.findByMovie(Movie.builder().mno(2L).build());

    // for (Review review : list) {
    // System.out.println(review);
    // System.out.println(review.getMember().getEmail());
    // }

    // }

}
