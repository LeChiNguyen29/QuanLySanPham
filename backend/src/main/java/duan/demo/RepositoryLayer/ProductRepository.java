package duan.demo.RepositoryLayer;

import duan.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Phương thức tùy chỉnh (nếu cần): Tìm sản phẩm theo tên với phân trang
    Page<Product> findByNameContaining(String name, Pageable pageable);
}
