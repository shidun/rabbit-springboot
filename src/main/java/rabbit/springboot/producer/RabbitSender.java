package rabbit.springboot.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import rabbit.springboot.entity.Order;

import java.util.Map;

/**
 * @author shidun
 */
@Component
@Slf4j
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            log.info("--------confirmCallback---------");
            log.info("correlationData:{}", correlationData);
            log.info("ack:{}", b);
            log.info("cause:{}", s);
            if (!b) {
                log.info("异常处理1111");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.info("--------returnCallback---------");
            log.info("message:{}", message);
            log.info("replyCode:{}", replyCode);
            log.info("replyText:{}", replyText);
            log.info("exchange:{}", exchange);
            log.info("routingKey:{}", routingKey);
        }
    };

    public void send(Object message, Map<String , Object> properties) throws Exception{
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message message1 = MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData cd = new CorrelationData();
        cd.setId("11111"); //id + 全局唯一
        rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", message1, cd);
    }

    public void sendOrder(Order order) throws Exception{
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData cd = new CorrelationData();
        cd.setId("21312321"); //id + 全局唯一
        rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", order, cd);
    }
}
