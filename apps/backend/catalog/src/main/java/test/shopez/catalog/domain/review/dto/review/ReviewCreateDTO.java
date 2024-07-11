package test.shopez.catalog.domain.review.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import test.shopez.catalog.domain.product.validator.ValidProductId;

@Data
public class ReviewCreateDTO {
    @NotBlank
    private String author;

    @NotBlank
    @ValidProductId
    private String product;

    @Min(1)
    @Max(5)
    private int note;

    private String message;
}
