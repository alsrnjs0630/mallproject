package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    * @EntityGraph : 엔티티를 조회할 때 연관된 엔티티를 한 번의 쿼리로 함께 가져옴
    * attributePaths 속성 : 어던 연관된 속성을 Eager 로딩할지 지정 => 'imageList'를 지정했으므로 'Product'
    * 엔티티를 조회할 때 연관된 'imageList' 컬렉션도 함께 로딩됨.
    */
    @EntityGraph(attributePaths = "imageList")
    /*
    * @Query : JPQL(Java Persistence Query Language) 쿼리를 사용하여 특정 조건에 맞는 엔티티 조회
    */
    @Query("select p from Product p where p.pno = :pno")
    // @Param : 쿼리에서 사용된 파라미터를 메서드 인자와 매핑
    Optional<Product> selectOne(@Param("pno") Long pno);

    // 특정 제품의 삭제 플래그 업데이트 => 제품이 삭제 되었는지 표시
    @Modifying // 쿼리가 DB의 상태를 변경하는 쿼리임을 나타내는 애너테이션 => 데이터 삽입, 업데이트, 삭제 작업에 사용
    @Query("update Product p set p.delFlag =:flag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);

    // 페이지네이션된 제품 목록과 각 제품의 기본 이미지 조회
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable); // 제품과 이미지 데이터를 포함하는 객체 배열을 페이지 단위로 반환
}
