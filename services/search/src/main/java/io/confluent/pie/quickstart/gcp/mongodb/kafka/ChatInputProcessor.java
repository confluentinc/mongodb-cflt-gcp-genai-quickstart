package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.key.ChatInputKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.query.ChatInputQuery;
import io.confluent.pie.quickstart.gcp.mongodb.entities.data.ChatInputWithData;
import io.confluent.pie.quickstart.gcp.mongodb.entities.Product;
import io.confluent.pie.quickstart.gcp.mongodb.repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Processing record: {}", record.value().sessionId());

        final ChatInputQuery query = record.value();

        final List<Product> products = productRepo.findProductsByVector(
                query.embeddings(),
                query.numberOfCandidate(),
                query.limit(),
                query.minScore());

        final ChatInputWithData chatInputWithData = new ChatInputWithData(
                query.sessionId(),
                products.stream().map(ChatInputWithData.Result::fromProduct).toList(),
                products.stream().map(Product::getSummary).collect(Collectors.joining("\n")),
                query.metadata()
        );

        context.forward(record.withValue(chatInputWithData));

        log.info("Forwarded record: {}", chatInputWithData.sessionId());
    }

    @Override
    public void close() {
    }
}
