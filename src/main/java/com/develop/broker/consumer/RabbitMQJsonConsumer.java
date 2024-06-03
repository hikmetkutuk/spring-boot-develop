package com.develop.broker.consumer;

import com.develop.dto.UserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQJsonConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.json-queue.name}")
    public void consumeJsonMessage(UserRequest request) {
        log.info("Json message received -> {}", request.email());
    }
}
