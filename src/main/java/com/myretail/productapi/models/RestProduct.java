package com.myretail.productapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.persisters.Persistable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestProduct {
    @JsonProperty("product")
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Product implements FetchedResultRow, Persistable {

        @JsonProperty("item")
        private Item item;

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }


        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Item {

            @JsonProperty("tcin")
            private String tcin;

            @JsonProperty("product_description")
            private ProductDescription productDescription;

            public String getTcin() {
                return tcin;
            }

            public void setTcin(String tcin) {
                this.tcin = tcin;
            }

            public ProductDescription getProductDescription() {
                return productDescription;
            }

            public void setProductDescription(ProductDescription productDescription) {
                this.productDescription = productDescription;
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class ProductDescription {

                @JsonProperty("title")
                private String title;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }
        }
    }
}
