# 도메인 분석 설계

## 요구사항 분석

<img src="https://user-images.githubusercontent.com/35963403/154045315-9e58bac1-edac-44dd-bbb0-644e68085700.PNG" width="500">

**기능 목록**

- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록
  - 상품 수정
  - 상품 조회
- 주문 기능
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
- 기타 요구사항
  - 상품은 재고 관리가 필요하다.
  - 상품의 종류는 도서, 음반, 영화가 있다.
  - 상품을 카테고리로 구분할 수 있다.
  - 상품 주문시 배송 정보를 입력할 수 있다.

## 도메인 모델과 테이블 설계

<img src="https://user-images.githubusercontent.com/35963403/154045327-da5c5109-ebbe-4c57-8c4c-ce8150ed67ca.PNG" width="500">

#### 회원, 주문의 관계

- 회원은 여러 상품을 주문할 수 있다.

#### 주문, 상품의 관계

- 한 번 주문할 때 여러 상품을 선택할 수 있다.
- 같은 상품도 여러 번 주문될 수 있다.
- 다대다 관계지만 주문상품이라는 연결 엔티티를 추가해서 일대다, 다대다 관계로 풀어낸다.
  - 다대다 관계는 사용하지 않는게 좋다.

### 회원 엔티티 분석

<img src="https://user-images.githubusercontent.com/35963403/154045329-3582ee39-4c49-4f6c-94a2-6d173253f958.PNG" width="500">

#### 회원 (Member)

- 이름과 임베디드 타입인 주소(Address), 주문(orders) 리스트를 가진다.
- 실무에서는 회원이 주문을 참조하지 않고, 주문이 회원을 참조하는 것으로 충분하다.

#### 주문 (Order)

- 한 번 주문시 여러 상품을 주문할 수 있으므로 주문과 주문상품(OrderItem)은 일대다 관계이다.
- 상품을 주문한 회원, 배송 정보, 주문 날짜, 주문 상태를 가지고 있다.
- 주문 상태는 열겨형으로 주문(ORDER), 취소(CANCEL)을 표현한다.

#### 주문상품 (OrderItem)

- 주문한 상품 정보와 주문 금액(orderPrice), 주문 수량(count) 정보를 가지고 있다.

#### 상품 (Item)

- 이름, 가격, 재고수량(stockQuantity)을 가지고 있다.
- 상품의 종류로는 도서, 음반, 영화가 있다.

#### 카테고리 (Category)

- 상품과 다대다 관계를 맺는다.
- parent, child로 부모, 자식 카테고리를 연결한다.

주소 (Address)

- 값 타입(임베디드 타입)이다.
- 회원과 배송에서 사용한다.

### 회원 테이블 분석

<img src="https://user-images.githubusercontent.com/35963403/154045333-52487f7a-6375-4760-81b5-41060ac05af1.PNG" width="500">

#### MEMBER

- 회원 엔티티의 Address 임베디드 타입 정보가 회원 테이블에 그대로 들어간다.

#### ORDERS

- 데이터베이스가 order by로 인식할 수 있어서 관례상 ORDERS로 설정한다.

#### ITEM

- 싱글테이블로 만들고 DTYPE 컬럼으로 타입을 구분한다.

### 연관관계 매핑 분석

#### 연관관계 주인 

- 주인만이 데이터베이스 연관관계와 매핑되고 외래키를 관리할 수 있다.
- 주인이 아니면 읽기만 가능하다.
- jpa에서는 FK값을 누가 바꿀지 둘중 하나만 선택하게 되어있다.
- 객체는 변경포인트가 두개인데 테이블은 FK만 변경하면 된다.
- 연관관계 주인은 FK와 가까운 곳으로 정하면 된다.

#### 회원과 주문

- 일대다, 다대일의 양방향 연관관계여서 연관관계의 주인을 설정해야 한다.
- 외래키가 있는 주문을 주인으로 정하는게 좋다.
- Order.member를 ORDERS.MEMBER_ID 외래키와 매핑한다.

   ```java
  // Order
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "member_id") // FK가 member_id
   private Member member;
   ```

   ```java
  // Member
   @OneToMany(mappedBy = "member") // order 테이블에 있는 멤버 필드에 의해 매핑, 읽기 전용이 됨
   private List<Order> orders = new ArrayList<>();
   ```

#### 주문상품과 주문

- 다대일, 일대다 양방향 연관관계다.
- 외래키가 주문상품에 있기 때문에 주문상품이 연관관계 주인이 된다.
- OrderItem.order를 ORDER_ITEM.ORDER_ID 외래키와 매핑한다.

   ```java
   // OrderItem
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "order_id")
   private Order order;
   ```

#### 주문상품과 상품

- 다대일, 일대다 양방향 연관관계다.
- OrderItem.item을 ORDER_ITEM.ITEM_ID 외래키와 매핑한다.

   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "item_id")
   private Item item;
   ```

#### 주문과 배송

- 일대일 양방향 관계다.
- Order.delivery를 ORDERS.DELIVERY_ID 외래키와 매핑한다.

   ```java
   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "delivery_id")
   private Delivery delivery;
   ```

#### 카테고리와 상품

- @ManyToMany를 사용해서 매핑한다.

## 엔티티 클래스 개발

- 실무에서는 Getter는 열어두고, Setter는 꼭 필요한 경우에만 사용하는 것이 좋다.

### 회원 엔티티

```java
package com.gxdxx.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;
    
    @OneToMany(mappedBy = "member") // order 테이블에 있는 멤버 테이블에 의해 매핑, 읽기 전용이 됨
    private List<Order> orders = new ArrayList<>();
    
}
```

### 주문 엔티티

```java
package com.gxdxx.jpa.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

   @Id
   @GeneratedValue
   @Column(name = "order_id")
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "member_id") // FK가 member_id
   private Member member;

   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
   // order을 persist하면 orderItems 컬렉션에 있는 것들이 같이 persist 된다. 각각 persist 하지 않아도 된다.
   private List<OrderItem> orderItems = new ArrayList<>();

   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "delivery_id")
   private Delivery delivery;

   private LocalDateTime orderDate;

   @Enumerated(EnumType.STRING)
   private OrderStatus status; // 주문상태 [ORDER, CANCEL]

   //==연관관계 메서드==//
   public void setMember(Member member) {
      this.member = member;
      member.getOrders().add(this);
   }

   public void addOrderItem(OrderItem orderItem) {
      orderItems.add(orderItem);
      orderItem.setOrder(this);
   }

   public void setDelivery(Delivery delivery) {
      this.delivery = delivery;
      delivery.setOrder(this);
   }
   
}
```

#### cascade

- cascade는 mappedBy, 양방향 등등과 전혀 관계가 없습니다.
- 복잡하게 다른 것과 엮어서 고민하지 않으셔도 됩니다^^
- 단순하게 A -> B 관계가 cascade로 되어 있으면 A엔티티를 PERSIST할 때 B 엔티티도 연쇄해서 함께 PERSIST 해버린다고 이해하시면 됩니다.

#### @Enumerated(EnumType.STRING)

- ORDINAL과 STRING이 있는데 ORDINAL을 할 경우 중간에 다른 값이 추가되거나 삭제되면 숫자값이 변경되기 때문에 STRING을 사용하는게 좋다.

### 주문 상태

```java
package com.gxdxx.jpa.domain;

public enum OrderStatus {
    ORDER, CANCEL
}
```

### 주문상품 엔티티

```java
package com.gxdxx.jpa.domain;

import com.gxdxx.jpa.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "order_item")
public class OrderItem {

   @Id
   @GeneratedValue
   @Column(name = "order_item_id")
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "item_id")
   private Item item;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "order_id")
   private Order order;

   private int orderPrice; // 주문 가격
   private int count;  // 주문 수량
   
}
```

### 상품 엔티티

```java
package com.gxdxx.jpa.domain.item;

import com.gxdxx.jpa.domain.Category;
import com.gxdxx.jpa.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

   @Id
   @GeneratedValue
   @Column(name = "item_id")
   private Long id;

   private String name;
   private int price;
   private int stockQuantity;

   @ManyToMany(mappedBy = "items")
   private List<Category> categories = new ArrayList<>();

}
```

### 상품 - 도서 엔티티

```java
package com.gxdxx.jpa.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;

}
```

### 배송 엔티티

```java
package com.gxdxx.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)    // ORDINAL은 숫자로 들어가는데 중간에 다른 상태가 생기면 문제 생김
    private DeliveryStatus status;  // READY, COMP
   
}
```

### 배송 상태

```java
package com.gxdxx.jpa.domain;

public enum DeliveryStatus {
    READY, COMP
}
```

### 카테고리 엔티티

- @ManyToMany 는 중간 테이블( CATEGORY_ITEM )에 컬럼을 추가할 수 없고, 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다.
- 중간 엔티티( CategoryItem ) 를 만들고 @ManyToOne , @OneToMany 로 매핑해서 사용해야 한다.

### 주소 값 타입

```java
package com.gxdxx.jpa.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
    
}
```

- @Embeddable, @Embedded 로 값 타입을 사용할 수 있다.
- 값 타입은 변경 불가능하게 설계해야 한다.
- @Setter를 사용하지 않고 생성자에서 값을 초기화하는게 좋다.
- 임베디드 타입은 자바 기본 생성자를 public 또는 protected 로 설정해야 하기 때문에 protected 로 설정한다.
- JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문이다.

## 엔티티 설계시 주의점

### 엔티티에는 가급적 Setter를 사용하지 않는다.

### 모든 연관관계는 지연로딩(LAZY)으로 설정한다.

- 즉시로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다.
- JPQL을 실행할 때 N + 1 문제가 발생한다.
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용한다.
- @XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 한다.

### 컬렉션은 필드에서 초기화한다.

- 필드에서 초기화하면 null 문제에서 안전하게 된다.
- 하이버네이트는 엔티티를 영속화 할 때, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 
- 만약 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.

```
Member member = new Member();
System.out.println(member.getOrders().getClass());
em.persist(team);
System.out.println(member.getOrders().getClass());

//출력 결과
class java.util.ArrayList
class org.hibernate.collection.internal.PersistentBag
```

# 회원 도메인 개발

## 회원 리포지토리 개발

```java
package com.gxdxx.jpa.repository;

import com.gxdxx.jpa.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // 컴포넌트 스캔으로 인해 자동으로 스프링 빈으로 관리됨
@RequiredArgsConstructor
public class MemberRepositoryOld {

    private final EntityManager em;   // 스프링이 엔티티 매니져를 만들어서 주입해준다.

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);   // id에 맞는 member를 찾아서 반환해준다.
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)   // JPQL은 엔티티 객체를 대상으로 쿼리를 실행한다.
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)  // 파라미터를 바인딩한다.
                .setParameter("name", name)
                .getResultList();
    }
}
```

#### @Repository
- 컴포넌트 스캔에 의해 자동으로 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환한다.

#### @PersistenceContext
- 스프링 프레임워크는 실제 EntityManager를 주입하는 것이 아니라, 실제 EntityManager를 연결해주는 가짜 EntityManager를 주입해둔다.
- 그리고 이 EntityManager를 호출하면, 현재 데이터베이스 트랜잭션과 관련된 실제 EntityManager를 호출해줍니다.

#### JPQL
- sql은 테이블을 대상으로 쿼리를 하지만, jpql은 엔티티 객체를 대상으로 쿼리를 한다.

## 회원 서비스 개발

```java
package com.gxdxx.jpa.service;

import com.gxdxx.jpa.domain.Member;
import com.gxdxx.jpa.repository.MemberRepository;
import com.gxdxx.jpa.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) 
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  /**
   * 회원 가입
   */
  @Transactional
  public Long join(Member member) {
    validateDuplicateMember(member);    // 중복 회원 검증
    memberRepository.save(member);  // db에 저장되기 전이여도 id값이 생성됨
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  // 회원 전체 조회
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  public Member findOne(Long memberId) {
    return memberRepository.findById(memberId).get();
  }

  @Transactional  // jpa가 id로 영속성 컨텍스트나 db에서 찾아오고 member에 반환해준다. 영속 상태가 된 member의 name을 바꿔주고 종료되면 스프링 AOP가 동작하면서 @Transactional 어노테이션에 의해서 커밋이 되고 jpa가 플러쉬한다.
  public void update(Long id, String name) {
    Member member = memberRepository.findById(id).get();
    member.setName(name);
  }
  
}
```

#### @RequiredArgsConstructor
- final이 붙은 필드만 가지고 생성자를 만들어준다.

#### @Transactional (스프링 어노테이션)
- 모든 데이터 변경이나 로직은 트랜잭션 안에서 이루어져야 한다.
- 데이터의 변경이 없는 읽기 전용 메서드에 readOnlye=true를 하면 영속성 컨텍스트를 플러시 하지 않아서 약간의 성능 향상이 있다.
- 실행 중간에 오류가 발생하면 두 로직이 함께 롤백되어야 하기 때문에 서비스 계층에 둔다.

  ```
  Service {
  
    나의돈을 1000원 제거
  
    상대방에게 돈을 1000원 추가
  
  }
  ```

#### @Autowired
- 생성자가 하나만 있는 경우엔 @Autowired가 없어도 스프링이 자동으로 주입해준다.

```
/**
* 회원 가입
**/
@Transactional
public Long join(Member member) {
    validateDuplicateMember(member);    // 중복 회원 검증
    memberRepository.save(member);  // db에 저장되기 전이여도 id값이 생성됨
    return member.getId();
}
```

- memberRepository.save(member); 에서 em.persist()를 하면 영속성 컨텍스트에 member 객체가 올라간다.
- 그 때 영속성 컨텍스트의 key값이 member 객체의 PK(id값)가 된다.
- @GeneratedValue로 인해 항상 id값이 생성되어 있는게 보장되기 때문에 아직 db에 들어간 시점이 아니여도 id값이 생성된다.
- 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전하다

## 회원 기능 테스트

#### 테스트 요구사항

- 회원가입을 성공해야 한다.
- 회원가입 할 때 중복된 이름이 있으면 예외가 발생해야 한다.

```java
package com.gxdxx.jpa.service;

import com.gxdxx.jpa.domain.Member;
import com.gxdxx.jpa.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {


    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("nam");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(saveId).get()); // 같은 트랙잭션 내에서 id값이 같으면 같은 영속성 컨텍스트에서 하나로 관리된다.
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("nam");

        Member member2 = new Member();
        member2.setName("nam");

        //when
        memberService.join(member1);
        memberService.join(member2);    // 예외가 발생해야 한다.

        //then
        fail("예외가 발생해야 한다.");
    }
}
```

#### @RunWith(SpringRunner.class)
- 스프링과 테스트를 통합한다.

#### @SpringBootTest
- 스프링 부트를 띄우고 테스트한다.
- 없으면 @Autowired가 다 실패한다.

#### @Transactional
- 같은 트랙잭션에서 같은 엔티티(같은 id값)이면 영속성 컨텍스트에서 하나로 관리된다.
- 데이터베이스 트랜잭션이 커밋하는 순간 플러시가 되면서 jpa 영속성 컨텍스트에 있는 객체가 db에 insert된다.
- 각각의 테스트를 실행할 때마다 트랜잭션을 시작한다.
- 테스트 케이스에선 insert 되지않고 롤백을 한다.(영속성 컨텍스트 flush를 하지 않는다.)



