package com.lab.mallrestful.controller.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component // 스프링 빈으로 등록하기 위해 사용되는 어노테이션. 이 클래스를 자동으로 스프링의 관리하에 둠.
@Log4j2
@RequiredArgsConstructor
// CustomFileUtil : 파일 데이터의 입출력을 담당. 프로그램이 시작되면 upload라는 폴더를 체크해서 자동으로 생성
// 파일 업로드 작업은 saveFiles()로 작성
public class CustomFileUtil {
    @Value("${com.lab.upload.path}") // 파일이 저장될 기본 경로 지정
    private String uploadPath;

    @PostConstruct // 스프링 빈이 생성된 후 초기화 작업 수행. 빈이 생성되고 의존성 주입이 완료된 후 자동 호출
    public void init() { // uploadPath에 설정된 경로를 바탕으로 폴더가 존재하지 않으면 폴더 생성
        File tempFolder = new File(uploadPath);
        // 설정 파일에서 주입된 uploadPath를 기반으로 폴더를 가리키는 File 객체 생성

        if (tempFolder.exists() == false) {
            tempFolder.mkdirs(); // mkdirs : 상위 디렉토리까지 포함하여 폴더 생성
        }
        uploadPath = tempFolder.getAbsolutePath(); // 생성된 폴더의 절대 경로를 다시 할당

        log.info("--------------------------------------");
        log.info(uploadPath); // 설정된 경로 출력
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        // 다수의 파일을 받아와 설정된 경로에 저장하고, 저장된 파일의 이름 리스트 반환

        if (files == null || files.size() == 0) {
            return null; // 파일 리스트가 null 또는 비어있는 경우 null 반환. 처리 중단
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            // 파일을 하나씩 처리하면서, 파일 이름에 고유햔 UUID를 붙여 중복 방지
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, savedName); // 저장될 파일의 경로 생성

            try {
                Files.copy(multipartFile.getInputStream(), savePath); // 업로드된 파일을 지정 경로에 저장
                String contentType = multipartFile.getContentType();
                // 이미지여부 확인
                if(contentType != null && contentType.startsWith("image")) { // 이미지 파일일 경우 썸네일 생성
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName); // 썸네일 파일 경로 설정, 원본 파일 이름 앞에 "s_" 추가
                    Thumbnails.of(savePath.toFile()).size(400,400).toFile(thumbnailPath.toFile()); // 썸네일 이미지 크기 설정
                }
                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } // end for
        return uploadNames;
    }

    public ResponseEntity<Resource> getFile(String fileName) {
        // 특정 파일 이름을 기반으로 파일을 검색하고 클라이언트에게 반환

        // 파일 시스템에서 파일을 가리키는 자원 'Resource' 생성
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        // 요청한 파일이 존재하지 않는 경우 기본 이미지 반환
        if(!resource.exists()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }

        HttpHeaders headers = new HttpHeaders();

        try{
            //  Files.probeContentType : 파일의 MIME 타입 자동 탐지.
            // 이 MIME 타입을 'Content-Type' 헤더에 추가하여 클라이언트가 파일의 타입을 인식할 수 있게 함
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.size() == 0) {
            return; // 비어있거나 null인 경우 메서드는 아무 작업을 수행하지 않고 종료
        }

        fileNames.forEach(fileName -> {
            // 썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName); // 썸네일 파일 경로
            Path filePath = Paths.get(uploadPath, fileName); // 원본 파일 경로

            try {
                Files.deleteIfExists(filePath);         // 경로에 파일이 존재하면 삭제
                Files.deleteIfExists(thumbnailPath);
            }catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}
