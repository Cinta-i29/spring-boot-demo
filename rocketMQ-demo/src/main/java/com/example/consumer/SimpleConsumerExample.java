package com.example.consumer;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author howe
 */
@Component
public class SimpleConsumerExample {
    // 接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8081;xxx:8081。
    @Value("${rocketmq.proxy.addr}")
    private String proxyAddr;

    public void consumer() throws ClientException {
        // 消费示例：使用 SimpleConsumer 消费普通消息，主动获取消息处理并提交。
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(proxyAddr)
                .build();
        // 订阅消息的过滤规则，表示订阅所有Tag的消息。
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        // 为消费者指定所属的消费者分组，Group需要提前创建。
        String consumerGroup = "YourConsumerGroup";
        // 指定需要订阅哪个目标Topic，Topic需要提前创建。
        String topic = "TestTopic";
        SimpleConsumer simpleConsumer = provider.newSimpleConsumerBuilder()
                // 设置接入点
                .setClientConfiguration(clientConfiguration)
                // 设置消费者分组。
                .setConsumerGroup(consumerGroup)
                // 设置预绑定的订阅关系。
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                // 设置从服务端接受消息的最大等待时间(没有消息可用时等待十秒)
                .setAwaitDuration(Duration.ofSeconds(10))
                .build();
        try {
            // SimpleConsumer 需要主动获取消息，并处理。
            List<MessageView> messageViewList = simpleConsumer.receive(10, Duration.ofSeconds(30));
            messageViewList.forEach(messageView -> {
                // 处理消息并返回消费结果。
                // .......
                System.out.println("messageView => " + messageView);
                // 消费处理完成后，需要主动调用 ACK 提交消费结果。
                try {
                    simpleConsumer.ack(messageView);
                } catch (ClientException e) {
                    System.err.println("Failed to ack message, messageId={" + messageView.getMessageId() + "}" + e);
                }
            });
        } catch (ClientException e) {
            // 如果遇到系统流控等原因造成拉取失败，需要重新发起获取消息请求。
            System.err.println("Failed to receive message" + e);
        }
    }
}
