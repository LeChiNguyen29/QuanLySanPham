package duan.demo.controller;


import duan.demo.ServiceLayer.ProductCategoryService;
import duan.demo.dto.ProductCategoryDTO;
import duan.demo.entity.ProductCategory;
import duan.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    // 1. Lấy tất cả ProductCategory
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAllProductCategories() {
        List<ProductCategoryDTO> productCategories = productCategoryService.getProductCategoryMappings();
        return ResponseEntity.ok(productCategories);
    }

    // Tạo ProductCategory mới
    @PostMapping
    public ResponseEntity<ProductCategory> createProductCategory(@RequestBody ProductCategory productCategory) {
        ProductCategory newProductCategory = productCategoryService.createProductCategory(productCategory);
        return ResponseEntity.status(201).body(newProductCategory);  // Trả về HTTP 201 Created
    }

    // Cập nhật ProductCategory
    @PutMapping("/{productId}/{categoryId}")
    public ResponseEntity<ProductCategory> updateProductCategory(
            @PathVariable Integer productId,
            @PathVariable Integer categoryId,
            @RequestBody ProductCategory updatedProductCategory) {
        ProductCategory updated = productCategoryService.updateProductCategory(productId, categoryId, updatedProductCategory);

        // Nếu không tìm thấy ProductCategory, ném ResourceNotFoundException
        if (updated == null) {
            throw new ResourceNotFoundException("ProductCategory not found with productId: " + productId + " and categoryId: " + categoryId);
        }

        return ResponseEntity.ok(updated);
    }

    // Xóa ProductCategory
    @DeleteMapping("/{productId}/{categoryId}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Integer productId, @PathVariable Integer categoryId) {
        boolean isDeleted = productCategoryService.deleteProductCategory(productId, categoryId);

        // Nếu không tìm thấy ProductCategory, ném ResourceNotFoundException
        if (!isDeleted) {
            throw new ResourceNotFoundException("ProductCategory not found with productId: " + productId + " and categoryId: " + categoryId);
        }

        return ResponseEntity.noContent().build(); // Trả về HTTP 204 No Content khi xóa thành công
    }
}
