package ru.chubaka.springcourse.kafka;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.chubaka.springcourse.models.Person;

@EnableBinding(Sink.class)
public class Consumer {
    //@KafkaListener(topics = "test_topic_0", groupId = "group_id")
    @StreamListener(Sink.INPUT)
    public void consumePerson(@Payload Person person) throws InterruptedException {
        //Thread.sleep(15000);
        System.out.println(person);
    }

}
