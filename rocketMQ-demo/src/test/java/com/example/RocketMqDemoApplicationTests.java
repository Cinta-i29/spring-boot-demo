package com.example;

import com.example.consumer.PushConsumerExample;
import com.example.consumer.SimpleConsumerExample;
import com.example.producer.MessageTypeList;
import com.example.producer.TransactionMessage;
import org.apache.rocketmq.client.apis.ClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
@Component
class RocketMqDemoApplicationTests {


    @Autowired
    MessageTypeList messageTypeList;

    @Autowired
    PushConsumerExample pushConsumer;

    @Autowired
    SimpleConsumerExample simpleConsumer;

    @Autowired
    TransactionMessage transactionMessage;

    @Test
    public void test() throws ClientException, InterruptedException {
        transactionMessage.sendMessage();
    }


}
