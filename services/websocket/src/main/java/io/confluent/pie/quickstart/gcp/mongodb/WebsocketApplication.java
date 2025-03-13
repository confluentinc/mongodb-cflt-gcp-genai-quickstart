package io.confluent.pie.quickstart.gcp.mongodb;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) throws IOException {

//        InputStream is = new FileInputStream("/Users/pascal/projects/quickstarts/mongodb-cflt-gcp-genai-quickstart/infrastructure/modules/gcp/data/Ibuprofen.json");
//
//        MappingIterator<JsonNode> reader = new ObjectMapper().readerFor(JsonNode.class)
//                .with(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
//                .readValues(is);
//        while (reader.hasNext()) {
//            JsonNode record = reader.next();
//            if (record == null) {
//                break;
//            }
//
//            System.out.println(record);
//        }


        SpringApplication.run(WebsocketApplication.class, args);
    }

}
