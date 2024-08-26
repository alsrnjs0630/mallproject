package com.lab.mallrestful.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data // @Data : Getter, Setter, toString(), equals(), hashCode() 메서드 자동 생성
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;

    @Builder.Default // 빌더 패턴을 사용할 때 기본 값 설정
    // MultipartFile : 스프링에서 파일 업로드 기능을 처리하기 위한 인터페이스
    // 제품에 대한 다중 파일 업로드를 지원하기 위해 사용, 사용자가 업로드하는 파일이 이 리스트에 MultipartFile 객체들이 담김.
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    // 업로드가 완료된 파일의 이름만 문자열로 보관한 리스트
    // DB 파일 이름들을 처리하는 용도로 사용
    private List<String> uploadFileNames = new ArrayList<>();
}
