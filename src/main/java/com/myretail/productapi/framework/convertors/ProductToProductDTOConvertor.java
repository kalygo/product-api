package com.myretail.productapi.framework.convertors;

import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.domain.entities.ProductPrice;
import com.myretail.productapi.rest.dto.ErrorDTO;
import com.myretail.productapi.rest.dto.PriceDTO;
import com.myretail.productapi.rest.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ProductToProductDTOConvertor implements Convertor<Product, ProductDTO> {

    @Override
    public ProductDTO convert(Product source) {
        ProductDTO target = new ProductDTO();

        convertPrimaryProductData(source, target);
        convertCurrentPrice(source, target);
        convertProcessingReport(source, target);

        return target;
    }

    private void convertPrimaryProductData(Product source, ProductDTO target) {
        target.setTcin(source.getTcin());
        target.setTitle(source.getName());
    }

    private void convertCurrentPrice(Product source, ProductDTO target) {
        PriceDTO priceDTO = new PriceDTO();
        ProductPrice productPrice = source.getPrice();
        if(null!=productPrice) {
            priceDTO.setCurrency(productPrice.getCurrencyType());
            priceDTO.setValue(productPrice.getValue());
            if(!productPrice.isActive()) {
                priceDTO.setDeleted(true);
            }
            target.setCurrentPrice(priceDTO);
        }
    }

    private void convertProcessingReport(Product source, ProductDTO target){
        List<ErrorDTO> errors = source.getProcessingReport().getEvents().stream().map(e -> new ErrorDTO(e)).collect(Collectors.toList());
        target.addErrors(errors);
    }
}