package com.example.movie.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movie.dto.MovieDTO;
import com.example.movie.dto.PageRequestDTO;
import com.example.movie.dto.PageResultDTO;
import com.example.movie.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/list")
    public void getList(PageRequestDTO pageRequestDTO, Model model) {
        log.info("영화 리스트 요청");
        PageResultDTO<MovieDTO> result = movieService.getList(pageRequestDTO);
        model.addAttribute("result", result);
    }

    @GetMapping({ "/read", "/modify" })
    public void getMovie(Long mno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("movie 상세 조회 요청 : {}", mno);
        MovieDTO dto = movieService.getRow(mno);
        model.addAttribute("dto", dto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove")
    public String removeMovie(Long mno, PageRequestDTO pageRequestDTO, RedirectAttributes rttr) {
        log.info("movie 삭제 요청 : {}", mno);

        movieService.deleteRow(mno);

        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("size", pageRequestDTO.getSize());
        rttr.addAttribute("type", pageRequestDTO.getType());
        rttr.addAttribute("keyword", pageRequestDTO.getKeyword());

        return "redirect:/movie/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public void getCreate(PageRequestDTO pageRequestDTO) {
        log.info("영화 등록 폼 요청");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String postCreate(MovieDTO dto, PageRequestDTO pageRequestDTO, RedirectAttributes rttr) {
        log.info("영화 등록 요청 : {}", dto);

        Long mno = movieService.createMovie(dto);
        rttr.addAttribute("mno", mno);
        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("size", pageRequestDTO.getSize());
        rttr.addAttribute("type", pageRequestDTO.getType());
        rttr.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/movie/read";
    }

}
