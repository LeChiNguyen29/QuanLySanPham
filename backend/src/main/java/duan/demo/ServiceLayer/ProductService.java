package duan.demo.ServiceLayer;

import duan.demo.RepositoryLayer.CategoryRepository;
import duan.demo.RepositoryLayer.ProductRepository;
import duan.demo.dto.ProductRequest;
import duan.demo.entity.Category;
import duan.demo.entity.Product;
import duan.demo.exception.CategoryNotFoundException;
import duan.demo.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ✅ Lấy tất cả sản phẩm với phân trang (Chỉ đọc)
    @Transactional(readOnly = true)
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ✅ Tìm kiếm sản phẩm theo tên với phân trang (Chỉ đọc)
    @Transactional(readOnly = true)
    public Page<Product> findProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable);
    }

    // ✅ Lấy sản phẩm theo ID (Chỉ đọc)
    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
    }

    // ✅ Tạo sản phẩm mới và gán danh mục
    @Transactional
    public Product addProductToCategories(Product product, Set<Integer> categoryIds) {
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));

        if (categories.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy danh mục nào phù hợp");
        }

        product.setCategories(categories);
        return productRepository.save(product);
    }

    // ✅ Cập nhật sản phẩm
    @Transactional
    public Product updateProduct(Integer id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Cập nhật thông tin sản phẩm (chỉ cập nhật nếu có giá trị mới)
        if (productRequest.getName() != null) product.setName(productRequest.getName());
        if (productRequest.getDescription() != null) product.setDescription(productRequest.getDescription());
        if (productRequest.getPrice() != null) product.setPrice(productRequest.getPrice());
        if (productRequest.getProductCode() != null) product.setProductCode(productRequest.getProductCode());
        if (productRequest.getQuantity() != null) product.setQuantity(productRequest.getQuantity());
        if (productRequest.getStatus() != null) product.setStatus(productRequest.getStatus());
        if (productRequest.getCreatedBy() != null) product.setModifiedBy(productRequest.getCreatedBy());

        // Cập nhật danh mục nếu có categoryIds hợp lệ
        if (productRequest.getCategoryIds() != null && !productRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(productRequest.getCategoryIds()));

            if (categories.size() != productRequest.getCategoryIds().size()) {
                throw new CategoryNotFoundException("Một hoặc nhiều danh mục không hợp lệ");
            }

            product.setCategories(categories);
        }

        return productRepository.save(product);
    }

    // ✅ Xóa sản phẩm
    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
