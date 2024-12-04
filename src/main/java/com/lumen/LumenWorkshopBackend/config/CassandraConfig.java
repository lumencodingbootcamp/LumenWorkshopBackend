package com.lumen.LumenWorkshopBackend.config;

import java.net.InetSocketAddress;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.Metadata;

@Configuration
public class CassandraConfig {

    @Bean
    public CqlSession cqlSession() {
        InetSocketAddress contactPoint = new InetSocketAddress("20.198.176.160", 9042);
        return CqlSession.builder()
                         .addContactPoint(contactPoint)
                         .withLocalDatacenter("datacenter1") // Your datacenter name
                         .build();
    }

    @Bean
    public MappingCassandraConverter mappingCassandraConverter(CassandraMappingContext cassandraMappingContext) {
        return new MappingCassandraConverter(cassandraMappingContext);
    }

    @Bean
    public CassandraTemplate cassandraTemplate(CqlSession cqlSession, MappingCassandraConverter converter) {
    	System.out.println(cqlSession.getKeyspace());
    	Metadata metadata = cqlSession.getMetadata();
    	System.out.println(metadata);
        return new CassandraTemplate(cqlSession, converter);
    }
}
