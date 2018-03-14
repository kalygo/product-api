package com.myretail.productapi.rest.controllers;

import com.myretail.productapi.rest.dto.PriceDTO;
import com.myretail.productapi.rest.dto.ProductDTO;

import java.math.BigDecimal;

import static com.myretail.productapi.framework.domain.entities.CurrencyType.USD;

public class ProductsFactory {

    static ProductDTO getProductWithoutPrice(Long tcin, String title) {
        ProductDTO product = new ProductDTO();
        product.setTcin(tcin);
        product.setTitle(title);
        return product;
    }

    static ProductDTO getProductWithPrice(Long tcin, String title, BigDecimal price) {
        ProductDTO product = new ProductDTO();
        product.setTcin(tcin);
        product.setTitle(title);
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setValue(price);
        priceDTO.setCurrency(USD);
        product.setCurrentPrice(priceDTO);
        return product;
    }

    static ProductDTO getProductWithPrice(BigDecimal price) {
        ProductDTO product = new ProductDTO();
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setValue(price);
        priceDTO.setCurrency(USD);
        product.setCurrentPrice(priceDTO);
        return product;
    }

    static ProductDTO getProductWithPriceNoCurrency(BigDecimal price) {
        ProductDTO product = new ProductDTO();
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setValue(price);
        product.setCurrentPrice(priceDTO);
        return product;
    }
}
