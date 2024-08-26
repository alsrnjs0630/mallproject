package com.lab.mallrestful.service;

import com.lab.mallrestful.domain.Product;
import com.lab.mallrestful.domain.ProductImage;
import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.ProductDTO;
import com.lab.mallrestful.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        // pageRequestDTO를 사용하여 Pageable 객체 생성
        log.info("getList.................");

        // 페이지 시작 번호가 0부터 시작하므로
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(), Sort.by("pno").descending());

        // 페이지네이션된 제품 및 이미지 데이터 result에 저장
        Page<Object[]> result = productRepository.selectList(pageable);

        // Object[]를 ProductDTO로 매핑 => Product와 ProductImage 추출
        List<ProductDTO> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            // 빌더 패턴을 사용하여 새로운 ProductDTO 객체 생성
            ProductDTO productDTO = ProductDTO.builder().pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;
        }).collect(Collectors.toList()); // map()으로 생성된 Stream<ProductDTO>를 List<ProductDTO>로 변환

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList) // 현재 페이지에 대한 제품 데이터
                .totalCount(totalCount) // 전체 데이터의 총 개수
                .pageRequestDTO(pageRequestDTO) // 클라이언트가 요청한 페이지네이션 정보를 담고 있는 DTO
                .build();
    }

    // ProductDTO 객체를 Product 객체로 변환후 DB 저장. => 등록 메서드
    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO); // DTO 객체를 Product 엔티티로 변환
        Product result = productRepository.save(product); // 데이터베이스에 저장
        return result.getPno(); // 제품 번호 pno 반환
    }

    private Product dtoToEntity(ProductDTO productDTO) {
        Product product = Product.builder().pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        // 업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames == null) {
            return product;
        }

        uploadFileNames.stream().forEach(uploadName -> {
            product.addImageString(uploadName);
        });

        return product;
    }

    @Override
    public ProductDTO get(Long pno) {
        java.util.Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityDTO(product);
        return productDTO;
    }

    private ProductDTO entityDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.size() == 0) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();
        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {
        // step1 read
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product= result.orElseThrow();

        // change pname, pdesc, price
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());

        // upload File -- clear first
        product.clearList();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames != null && uploadFileNames.size() > 0) {
            uploadFileNames.stream().forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }
}
