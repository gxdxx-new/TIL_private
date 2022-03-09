## JPA

#### JPA (Java Persistence API): 자바 ORM 기술에 대한 API 표준

#### ORM (Object Relational Mapping): 객체와 관계형 데이터베이스를 매핑해주는 것

#### Hibernate: JPA 인터페이스를 구현한 가장 대표적인 오픈소스

### JPA란?

- 객체를 db에 넣기 위해 SQL문을 작성해 저장하고, db에서 데이터를 꺼낼 때도 SQL문을 작성해야 한다.
- 결국 객체를 단순히 데이터 전달 목적으로 사용할 뿐 객체지향적으로 프로그래밍을 할 수 없게된다.
- 이러한 객체지향과 관계형 db간의 패러다임의 불일치를 해결하기 위해 나온 기술이 ORM이다.
  - 객체는 객체지향적으로, 데이터베이스는 데이터베이스대로 설계하고, ORM은 중간에서 2개를 매핑한다.

### JPA 사용시 장점

- 특정 db에 종속되지 않는다.
  - JPA는 추상화한 데이터 접근 계층을 제공하기 때문에 설정 파일에 어떤 db를 사용하는지 알려주기만 하면 된다.
- 객체지향적 프로그래밍
  - db 설계 중심의 패러다임에서 객체지향적으로 설계가 가능하다.
  - 비즈니스 로직에 집중할 수 있다.
- 생산성 향상
  - JPA에서는 테이블과 매핑된 클래스에 필드만 추가하면 쉽게 관리가 가능하다.
  - SQL문을 직접 작성하지 않고 객체를 사용하여 동작하기 때문에 유지보수에 좋고 재사용성도 증가한다.

### JPA 사용시 단점

- 복잡한 쿼리 처리
  - 통계 처리 같은 복잡한 쿼리를 사용할 경우는 SQL문을 사용하는게 나을 수 있다.
- 성능 저하 위험
  - 객체 간의 매핑 설계를 잘못했을 때 성능 저하가 발생할 수 있다.
  - 자동으로 생성되는 쿼리가 많기 때문에 개발자가 의도하지 않는 쿼리로 인해 성능이 저하될 수 있다.

## JPA 동작 방식

<img src="https://user-images.githubusercontent.com/35963403/156159151-d753cee8-da70-4db8-8492-d28456812f7c.png" width=600>

### 엔티티

- db의 테이블에 대응하는 클래스
- @Entity가 붙은 클래스는 JPA에서 관리한다.

### 엔티티 매니저 팩토리

- 엔티티 매니저 인스턴스를 관리하는 주체
- 애플리케이션 실행 시 한개만 만들어지고 사용자로부터 요청이 오면 엔티티 매니저를 생성한다.

### 엔티티 매니저

- 영속성 컨텍스트에 접근해 엔티티에 대한 db 작업을 제공
- 내부적으로 db 커넥션을 사용해 db에 접근한다.
  - DB Connection: db를 사용하기 위해 db와 애플리케이션이 통신을 할 수 있는 수단

  #### 엔티티 매니저의 메소드

  - find(): 영속성 컨텍스트에서 엔티티를 찾고, 없을 경우 db에서 데이터를 찾아 영속성 컨텍스트에 저장
  - persist(): 엔티티를 영속성 컨텍스트에 저장
  - remove(): 엔티티를 영속성 컨텍스트에서 삭제
  - flush(): 영속성 컨텍스트에서 저장된 내용을 db에 반영

### 영속성 컨텍스트

- 엔티티를 영구 저장하는 환경으로 엔티티 매니저를 통해 영속성 컨텍스트에 접근

### 엔티티 생명주기

<img src="https://user-images.githubusercontent.com/35963403/156159378-3c2dd28a-4ffd-4e0a-b346-de58bb5256a1.png" width=600>

|생명주기| 내용                                                                         |
|---|----------------------------------------------------------------------------|
|비영속 (new)| new 키워드를 통해 생성된 상태로 영속성 컨텍스트와 관련이 없는 상태                                    |
|영속 (managed)| - 엔티티가 영속성 컨텍스트에 저장된 상태로 영속성 컨텍스트에 의해 관리되는 상태<br/>- 트랜잭션 커밋 시점에 데이터베이스에 반영 |
|준영속 (detached)|영속성 컨텍스트에 엔티티가 저장되었다가 분리된 상태|
|삭제 (removed)|영속성 컨텍스트와 db에서 삭제된 상태|

#### 엔티티 db에 반영 과정

```java
Item item = new Item(); //1
item.setItemName("테스트 상품");

EntityManager em = entityManagerFactory.createEntityManager();  //2

EntityTransaction transaction = em.getTransaction();    //3
transaction.begin();

em.persist(item);   //4

transaction.commit();   //5

em.close(); //6
emf.close();    //6
```

1. 영속성 컨텍스트에 저장할 상품 엔티티를 생성, new 키워드를 통해 생성했기 때문에 비영속 상태
2. 엔티티 매니저 팩토리로부터 엔티티 매니저를 생성
3. 엔티티 매니저는 데이터 변경 시 데이터의 무결성을 위해 반드시 트랜잭션을 시작해야 한다.
4. 상품 엔티티를 영속성 컨텍스트에 저장, 아직 db에 반영되지 않은 상태
5. 트랜잭션을 db에 반영, 이때 영속성 컨텍스트에 저장된 상품 엔티티가 db에 INSERT되며 반영
6. 사용한 자원을 반환

## 영속성 컨텍스트 사용 시 이점

- 애플리케이션과 db 사이에 영속성 컨텍스트라는 중간 계층을 만들어서 버퍼링, 캐싱 등을 할 수 있다.

<img src="https://user-images.githubusercontent.com/35963403/156159855-02bfc0a6-97e8-4ae3-92ab-a63b21b77444.png" width=600>

### **1차 캐시**

- 영속성 컨텍스트에 존재
- Map<KEY, VALUE>로 저장
- entityManager.find() 메소드 호출 시 영속성 컨텍스트의 1차 캐시를 조회
- 엔티티가 존재하면 해당 엔티티를 반환, 없으면 db에서 조회 후 1차 캐시에 저장하고 반환

### **동일성 보장**

- 하나의 트랜잭션에서 같은 키값으로 영속성 컨텍스트에 저장된 엔티티 조회 시 같은 엔티티 조회를 보장
- 1차 캐시에 저장된 엔티티를 조회하기 때문에

<img src="https://user-images.githubusercontent.com/35963403/156159947-b7faa197-a432-4f36-947e-c47516cb6637.png" width=600>

### **트랜잭션을 지원하는 쓰기 지연**

- 영속성 컨텍스트에는 쓰기 지연 SQL 저장소가 존재
- entityManager.persist()를 호출하면 1차 캐시에 저장됨과 동시에 쓰기 지연 SQL 저장소에 SQL문이 저장된다.
- SQL을 계속 쌓아두다가 트랜잭션을 커밋하는 시점에 저장된 SQL문들이 flush되면서 db에 반영된다.

### **변경 감지**

- JPA는 1차 캐시에 DB에서 처음 불러온 엔티티의 스냅샷 값을 갖고 있음
- 1차 캐시에 저장된 엔티티와 스냅샷을 비교 후 변경 내용이 있으면 UPDATE SQL문을 쓰기 지연 SQL 저장소에 담아둔다.
- 커밋 시점에 변경 내용을 자동으로 반영한다. (따로 update문을 호출할 필요가 없다)

---

## 상품 엔티티 설계

#### 엔티티: db의 테이블에 대응하는 클래스

#### @Entity가 붙은 클래스는 JPA에서 관리한다.

```java
package com.gxdxx.shop.constant;

public enum ItemSellStatus {
  SELL, SOLD_OUT
}
```

- enum 클래스를 사용해 연관된 상수들을 모아둘 수 있다.
- enum에 정의한 타입만 값을 가지도록 컴파일 시 체크할 수 있다.

```java
package com.gxdxx.shop.entity;

import com.gxdxx.shop.constant.ItemSellStatus;
import com.gxdxx.shop.dto.ItemFormDto;
import com.gxdxx.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "item_id")
  private Long id;    //상품 코드

  @Column(nullable = false, length = 50)
  private String itemName;    //상품명

  @Column(nullable = false)
  private int price;  //가격

  @Column(nullable = false)
  private int stockQuantity;  //재고 수량

  @Lob
  @Column(nullable = false)
  private String itemDescription; //상품 상세 설명

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;  //상품 판매 상태
  
  private LocalDateTime registerTime;
  
  private LocalDateTime updateTime;
  
}
```

- 상품 정보로 상품코드, 가격, 상품명, 재고수량, 상품 상세 설명, 판매 상태를 갖는다.

### 엔티티 매핑 관련 어노테이션

|어노테이션|설명|
|---|---|
|@Entity|클래스를 엔티티로 선언|
|@Table|엔티티와 매핑할 테이블을 지정|
|@Id|테이블의 기본키에 사용할 속성을 지정|
|@GeneratedValue|키 값을 생성하는 전략 명시|
|@Column|필드와 컬럼 매핑|
|@Lob|BLOB, CLOB 타입 매핑|
|@CreationTimestamp|insert 시 시간 자동 저장|
|@UpdateTimestamp|update 시 시간 자동 저장|
|@Enumerated|enum 타입 매핑||
|@Transient|해당 필드 데이터베이스 매핑 무시|
|@Temporal|날짜 타입 매핑|
|@CreateDate|엔티티가 생성되어 저장될 때 시간 자동 저장|
|@LastModifiedDate|조회한 엔티티의 값을 변경할 때 시간 자동 저장|

#### CLOB와 BLOB의 의미

- CLOB: 문자형 댕대용량 파일을 외부 파일로 저장할 때 사용하는 데이터 타입
- BLOB: 멀티미디어 데이터(바이너리 데이터)를 외부 파일로 저장할 때 사용하는 데이터 타입

### @Column 어노테이션 추가 속성

|속성| 설명                                                             | 기본값       |
|---|----------------------------------------------------------------|-----------|
|name| 필드와 매핑할 컬럼의 이름 설정                                              | 객체의 필드 이름 |
|unique(DDL)| 유니크 제약 조건 설정                                                   ||
|insertable| insert 가능 여부                                                   | true      |
|updatable| update 가능 여부                                                   | true      |
|length| String 타입의 문자 길이 제약조건 설정                                       |255|
|nullable(DDL)| - null 값의 허용 여부 설정<br/>- false 설정 시 DDL 생성 시에 not null 제약조건 추가 ||
|columnDefinition|db 컬럼 정보 직접 기술||

### @GeneratedValue 어노테이션 기본키 생성 전략

|생성 전략|설명|
|---|---|
|GenerationType.AUTO (default)|JPA 구현체가 자동으로 생성 전략 결정|
|GenerationType.IDENTIFY|기본키 생성을 데이터베이스에 위임|
|GenerationType.SEQUENCE|데이터베이스 시퀀스 오브젝트를 이용한 기본키 생성|
|GenerationType.TABLE|키 생성용 테이블 사용|

- @GenerationType.AUTO를 사용하면 데이터베이스에 의존하지 않고 기본키를 할당한다.
- JPA 구현체가 IDENTIFY, SEQUENCE, TABLE 생성 전략 중 하나를 자동으로 선택하기 때문에 db가 변경되도 코드를 수정할 필요가 없다.

## 상품 리포지터리 설계

#### Spring Data JPA는 엔티티 매니저를 직접 이용한다.

#### 대신 Data Access Object의 역할을 하는 Repository 인터페이스를 설계한 후 사용한다.

#### 인터페이스만 설계하면 런타임 시점에 자바의 Dynamic Proxy를 이용해 객체를 동적으로 생성해준다.

```java
package com.gxdxx.shop.repository;

import com.gxdxx.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
}
```

- JpaRepository는 2개의 제네릭 타입을 사용한다.
- 첫 번쨰에 엔티티 타입 클래스를 넣고, 두 번째에 기본키 타입을 넣어준다.

### JpaRepository에서 지원하는 메소드 예시

#### JpaRepository는 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.

|메소드|기능|
|---|---|
|<S extends T> save(S entity)|엔티티 저장 및 수정|
|void delete(T entity)|엔티티 삭제|
|count()|엔티티 총 개수 반환|
|Iterable<T> findAll()|모든 엔티티 조회|

### 상품 저장 테스트

```java
package com.gxdxx.shop.repository;

...

@SpringBootTest // 1
@TestPropertySource(locations="classpath:application-test.properties")  // 2
class ItemRepositoryTest {
    
    @Autowired
    ItemRepository itemRepository;  // 3

    @Test   // 4
    @DisplayName("상품 저장 테스트")   // 5
    public void saveItemTest() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setStockQuantity(100);
        item.setItemDescription("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setRegisterTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }
}
```

1. 통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션. 실제 애플리케이션을 구동할 때처럼 모든 Bean을 IoC 컨테이너에 등록
2. 테스트 코드 실행 시 applicaiton-test.properties에 더 높은 우선순위 부여
3. ItemRepository를 사용하기 위해 @Autowired 어노테이션을 이용해 Bean 주입
4. 테스트할 메소드 위에 선언해 해당 메소드를 테스트 대상으로 지정
5. Junit5에 추가된 어노테이션. 테스트 코드 실행 시 @DisplayName에 지정한 테스트명이 노출

## 쿼리 메소드

#### find + (엔티티 이름) + By + 변수이름

```java
package com.gxdxx.shop.repository;

import com.gxdxx.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
    List<Item> findByItemName(String itemName);
    
    List<Item> findByItemNameOrItemDescription(String itemName, String itemDescription);
    
    List<Item> findByPriceLessThan(Integer price);
    
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    
}
```

- 엔티티명은 생략이 가능하다.
- 매개 변수로는 검색할 때 사용할 데이터를 넘겨준다.

## @Query 어노테이션

#### 쿼리 메소드는 간단한 처리를 할 때는 유용하지만 복잡한 쿼리를 다루기에는 적합하지 않다.

#### @Qeury는 JPQL이라는 객체지향 쿼리 언어를 통해 복잡한 쿼리를 처리해준다.

#### JPQL은 엔티티 객체를 대상으로 쿼리를 수행하고 특정 db의 sql에 의존하지 않는다.

```java
package com.gxdxx.shop.repository;

import com.gxdxx.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
    ...
  
    @Query("select i from Item i where i.itemDescription like " +
            "%:itemDescription% order by i.price desc")
    List<Item> findByItemDescription(@Param("itemDescription") String itemDescription); // 1
    
}
```

1. @Param 어노테이션을 이용해 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해준다.

## Querydsl

#### @Query 어노테이션을 이용하면 JPQL 문법으로 문자열을 입력하기 때문에 컴파일 시점에 에러를 발견할 수 없다.

#### Querydsl은 JPQL을 코드로 작성할 수 있도록 도와주는 빌더 API이다.

### Querydsl 장점

- 고정된 SQL문이 아닌 조건에 맞게 동적으로 쿼리를 생성할 수 있다.
- 비슷한 쿼리를 재사용할 수 있고 제약 조건 조립 및 가독성을 향상시킬 수 있다.
- 문자열이 아닌 자바 소스코드로 작성하기 때문에 컴파일 시점에 오류를 발견할 수 있다.
- IDE의 도움을 받아서 자동 완성 기능을 이용할 수 있기 때문에 생산성을 향상시킬 수 있다.

### JPAQueryFactory 이용한 상품 조회 테스트

```java
package com.gxdxx.shop.repository;

...

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {
    
    @Autowired
    EntityManager em;   // 1

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // 2
        QItem qItem = QItem.item;   // 3
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDescription.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());
  
        List<Item> itemList = query.fetch();    // 4
  
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }
    
}
```

1. 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입한다.
2. JPAQueryFactory를 이용해 쿼리를 동적으로 생성한다. 생성자의 파라미터로는 EntityManager 객체를 넣어준다.
3. Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용한다.
4. JPAQeury 메소드인 fetch를 이용해 쿼리 결과를 리스트로 반환한다. fetch() 실행 시점에 쿼리문이 실행된다.

#### JPAQuery 데이터 반환 메소드

|메소드|기능|
|---|---|
|List<T> fetch()|조회 결과 리스트 반환|
|T fetchOne|조회 대상이 1건인 경우 제네릭으로 지정한 타입 반환|
|T fetchFirst()|조회 대상 중 1건만 반환|

### QuerydslPredicateExecutor 이용한 상품 조회 테스트

```java
package com.gxdxx.shop.repository;

...

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
    
    ...

}
```

- Repository에 Predicate를 파라미터로 전달하기 위해 QueryDslPredicateExecutor 인터페이스를 상속 받는다.

```java
package com.gxdxx.shop.repository;

....

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {
    
    ...

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setStockQuantity(100);
            item.setItemDescription("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setRegisterTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setStockQuantity(0);
            item.setItemDescription("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setRegisterTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();   // 1
        QItem item = QItem.item;
        String itemDescription = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStatus = "SELL";

        booleanBuilder.and(item.itemDescription.like("%" + itemDescription + "%")); // 2
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);   // 3
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable); // 4
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }
    }

}
```

1. BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더이다. Predicate를 구현하고 있고 메소드 체인 형식으로 사용할 수 있다.
2. 필요한 상품을 조회하는데 필요한 "and" 조건을 추가한다.
3. 데이터를 페이징해 조회하도록 PageRequest.of() 메소드를 이용해 Pageble 객체를 생성한다. 조회할 페이지 번호, 한 페이지당 조회할 데이터 개수를 넣어준다.
4. QueryDslPredicateExecutor 인터페이스에서 정의한 findAll() 메소드를 이용해 조건에 맞는 데이터를 Page 객체로 받아온다.

#### QueryDslPredicateExecutor 인터페이스 정의 메소드

|메소드|기능|
|---|---|
|long count(Predicate)|조건에 맞는 데이터의 총 개수 반환|
|boolean exists(Predicate)|조건에 맞는 데이터 존재 여부 반환|
|Iterable findAll(Predicate)|조건에 맞는 모든 데이터 반환|
|Page<T> findAll(Predicate, Pageable)|조건에 맞는 페이지 데이터 반환|
|Iterable findAll(Predicate, Sort)|조건에 맞는 정렬된 데이터 반환|
|T findOne(Predicate)|조건에 맞는 데이터 1개 반환|

---

## 스프링 시큐리티

#### 스프링 시큐리티는 스프링 기반의 애플리케이션을 위한 보안 솔루션을 제공한다.

#### 인증: 요청에 대해 작업을 수행할 수 있는 주체인지 확인 ex) 로그인

#### 인가: 인증 과정 이후 발생, 권한 여부를 확인 ex) 관리자 페이지에 접근

### 스프링 시큐리티 설정

```java
package com.gxdxx.shop.config;

import com.gxdxx.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // 1
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // 2

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {  // 3
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/", "/members/**",
                                "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint((new CustomAuthenticationEntryPoint()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {  // 4
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

}
```

1. WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면 SpringSecurityFilterChain이 자동으로 포함된다.
2. WebSecurityConfigurerAdapter를 상속받아 메소드 오버라이딩을 통해 보안 설정을 커스터마이징할 수 있다.
3. http 요청에 대한 보안을 설정한다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다.
4. BCryptPasswordEncoder의 해시 함수를 이용해 비밀번호를 암호화하여 저장한다. 

## 회원 가입 기능 구현하기

```java
package com.gxdxx.shop.constant;

public enum Role {
    USER, ADMIN
}
```

- 일반 유저, 관리자를 구분하기 위해 enum 클래스에 모아놨다.

#### MemberFormDto

```java
package com.gxdxx.shop.dto;

...

@Getter @Setter
public class MemberFormDto {

  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String name;

  @NotEmpty(message = "이메일은 필수 입력 값입니다.")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
  @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
  private String password;

  @NotEmpty(message = "주소는 필수 입력 값입니다.")
  private String address;

}
```

- 회원 가입 화면으로부터 넘어오는 가입 정보를 담을 DTO를 생성한다.

#### javax.validation 어노테이션

|어노테이션|설명|
|@NotEmpty|NULL 체크 및 문자열의 경우 길이 0인지 검사|
|@NotBlank|Null 체크 및 문자열의 경우 길이 0 및 빈 문자열(" ") 검사|
|@Length(min=, max=)|최소, 최대 길이 검사|
|@Email|이메일 형식인지 검사|
|@Max(숫자)|지정한 값보다 작은지 검사|
|@Min(숫자)|지정한 값보다 큰지 검사|
|@Null|값이 NULL인지 검사|
|@NotNULL|값이 NULL이 아닌지 검사|

#### Member 엔티티

```java
package com.gxdxx.shop.entity;

...

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)  // 1
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)    // 2
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {   // 3
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  // 4
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

}
```

1. 회원은 이메일을 통해 유일하게 구분해야 하기 때문에, 동일한 값이 db에 들어올 수 없도록 unique 속성을 지정
2. "EnumType.STRING" 옵션으로 enum의 순서 상관없이 저장되도록 지정
3. Member 엔티티에 회원 생성하는 메소드를 만들어 관리
4. BCryptPasswordEncoder Bean을 파라미터로 받아 비밀번호를 암호화

#### MemberRepository

```java
package com.gxdxx.shop.repository;

...

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);   // 1

}
```

1. 회원 가입 시 중복된 회원이 있는지 검사하기 위해 이메일로 회원을 검사할 수 있도록 쿼리 메소드 작성

#### MemberService

```java
package com.gxdxx.shop.service;

...

@Service
@Transactional  // 1
@RequiredArgsConstructor    // 2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;    // 3

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {   // 4
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    

}
```

1. 비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션 선언하면 로직을 처리하다 에러 발생했을 때 변경된 데이터를 로직을 수행하기 이전 상태로 롤백
    - 테스트 클래스에 선언하면 테스트 실행 후 롤백 처리
2. final이나 @NonNull이 붙은 필드에 생성자를 생성해줌
3. 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록 가능하면 @Autowired 생략 가능
4. 이미 가입된 회원이면 IllegalStateException 예외 발생

#### MemberController

```java
package com.gxdxx.shop.controller;

...

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {    // 1

        if (bindingResult.hasErrors()) {    // 2
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); // 3
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMessage", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

}
```

1. 검증하려는 객체 앞에 @Valid 어노테이션을 선언하고 파라미터로 bindingResult 객체를 추가한다. 검사 후 결과는 bindingResult에 담긴다.
2. bindingResult.hasErrors()를 호출해 에러가 있으면 회원가입 페이지로 이동한다.

## 로그인 / 로그아웃 구현

#### UserDetailsService

- db에서 회원 정보를 가져오는 역할을 한다.
- ladUserByUsername() 메소드는 회원 정보를 조회해 사용자의 정보와 권한을 갖는 UserDetils 인터페이스를 반환한다.

#### UserDetails

- 회원의 정보를 담기 위해 사용하는 인터페이스이다.
- 직접 구현하거나 스프링 시큐리티에서 제공하는 User 클래스를 사용한다.
- User 클래스는 UserDetails 인터페이스를 구현하고 있는 클래스이다.

```java
package com.gxdxx.shop.service;

...

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {  // 1
    
    ...

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  // 2
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()   // 3
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
```

1. MemberService가 UserDetailsService를 구현
2. UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩, 로그인할 유저의 이메일을 파라미터로 전달받음
3. UserDetails를 구현하고 있는 User 객체를 반환

#### SecurityConfig

```java
package com.gxdxx.shop.config;

...

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")    // 1
                .defaultSuccessUrl("/") // 2
                .usernameParameter("email") // 3
                .failureUrl("/members/login/error") // 4
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 5
                .logoutSuccessUrl("/"); // 6

        http.authorizeRequests()    // 7
                .mvcMatchers("/", "/members/**",
                                "/item/**", "/images/**").permitAll()   // 8
                .mvcMatchers("/admin/**").hasRole("ADMIN")  // 9
                .anyRequest().authenticated();  // 10

        http.exceptionHandling()
                .authenticationEntryPoint((new CustomAuthenticationEntryPoint()));  // 11
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // 12
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());    // 13
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); // 14
    }

}
```

1. 로그인 페이지 URL 설정
2. 로그인 성공시 이동할 URL
3. 로그인 시 사용할 파라미터 이름으로 email 지정
4. 로그인 실패 시 이동할 URL
5. 로그아웃 URL
6. 로그아웃 성공시 이동할 URL
7. 시큐리티 처리에 HttpServletRequest를 이용
8. permitAll()을 통해 모든 사용자가 인증(로그인) 없이 해당 경로에 접근할 수 있도록 설정
9. /admin 으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 가능하도록 설정
10. 8, 9에서 설정해준 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정
11. 인증되지 않은 사용자가 리소스에 접근했을 때 수행되는 핸들러를 등록
12. 스프링 시큐리티에서 인증은 AuthenticationManager를 통해 이루어지는데 AuthenticationManagerBuilder가 AuthenticationManager를 생성
13. userDetailsService를 구현하고 있는 객체로 memberService를 지정하고, 비밀번호 암호화를 위해 passwordEncoder를 지정
14. static 디렉터리 하위 파일은 인증 무시하도록 설정
      - antMatchers, mvcMatchers 차이
        - antMatchers(”/info”)은 /info URL과 매핑 되지만 mvcMatchers(”/info”)은 /info/, /info.html 이 매핑 가능

#### CustomAuthenticationEntryPoint

```java
package com.gxdxx.shop.config;

...

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}
```

- 인증되지 않은 사용자가 리소스를 요청할 경우 "Unauthorized" 에러를 발생하도록 AuthenticationEntryPoint 인터페이스를 구현

### 로그인 / 로그아웃 테스트

```java
package com.gxdxx.shop.controller;

...

@SpringBootTest
@AutoConfigureMockMvc   // 1
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;    // 2

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) { // 3
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("경북 경산시 대동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")   // 4
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());  // 5
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}
```

1. MockMvc 테스트를 위해 @AutoConfigureMockMvc 어노테이션을 선언
2. MockMvc 클래스를 이용해 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체 생성, 이를 이용해 웹 브라우저에서 요청을 하는 것처럼 테스트 가능
3. 로그인 전 회원 등록 메소드
4. userParameter() 를 이용해 이메일을 아이디로 세팅하고 로그인 URL에 요청
5. 로그인이 성공해 인증되었으면 테스트 통과

#### 유저 접근 권한 테스트

```java
package com.gxdxx.shop.controller;

...

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 관리자 접근 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")  // 1
    public void itemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))  // 2
                .andDo(print()) // 3
                .andExpect(status().isOk());    // 4
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "user", roles = "USER")
    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden()); // 5
    }

}
```

1. 현재 회원의 이름이 admin, role이 ADMIN인 유저가 로그인된 상태로 테스트를 할 수 있도록 해주는 어노테이션
2. 상품 등록 페이지에 get 요청을 보냄
3. 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력
4. 응답 상태 코드가 정상인지 확인
5. 상품 등록 페이지 진입 요청 시 Forbidden 예외가 발생하면 테스트가 성공적으로 통과