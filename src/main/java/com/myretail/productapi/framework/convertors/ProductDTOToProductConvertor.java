package com.myretail.productapi.framework.convertors;

import com.myretail.productapi.framework.domain.entities.BaseEntity;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.domain.entities.ProductPrice;
import com.myretail.productapi.rest.dto.PriceDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.apache.commons.lang3.math.NumberUtils;

import static java.lang.Boolean.TRUE;

public class ProductDTOToProductConvertor implements Convertor<ProductDTO, Product> {
    @Override
    public Product convert(ProductDTO source) {
        Product target = new Product();
        convertPrimaryData(source, target);
        convertPrice(source, target);

        return target;
    }

    private void convertPrimaryData(ProductDTO source, Product target) {
        if(source.getTcin()!=null) {
            target.setTcin(NumberUtils.createLong(source.getTcin()));
        }
        target.setName(source.getTitle());
    }

    private void convertPrice(ProductDTO source, Product target) {
        PriceDTO srcPrice = source.getCurrentPrice();
        ProductPrice productPrice = new ProductPrice(srcPrice.getValue(), srcPrice.getCurrency());
        productPrice.setStatus(srcPrice.isDeleted()!=null && srcPrice.isDeleted().equals(TRUE) ? BaseEntity.Status.DELETED : BaseEntity.Status.ACTIVE);
        target.setPrice(productPrice);
    }
}
