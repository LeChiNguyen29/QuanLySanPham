package duan.demo.RepositoryLayer;

import duan.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Phương thức tùy chỉnh (nếu cần): Tìm danh mục theo tên với phân trang
    Page<Category> findByNameContaining(String name, Pageable pageable);
}
