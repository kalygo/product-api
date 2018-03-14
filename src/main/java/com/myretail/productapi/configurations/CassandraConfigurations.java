package com.myretail.productapi.configurations;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.enums.EnumNameCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.myretail.productapi.models.ProductPriceByTcin;
import com.myretail.productapi.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;

@Configuration
@Profile(value = {"dev","prod"})
public class CassandraConfigurations {

    @Value("${cassandra.contactpoints}")
    public String cassandraContactpoints;

    @Value("${cassandra.port}")
    public Integer cassandraPort;

    @Autowired
    private Cluster cluster;

    @Autowired
    private Session session;

    @Autowired
    private MappingManager mappingManager;

    @Bean
    public Session session(){
        return cluster.connect();
    }

    @Bean
    public Cluster cluster(){
        Cluster cluster = Cluster.builder().addContactPoint(cassandraContactpoints).withPort(cassandraPort)
                .build();
        cluster.getConfiguration().getCodecRegistry()
                .register(new EnumNameCodec<Status>(Status.class));

        return cluster;

    }



    @PreDestroy
    public void close(){
        if(session!=null){
            session.close();
        }
        if(cluster!=null){
            cluster.close();
        }

    }

    @Bean
    public MappingManager mappingManager(){
        return new MappingManager(session);
    }

    @Bean
    public Mapper<ProductPriceByTcin> productPriceByTcinDatastaxMapper(){
        return mappingManager.mapper(ProductPriceByTcin.class);
    }

    @Accessor
    public interface ProductPriceByTcinAccessor{

        @Query("SELECT * FROM product_ks.product_price_by_tcin WHERE tcin = ?")
        Result<ProductPriceByTcin> getByTcin(@Param("tcin") Long tcin);
    }

    @Bean
    public ProductPriceByTcinAccessor productPriceByTcinAccessor(){
        return mappingManager.createAccessor(ProductPriceByTcinAccessor.class);
    }

}
