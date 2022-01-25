package com.gxdxx.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)    // ORDINAL은 숫자로 들어가는데 중간에 다른 상태가 생기면 문제 생김
    private DeliveryStatus status;  // READY, COMP
}