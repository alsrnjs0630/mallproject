package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Product;
import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.ProductDTO;
import com.lab.mallrestful.service.ProductService;
import com.lab.mallrestful.service.ProductServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
@Log4j2
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductServiceImpl productServiceImpl;
    @Autowired
    private ProductService productService;

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            Product product = Product.builder().pname("상품" + i).price(100 * i).pdesc("상품설명 " + i).delFlag(false).build();
            // 2개의 이미지 파일 추가
            product.addImageString(UUID.randomUUID().toString() + "-" + "IMAGE1.jpg");
            product.addImageString(UUID.randomUUID().toString() + "-" + "IMAGE2.jpg");
            productRepository.save(product);
            log.info("-------------------");
        }
    }

    /*@Transactional
    @Test
    public void testRead() {
        Long pno = 1L;
        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product); // ---------1
        log.info(product.getImageList()); // ----------2
    }*/

    @Test
    public void testRead2() {
        Long pno = 1L;
        Optional<Product> result = productRepository.selectOne(pno);
        // selectOne() 호출 => ProductRepository의 @Query에 정의된 JPQL 쿼리 실행
        // pno가 주어진 파라미터와 일치하는'Product' 엔티티를 찾음
        Product product = result.orElseThrow();
        log.info(product);
        log.info(product.getImageList());

        /** 필요한 테이블만 먼저 조회 => Lazy loading
        * 한 번에 모든 테이블을 같이 조회 => Eager loading*/


    }

    @Commit
    @Test
    public void testDelete() {
        Long pno = 2L;
        productRepository.updateToDelete(pno, true);
    }

    @Test
    public void testUpdate() {
        Long pno = 10L;

        Product product = productRepository.selectOne(pno).get();
        product.changeName("10번 상품");
        product.changeDesc("10번 상품 설명입니다.");
        product.changePrice(5000);

        // 첨부파일 수정
        product.clearList();

        product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString() + "-" + "NEWIMAGE3.jpg");

        productRepository.save(product);
    }

    @Transactional
    @Test
    public void testList() {
        /*// org.springframework.data.domain 패키지
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        // java.util
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));*/

        // 빌더패턴을 사용하여 PageRequestDTO 객체 생성. 기본생성자 사용
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);
        result.getDtoList().forEach(dto -> log.info(dto));
    }

    @Test
    public void testRegister() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(10000)
                .build();

        // uuid가 있어야함
        productDTO.setUploadFileNames(
                java.util.List.of(UUID.randomUUID() + "_" + "Test1.jpg",
                        UUID.randomUUID() + "_" + "Test2.jpg"));

        productService.register(productDTO);

    }

    @Test
    public void testRead() {
        // 실제 존재하는 번호로 테스트(DB에서 확인)
        Long pno = 9L;

        ProductDTO productDTO = productService.get(pno);

        log.info(productDTO);
        log.info(productDTO.getUploadFileNames());
    }
}