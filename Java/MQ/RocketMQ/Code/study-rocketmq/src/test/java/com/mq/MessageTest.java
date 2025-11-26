package com.mq;


import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.consumer.PushConsumerBuilder;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.junit.Test;

import java.time.Duration;

public class MessageTest {

    /**
     * 消息内部属性
     */
    @Test
    public void testMessage() {
        MessageBuilder messageBuilder = new MessageBuilderImpl();
        messageBuilder
                .setKeys("ysj_key") // 设置消息的key
                .setTag("ysj_tag") // 设置消息的tag
                .setBody("ysj_msg".getBytes()) // 设置消息内容
                .setTopic("ysj_topic") // 设置消息的topic
                // .setMessageGroup() // 设置消息的group
                // .setDeliveryTimestamp() // 设置消息的deliveryTimestamp
                // .setLiteTopic() // 创建lite topic
                .build();
    }

    /**
     * 创建Provider的过程
     */
    @Test
    public void testProvider() throws ClientException {
        // 创建一个providerBuilder
        ClientServiceProvider clientServiceProvider = ClientServiceProvider.loadService();
        ProducerBuilder producerBuilder = clientServiceProvider.newProducerBuilder();

        // 创建凭证
        SessionCredentialsProvider credentials = new StaticSessionCredentialsProvider("accessKey", "secretKey");

        // 创建一个客户端配置
        ClientConfiguration build = ClientConfiguration.newBuilder()
                .setEndpoints("192.168.147.101:9876") // 设置接入点
                .setRequestTimeout(Duration.ofSeconds(3)) // 设置请求超时时间
                .setCredentialProvider(credentials) // 设置凭证提供者
                .enableSsl(false) // SSL
                .build();

        // 2. 配置属性
        Producer producer = producerBuilder
                .setTopics("test", "test1") // 预绑定主题
                .setMaxAttempts(3) // 最大重试次数
                .setClientConfiguration(build) // 设置客户端配置
                // .setTransactionChecker() // 设置事务检查
                .build();

    }

    /**
     * 创建Consumer的过程
     */
    @Test
    public void testConsumer() throws ClientException {

        PushConsumerBuilder pushConsumerBuilder = ClientServiceProvider.loadService().newPushConsumerBuilder();

        // 创建凭证
        SessionCredentialsProvider credentials = new StaticSessionCredentialsProvider("accessKey", "secretKey");

        // 创建一个客户端配置
        ClientConfiguration configuration = ClientConfiguration.newBuilder()
                .setEndpoints("192.168.147.101:9876") // 设置接入点
                .setRequestTimeout(Duration.ofSeconds(3)) // 设置请求超时时间
                .setCredentialProvider(credentials) // 设置凭证提供者
                .enableSsl(false) // SSL
                .build();

        PushConsumer consumer = pushConsumerBuilder
                .setClientConfiguration(configuration) // 配置客户端
                .setConsumerGroup("ysj_consumer_group") // 设置消费者组
                .setConsumptionThreadCount(5) // 设置消费者线程数
                .setMaxCacheMessageCount(100) // 设置缓存消息数量
                .setMessageListener(messageView -> null).build(); // 设置监听器



    }
}
