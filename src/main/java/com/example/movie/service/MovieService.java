package com.example.movie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movie.dto.MovieDTO;
import com.example.movie.dto.MovieImageDTO;
import com.example.movie.dto.PageRequestDTO;
import com.example.movie.dto.PageResultDTO;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.repository.MovieImageRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class MovieService {

        private final MovieImageRepository movieImageRepository;
        private final MovieRepository movieRepository;
        private final ReviewRepository reviewRepository;

        public PageResultDTO<MovieDTO> getList(PageRequestDTO pageRequestDTO) {

                Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                                Sort.by("mno").descending());

                Page<Object[]> result = movieImageRepository.getTotalList(pageRequestDTO.getType(),
                                pageRequestDTO.getKeyword(),
                                pageable);
                Function<Object[], MovieDTO> function = (entity -> entityToDto((Movie) entity[0],
                                (List<MovieImage>) Arrays.asList((MovieImage) entity[1]),
                                (Long) entity[2], (Double) entity[3]));

                List<MovieDTO> dtoList = result.stream().map(function).collect(Collectors.toList());
                Long totalCount = result.getTotalElements();

                PageResultDTO<MovieDTO> pageResultDTO = PageResultDTO.<MovieDTO>withAll()
                                .dtoList(dtoList)
                                .totalCount(totalCount)
                                .pageRequestDTO(pageRequestDTO)
                                .build();

                return pageResultDTO;
        }

        public MovieDTO getRow(Long mno) {
                List<Object[]> result = movieImageRepository.getMovieRow(mno);
                Movie movie = (Movie) result.get(0)[0];
                List<MovieImage> movieImages = new ArrayList<>();
                result.forEach(arr -> {
                        MovieImage movieImage = (MovieImage) arr[1];
                        movieImages.add(movieImage);
                });
                Long cnt = (Long) result.get(0)[2];
                Double avg = (Double) result.get(0)[3];

                return entityToDto(movie, movieImages, cnt, avg);
        }

        @Transactional
        public void deleteRow(Long mno) {
                // 제거 대상 영화 찾기
                Movie movie = Movie.builder().mno(mno).build();

                // 자식 삭제
                movieImageRepository.deleteByMovie(movie);
                reviewRepository.deleteByMovie(movie);

                // 부모 삭제
                movieRepository.delete(movie);
        }

        @Transactional
        public Long createMovie(MovieDTO dto) {
                log.info("영화 삽입 : {}", dto);
                // dto => entity
                Map<String, Object> resultMap = dtoToEntity(dto);
                Movie movie = (Movie) resultMap.get("movie");
                List<MovieImage> movieImages = (List<MovieImage>) resultMap.get("movieImages");

                movieRepository.save(movie);
                movieImages.forEach(movieImage -> {
                        movieImageRepository.save(movieImage);
                });
                return movie.getMno();
        }

        private MovieDTO entityToDto(Movie movie, List<MovieImage> movieImages, Long reviewCnt, Double avg) {
                MovieDTO movieDTO = MovieDTO.builder()
                                .mno(movie.getMno())
                                .title(movie.getTitle())
                                .createdDate(movie.getCreatedDate())
                                .build();

                // 이미지 정보 담기
                List<MovieImageDTO> mImageDTOs = movieImages.stream().map(movieImage -> {
                        return MovieImageDTO.builder()
                                        .inum(movieImage.getInum())
                                        .uuid(movieImage.getUuid())
                                        .imgName(movieImage.getImgName())
                                        .path(movieImage.getPath())
                                        .build();
                }).collect(Collectors.toList());

                movieDTO.setMovieImages(mImageDTOs);
                movieDTO.setAvg(avg != null ? avg : 0.0);
                movieDTO.setReviewCnt(reviewCnt);
                return movieDTO;
        }

        private Map<String, Object> dtoToEntity(MovieDTO dto) {
                Map<String, Object> resultMap = new HashMap<>();

                Movie movie = Movie.builder()
                                .mno(dto.getMno())
                                .title(dto.getTitle())
                                .build();
                resultMap.put("movie", movie);

                List<MovieImageDTO> movieImageDTOs = dto.getMovieImages();

                if (movieImageDTOs != null && movieImageDTOs.size() > 0) {
                        List<MovieImage> movieImages = movieImageDTOs.stream().map(imageDto -> {
                                MovieImage movieImage = MovieImage.builder()
                                                .path(imageDto.getPath())
                                                .uuid(imageDto.getUuid())
                                                .imgName(imageDto.getImgName())
                                                .movie(movie)
                                                .build();
                                return movieImage;
                        }).collect(Collectors.toList());
                        resultMap.put("movieImages", movieImages);
                }
                return resultMap;

        }

}
