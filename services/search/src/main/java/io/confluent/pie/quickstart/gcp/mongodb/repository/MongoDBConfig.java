package io.confluent.pie.quickstart.gcp.mongodb.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoDBConfig {

    @Value("${mongodb.uri}")
    private String connectionString;

    @Value("${mongodb.database}")
    private String mongodbDatabase;


    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        
        MongoClientSettings clientSettings = MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString(this.connectionString))
                .codecRegistry(codecRegistry)
                .build();

        return MongoClients.create(clientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase(@Autowired MongoClient mongoClient) {
        return mongoClient.getDatabase(mongodbDatabase);
    }
}
