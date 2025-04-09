package duan.demo.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private String productCode;
    private Integer quantity;
    private String status;
    private String createdBy;
    private Set<Integer> categoryIds = new HashSet<>(); // Danh sách ID danh mục
}
