package stmall.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import stmall.config.kafka.KafkaProcessor;
import stmall.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderPlaced'"
    )
    public void wheneverOrderPlaced_StartShipping(
        @Payload OrderPlaced orderPlaced
    ) {
        OrderPlaced event = orderPlaced;
        System.out.println(
            "\n\n##### listener StartShipping : " + orderPlaced + "\n\n"
        );

        // Comments //
        //1. 대한통운에 전문 발송
        // 2. 주문자에게 배송알림
        // 3. 배송 장부에도 키핑
        // ....

        // Sample Logic //
        Delivery.startShipping(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderModified'"
    )
    public void wheneverOrderModified_ModifyDelivery(
        @Payload OrderModified orderModified
    ) {
        OrderModified event = orderModified;
        System.out.println(
            "\n\n##### listener ModifyDelivery : " + orderModified + "\n\n"
        );

        // Sample Logic //
        Delivery.modifyDelivery(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
