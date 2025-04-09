package duan.demo.RepositoryLayer;

import duan.demo.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Tìm ProductCategory theo productId và categoryId
    Optional<ProductCategory> findByProduct_IdAndCategory_Id(Integer productId, Integer categoryId);

    // Tìm tất cả ProductCategory theo productId
    List<ProductCategory> findByProduct_Id(Integer productId);

    // Tìm tất cả ProductCategory theo categoryId
    List<ProductCategory> findByCategory_Id(Integer categoryId);
}
