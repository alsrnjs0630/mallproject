package com.lab.mallrestful.controller;

import com.lab.mallrestful.controller.util.CustomFileUtil;
import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.ProductDTO;
import com.lab.mallrestful.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService; // ProductService 주입
    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {
        log.info("register: " + productDTO);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames);
        log.info(uploadFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list..............." + pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }
}
