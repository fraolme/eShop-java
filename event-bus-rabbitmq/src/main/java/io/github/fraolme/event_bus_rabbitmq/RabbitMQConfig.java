package io.github.fraolme.event_bus_rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name:defaultQueueName}")
    private String queueName;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            // we need to set delivery mode to persistent
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue rabbitQueue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange rabbitExchange() {
        String brokerName = "eshop_event_bus";
        return new DirectExchange(brokerName);
    }

    @Bean
    MessageListenerAdapter rabbitListenerAdapter(EventProcessor processor) {
        MessageListenerAdapter mla = new MessageListenerAdapter(processor, "process");
        mla.setMessageConverter(null); // turn off message conversion to get the Message object with the metadata
        return mla;
    }

    @Bean
    SimpleMessageListenerContainer rabbitContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter,
                                             Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setQueueNames(queue.getActualName());
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
