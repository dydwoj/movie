package com.example.movie.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.movie.dto.MovieImageDTO;
import com.example.movie.entity.MovieImage;
import com.example.movie.repository.MovieImageRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileCheckTask {

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Value("${com.example.movie.upload.path}")
    private String uploadPath;

    // 전일자 폴더의 리스트 추출
    private String getFolderYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String str = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return str.replace("-", File.separator);
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void checkFile() {
        log.info("file Check Task...");

        // 데이터베이스에서 어제날짜 파일 목록 추출
        List<MovieImage> oldImages = movieImageRepository.getOldImages();
        // entity => dto 변경
        List<MovieImageDTO> movieImageDTOs = oldImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .inum(movieImage.getInum())
                    .uuid(movieImage.getUuid())
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        // 여기 까진 DB 내용을 dto 로 바꾸는 내용
        // ===========================================================================

        // ===========================================================================
        // 폴더에 들어있는 파일명과 일치하도록 db 내용 변경

        // java.nio.Path
        List<Path> fileListPaths = movieImageDTOs.stream() // => DB(entity) 의 파일들을 dto 로 변경 후 우리가 저장하는 파일 이름들과 동일하게 변경
                .map(dto -> Paths.get(uploadPath, dto.getImagelURL(), dto.getUuid() + "_" + dto.getImgName()))
                .collect(Collectors.toList());

        movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getImagelURL(), "s_" + dto.getUuid() + "_" + dto.getImgName()))
                .forEach(p -> fileListPaths.add(p));

        // ===========================================================================

        // 어제 폴더에 접근
        File targetDir = Paths.get(uploadPath, getFolderYesterday()).toFile();

        // 파일들을을 읽어서 가져옴 => 대신 어제자 파일들과 비교함
        // => 그 후, 비교해서 false(DB와 같은 내용이 아닌것들) 을 담음
        File[] removeFiles = targetDir.listFiles(f -> fileListPaths.contains(f.toPath()) == false);

        // 담은 파일들이 null 이 아니면 그 파일들은 지워주세요
        if (removeFiles != null) {
            for (File file : removeFiles) {
                log.warn(file.getAbsolutePath());
                file.delete();
            }
        }

    }

}
