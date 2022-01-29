package com.gxdxx.jpa.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {   // orderRepository가 화면에 의존하지 않고 엔티티만 조회하도록 위치 변경

    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos() {   // jpa는 엔티티나, 밸류 오브젝트만 반환 가능, dto를 반환하려면 new operation을 해야함
        return em.createQuery(
                        "select new com.gxdxx.jpa.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

}
