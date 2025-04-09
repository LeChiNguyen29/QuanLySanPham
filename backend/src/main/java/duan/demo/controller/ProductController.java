package duan.demo.controller;

import duan.demo.ServiceLayer.ProductService;
import duan.demo.dto.ProductRequest;
import duan.demo.entity.Product;
import duan.demo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Lấy tất cả sản phẩm với phân trang
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(page = 0, size = 2) Pageable pageable) {
        Page<Product> products = productService.findAllProducts(pageable);
        return ResponseEntity.ok(products);
    }


    // Tìm kiếm sản phẩm theo tên có phân trang
    @GetMapping("/search")
    public Page<Product> searchProductsByName(@RequestParam("name") String name, Pageable pageable) {
        return productService.findProductsByName(name, pageable);
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Tạo mới sản phẩm với validation
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setProductCode(productRequest.getProductCode());
        product.setQuantity(productRequest.getQuantity());
        product.setStatus(productRequest.getStatus());
        product.setCreatedBy(productRequest.getCreatedBy());

        Product savedProduct = productService.addProductToCategories(product, productRequest.getCategoryIds());

        return ResponseEntity.ok(savedProduct);
    }


    // Cập nhật sản phẩm với validation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Valid ProductRequest productRequest, // Dùng DTO thay vì entity trực tiếp
            BindingResult bindingResult) {

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Gọi service để cập nhật sản phẩm (truyền DTO đầy đủ)
        Product updatedProduct = productService.updateProduct(id, productRequest);

        return ResponseEntity.ok(updatedProduct);
    }


    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id); // Nếu không tìm thấy, sẽ ném ProductNotFoundException
        return ResponseEntity.noContent().build(); // 204 No Content nếu xóa thành công
    }
}
