package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatInputKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatInputQuery;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatInputWithData;
import io.confluent.pie.quickstart.gcp.mongodb.entities.Product;
import io.confluent.pie.quickstart.gcp.mongodb.repository.ProductRepo;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

import java.util.List;
import java.util.stream.Collectors;

public class ChatInputProcessor implements Processor<ChatInputKey, ChatInputQuery, ChatInputKey, ChatInputWithData> {
    private ProcessorContext<ChatInputKey, ChatInputWithData> context;
    private final ProductRepo productRepo;

    public ChatInputProcessor(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void init(ProcessorContext<ChatInputKey, ChatInputWithData> context) {
        this.context = context;
    }

    @Override
    public void process(Record<ChatInputKey, ChatInputQuery> record) {

        final ChatInputQuery query = record.value();

        final List<Product> products = productRepo.findProductsByVector(
                query.embeddings(),
                query.numberOfCandidate(),
                query.limit(),
                query.minScore());

        products.stream()
                .map(product -> {
                    return new ChatInputWithData(
                            query.requestId(),
                            products.stream().map(ChatInputWithData.Result::fromProduct).toList(),
                            products.stream().map(Product::getSummary).collect(Collectors.joining("\n")),
                            query.metadata()
                    );
                })
                .forEach(inputWithData -> context.forward(record.withValue(inputWithData)));
    }

    @Override
    public void close() {
    }
}
