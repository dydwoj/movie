package com.example.movie.repository.total;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.entity.QMovie;
import com.example.movie.entity.QMovieImage;
import com.example.movie.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MovieImageReviewRepositoryImpl extends QuerydslRepositorySupport implements MovieImageReviewRepository {

    public MovieImageReviewRepositoryImpl() {
        super(MovieImage.class);
    }

    @Override
    public Page<Object[]> getTotalList(String type, String keyword, Pageable pageable) {

        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QReview review = QReview.review;

        JPQLQuery<MovieImage> query = from(movieImage);
        query.leftJoin(movie).on(movieImage.movie.eq(movie));

        JPQLQuery<Long> count = JPAExpressions.select(review.countDistinct()).from(review)
                .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Double> avg = JPAExpressions.select(review.grade.avg().round()).from(review)
                .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Long> min = JPAExpressions.select(movieImage.inum.min()).from(movieImage).groupBy(movieImage.movie);

        JPQLQuery<Tuple> tuple = query.select(movie, movieImage, count, avg).where(movieImage.inum.in(min));
        // .orderBy(movie.mno.desc());

        // ==============================================================================================================
        // 검색
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = movie.mno.gt(0);
        builder.and(expression);

        BooleanBuilder condition = new BooleanBuilder();
        if (!type.isEmpty() && type.contains("title")) {
            condition.or(movie.title.contains(keyword));
            builder.and(condition);
        }
        tuple.where(builder);

        // ==============================================================================================================
        // Sort 생성
        Sort sort = pageable.getSort(); // => bno 로 Sort 요청한 값 가져옴

        sort.stream().forEach(order -> { // Sort 기준이 여러개 일수 있기에 forEach 로 DESC를 만듬
            // import com.querydsl.core.types.Order;
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();
            PathBuilder<Movie> orderBuilder = new PathBuilder<>(Movie.class, "movie");
            // => 어떤 클래스를 기준으로 Sort 의 기준을 정의할 것인지 정의하는 구문
            // Board 클래스의 Sort 의 기준을 정의한 것

            tuple.orderBy(new OrderSpecifier(direction, orderBuilder.get(prop))); // => 뽑아낸 SELECT 문 ORDER BY 적용
        });

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        long totalCnt = tuple.fetchCount();

        return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, totalCnt);
    }

    @Override
    public List<Object[]> getMovieRow(Long mno) {
        log.info("영화 상세 정보 요청 : {}", mno);

        QMovie movie = QMovie.movie;
        QMovieImage movieImage = QMovieImage.movieImage;
        QReview review = QReview.review;

        JPQLQuery<MovieImage> query = from(movieImage);
        query.leftJoin(movie).on(movieImage.movie.eq(movie));

        JPQLQuery<Long> count = JPAExpressions.select(review.countDistinct()).from(review)
                .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Double> avg = JPAExpressions.select(review.grade.avg().round()).from(review)
                .where(review.movie.eq(movieImage.movie));

        JPQLQuery<Tuple> tuple = query.select(movie, movieImage, count, avg)
                .where(movieImage.movie.mno.eq(mno))
                .orderBy(movieImage.inum.desc());

        List<Tuple> result = tuple.fetch();

        List<Object[]> row = result.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return row;
    }

}
