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