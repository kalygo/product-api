package com.myretail.productapi.rest.dto;

import java.util.List;

public class ProductsDTO extends BaseDTO {

    private List<ProductDTO> productDTOS;

    public List<ProductDTO> getProductDTOS() {
        return productDTOS;
    }

    public void setProductDTOS(List<ProductDTO> productDTOS) {
        this.productDTOS = productDTOS;
    }
}
