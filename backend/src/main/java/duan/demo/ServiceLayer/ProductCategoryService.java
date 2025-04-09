package duan.demo.ServiceLayer;

import duan.demo.RepositoryLayer.ProductCategoryRepository;
import duan.demo.dto.ProductCategoryDTO;
import duan.demo.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    // 1. Lấy tất cả ProductCategory
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public List<ProductCategoryDTO> getProductCategoryMappings() {
        String query = "SELECT p.name AS productName, c.name AS categoryName " +
                "FROM ProductCategory pc " +
                "JOIN Product p ON pc.productId = p.id " +
                "JOIN Category c ON pc.categoryId = c.id";

        return jdbcTemplate.query(query, (rs, rowNum) ->
                new ProductCategoryDTO(
                        rs.getString("productName"),
                        rs.getString("categoryName")
                )
        );
    }
    // 2. Tạo ProductCategory mới
    @Transactional
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        // Kiểm tra nếu ProductCategory đã tồn tại
        Optional<ProductCategory> existingProductCategory = productCategoryRepository.findByProduct_IdAndCategory_Id(
                productCategory.getProduct().getId(), productCategory.getCategory().getId());

        if (existingProductCategory.isPresent()) {
            throw new RuntimeException("ProductCategory đã tồn tại.");
        }

        // Nếu không tồn tại thì lưu mới
        return productCategoryRepository.save(productCategory);
    }

    // 3. Cập nhật ProductCategory
    @Transactional
    public ProductCategory updateProductCategory(Integer productId, Integer categoryId, ProductCategory updatedProductCategory) {
        ProductCategory productCategory = productCategoryRepository.findByProduct_IdAndCategory_Id(productId, categoryId)
                .orElseThrow(() -> new RuntimeException("ProductCategory không tồn tại."));

        // Cập nhật thông tin
        productCategory.setCreatedBy(updatedProductCategory.getCreatedBy());
        productCategory.setModifiedBy(updatedProductCategory.getModifiedBy());
        productCategory.setModifiedDate(updatedProductCategory.getModifiedDate());

        return productCategoryRepository.save(productCategory);
    }

    // 4. Xóa ProductCategory
    @Transactional
    public boolean deleteProductCategory(Integer productId, Integer categoryId) {
        // Tìm kiếm ProductCategory với productId và categoryId
        ProductCategory productCategory = productCategoryRepository.findByProduct_IdAndCategory_Id(productId, categoryId)
                .orElseThrow(() -> new RuntimeException("ProductCategory không tồn tại."));

        // Xóa ProductCategory nếu tìm thấy
        productCategoryRepository.delete(productCategory);

        // Trả về true nếu xóa thành công
        return true;
    }


    // 5. Lấy ProductCategory theo ProductId
    @Transactional
    public List<ProductCategory> findByProductId(Integer productId) {
        return productCategoryRepository.findByProduct_Id(productId);
    }

    // 6. Lấy ProductCategory theo CategoryId
    @Transactional
    public List<ProductCategory> findByCategoryId(Integer categoryId) {
        return productCategoryRepository.findByProduct_Id(categoryId);
    }
}
