package stmall.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import stmall.domain.*;
import stmall.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class DeliveryModified extends AbstractEvent {

    private Long id;
    private Long orderid;
    private String productname;
    private String userid;
    private String address;
    private String status;

    public DeliveryModified(Delivery aggregate) {
        super(aggregate);
    }

    public DeliveryModified() {
        super();
    }
}
//>>> DDD / Domain Event
