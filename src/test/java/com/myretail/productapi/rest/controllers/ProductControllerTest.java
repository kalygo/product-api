package com.myretail.productapi.rest.controllers;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.myretail.productapi.configurations.CassandraConfigurations;
import com.myretail.productapi.framework.domain.entities.ProcessingReport;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.fetchers.ProductByTcinFetcher;
import com.myretail.productapi.framework.fetchers.ProductPriceByTcinFetcher;
import com.myretail.productapi.framework.persisters.Persister;
import com.myretail.productapi.framework.producers.Producer;
import com.myretail.productapi.framework.producers.ProducerFactory;
import com.myretail.productapi.framework.updater.ProductUpdater;
import com.myretail.productapi.framework.updater.Updater;
import com.myretail.productapi.framework.updater.UpdaterFactory;
import com.myretail.productapi.models.ProductPriceByTcin;
import com.myretail.productapi.rest.dto.ErrorDTO;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @MockBean
    public Mapper<ProductPriceByTcin> productPriceByTcinDatastaxMapper;

    @MockBean
    public CassandraConfigurations.ProductPriceByTcinAccessor productPriceByTcinAccessor;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockBean
    private Map<String, Fetcher> fetchersByName;
    @MockBean
    private Map<String, Persister> persisterByName;
    @MockBean
    private ProducerFactory producerFactory;
    @MockBean
    private UpdaterFactory updaterFactory;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Producer mockProducer;
    private ProductUpdater mockUpdater;

    private ProductByTcinFetcher productByTcinFetcher;
    private ProductPriceByTcinFetcher productPriceByTcinFetcher;

    private static final  FieldDescriptor ARRAY_OF_PRODUCT_DATA = fieldWithPath("[]").description("Array of product data");
    private static final  FieldDescriptor ID_OF_THE_PRODUCT = fieldWithPath("[*].tcin").description("Id of the product");
    private static final  FieldDescriptor TITLE_OF_THE_PRODUCT = fieldWithPath("[*].title").description("Title of the product");
    private static final  FieldDescriptor CURRENT_PRICE_OF_THE_PRODUCT = fieldWithPath("[*].currentPrice").description("Current price of the product");
    private static final  FieldDescriptor DECIMAL_VALUE_OF_THE_CURRENT_PRICE = fieldWithPath("[*].currentPrice.value").description("Decimal value of the current price");
    private static final  FieldDescriptor CURRENCY_FOR_THE_CURRENT_PRICE = fieldWithPath("[*].currentPrice.currency").description("Currency for the current price. Allowed values are [USD]");
    private static final  FieldDescriptor NEW_PRICE_OF_THE_PRODUCT = fieldWithPath("currentPrice").description("Current price of the product");
    private static final  FieldDescriptor DECIMAL_VALUE_OF_THE_NEW_PRICE = fieldWithPath("currentPrice.value").description("Decimal value of the current price");
    private static final  FieldDescriptor CURRENCY_FOR_THE_NEW_PRICE = fieldWithPath("currentPrice.currency").description("Currency for the current price. Allowed values are [USD]");
    private static final  ParameterDescriptor PATH_PARAM_LONG_TCIN = parameterWithName("tcin").description("Id of the product");
    private static final  ParameterDescriptor PATH_PARAM_STRING_TCINS = parameterWithName("tcins").description("Id of the product");
    private static final  ParameterDescriptor REQUEST_PARAM_INCLUDES = parameterWithName("includes").description("product-data to be included in the response. Allowed values are [product-fetcher, product-price-fetcher]");;


    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();

        productByTcinFetcher = mock(ProductByTcinFetcher.class);
        productPriceByTcinFetcher = mock(ProductPriceByTcinFetcher.class);
        when(fetchersByName.get("product-fetcher")).thenReturn(productByTcinFetcher);
        when(fetchersByName.get("product-price-fetcher")).thenReturn(productPriceByTcinFetcher);

        mockProducer = mock(Producer.class);
        mockUpdater = mock(ProductUpdater.class);
    }

    @Test
    public void getProductDataWithPrice() throws Exception{
        when(producerFactory.newProductProducer(any(Set.class), any(Set.class))).thenReturn(mockProducer);

        ProductDTO response1 = ProductsFactory.getProductWithPrice(13860428l, "The Big Lebowski (Blu-ray)", BigDecimal.valueOf(100.99));
        when(mockProducer.produce()).thenReturn(Lists.newArrayList(response1));

        this.mockMvc
                .perform(get("/products/{tcins}?includes=product-fetcher,product-price-fetcher", 13860428).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(PATH_PARAM_STRING_TCINS),
                        requestParameters(REQUEST_PARAM_INCLUDES),
                        responseFields(ARRAY_OF_PRODUCT_DATA, ID_OF_THE_PRODUCT, TITLE_OF_THE_PRODUCT,
                        CURRENT_PRICE_OF_THE_PRODUCT, DECIMAL_VALUE_OF_THE_CURRENT_PRICE, CURRENCY_FOR_THE_CURRENT_PRICE
                )));
    }

    @Test
    public void getProductsDataWithPrice() throws Exception{
        when(producerFactory.newProductProducer(any(Set.class), any(Set.class))).thenReturn(mockProducer);

        ProductDTO response1 = ProductsFactory.getProductWithPrice(13860428l, "The Big Lebowski (Blu-ray)", BigDecimal.valueOf(100.99));
        ProductDTO response2 = ProductsFactory.getProductWithPrice(16696652l, "Beats Solo 2 Wireless - Black", BigDecimal.valueOf(9.99));
        when(mockProducer.produce()).thenReturn(Lists.newArrayList(response1, response2));

        this.mockMvc
                .perform(get("/products/{tcins}?includes=product-fetcher,product-price-fetcher", "13860428,16696652").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(PATH_PARAM_STRING_TCINS),
                        requestParameters(REQUEST_PARAM_INCLUDES),
                        responseFields(
                        ARRAY_OF_PRODUCT_DATA,
                        ID_OF_THE_PRODUCT,
                        TITLE_OF_THE_PRODUCT,
                        CURRENT_PRICE_OF_THE_PRODUCT,
                        DECIMAL_VALUE_OF_THE_CURRENT_PRICE,
                        CURRENCY_FOR_THE_CURRENT_PRICE
                )));
    }

    @Test
    public void getProductsDataWithoutPrice() throws Exception{
        when(producerFactory.newProductProducer(any(Set.class), any(Set.class))).thenReturn(mockProducer);

        ProductDTO response1 = ProductsFactory.getProductWithoutPrice(13860428l, "The Big Lebowski (Blu-ray)");
        ProductDTO response2 = ProductsFactory.getProductWithoutPrice(16696652l, "Beats Solo 2 Wireless - Black");
        when(mockProducer.produce()).thenReturn(Lists.newArrayList(response1, response2));

        this.mockMvc
                .perform(get("/products/{tcins}?includes=product-fetcher","13860428,16696652").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(PATH_PARAM_STRING_TCINS),
                        requestParameters(REQUEST_PARAM_INCLUDES),
                        responseFields(ARRAY_OF_PRODUCT_DATA,ID_OF_THE_PRODUCT,TITLE_OF_THE_PRODUCT
                )));
    }


    @Test
    public void putProductDataWithPrice() throws Exception{
        when(updaterFactory.newProductUpdater(anyLong(), any(ProductDTO.class), any())).thenReturn(mockUpdater);
        when(mockUpdater.update()).thenReturn(new ErrorsDTO());

        ProductDTO request = ProductsFactory.getProductWithPrice(BigDecimal.valueOf(100.99));

        this.mockMvc
                .perform(put("/products/{tcin}",13860428).accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(PATH_PARAM_LONG_TCIN),
                        requestFields(NEW_PRICE_OF_THE_PRODUCT,DECIMAL_VALUE_OF_THE_NEW_PRICE, CURRENCY_FOR_THE_NEW_PRICE)));
    }


    @Test
    public void putProductDataWithPriceBadRequest() throws Exception{

        ProductDTO request = ProductsFactory.getProductWithPriceNoCurrency(BigDecimal.valueOf(100.99));
        ErrorsDTO errorsDTO = new ErrorsDTO();
        ErrorDTO errorDTO = new ErrorDTO(ProcessingReport.Event.VALIDATION_ERROR, "currency_price.currency is null");
        errorsDTO.addError(errorDTO);

        when(updaterFactory.newProductUpdater(anyLong(), any(ProductDTO.class), any())).thenReturn(mockUpdater);
        when(mockUpdater.update()).thenReturn(errorsDTO);
        this.mockMvc
                .perform(put("/products/{tcin}",13860428).accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(PATH_PARAM_LONG_TCIN),
                        requestFields(NEW_PRICE_OF_THE_PRODUCT,DECIMAL_VALUE_OF_THE_NEW_PRICE,CURRENCY_FOR_THE_NEW_PRICE.optional().type(STRING)),
                        responseFields(fieldWithPath("tcin").description("Id of the product"),
                                fieldWithPath("errorsDTO").description("Wrapper for an array of errors from the processing"),
                                fieldWithPath("errorsDTO.errors").description("Array of errors from the processing"),
                                fieldWithPath("errorsDTO.errors[*].type").description("Error level. Possible values are [WARN, ERROR]"),
                                fieldWithPath("errorsDTO.errors[*].code").description("Error code"),
                                fieldWithPath("errorsDTO.errors[*].message").description("Description of the error"),
                                fieldWithPath("errorsDTO.errors[*].moreInfo").description("Additional description if any of the error")
                                )));
    }

}