package com.develop.controller;

import com.develop.broker.publisher.RabbitMQJsonProducer;
import com.develop.broker.publisher.RabbitMQProducer;
import com.develop.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    private final RabbitMQProducer rabbitMQProducer;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    public MessageController(RabbitMQProducer rabbitMQProducer, RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    /**
     * Sends a message to RabbitMQ.
     *
     * @param  message  the message to send
     * @return          the response entity with the message sent confirmation
     */
    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        rabbitMQProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ: " + message);
    }

    /**
     * Sends a JSON message to RabbitMQ.
     *
     * @param  request the UserRequest object containing the message
     * @return         ResponseEntity with a success message
     */
    @PostMapping("/json/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody UserRequest request) {
        rabbitMQJsonProducer.sendMessage(request);
        return ResponseEntity.ok("Json message sent to RabbitMQ: " + request.email());
    }
}
