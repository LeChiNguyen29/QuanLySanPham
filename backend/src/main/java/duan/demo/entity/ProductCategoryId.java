package duan.demo.entity;


import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ProductCategoryId implements Serializable {
    private Integer productId;
    private Integer categoryId;
}
