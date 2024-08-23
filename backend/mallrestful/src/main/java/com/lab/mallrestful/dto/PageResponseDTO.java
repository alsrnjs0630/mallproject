package com.lab.mallrestful.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<T> {

    private List dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev;
    private boolean next;
    private int totalCount;
    private int prevPage;
    private int nextPage;
    private int totalPage;
    private int current;

    // @Builder : 빌더 패턴을 사용해 객체를 생성할 수 있도록 해주는 애너테이션
    // builderMethodName = "withAll" : 빌더 메서드의 이름을 withAll로 지정 ( 기본 이름 : builder )
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;                     // 페이징 처리된 데이터 리스트
        this.pageRequestDTO = pageRequestDTO;       // 요청된 페이지와 페이지 크기등의 정보 담은 DTO
        this.totalCount = (int) totalCount;         // 전체 데이터의 개수

        // 끝페이지, 시작페이지, 마지막 페이지 계산
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        int start = end - 9;
        int last = (int) (Math.ceil((totalCount / (double) pageRequestDTO.getPage())));

        // 계산된 end페이지가 실제 last 페이지를 초과하지 않도록함.
        end = end > last ? last : end;

        // 이전, 다음페이지 여부 결정
        this.prev = start > 1;
        this.next = totalCount > end * pageRequestDTO.getSize();
        // 페이지 번호 목록 생성
        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

        // 이전, 다음 페이지 번호 설정
        if(prev) {
            this.prevPage = start - 1;
        }
        if(next) {
            this.nextPage = end + 1;
        }
        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDTO.getPage();
    }
}
