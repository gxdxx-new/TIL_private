# 객체 지향 설계와 스프링

## 스프링 프레임워크
- 핵심 기술: 스프링 DI 컨테이너, AOP, 이벤트, 기타
- 웹 기술: 스프링 MVC, 스프링 WebFlux
- 데이터 접근 기술: 트랜잭션, JDBC, ORM 지원, XML 지원
- 기술 통합: 캐시, 이메일, 원격접근, 스케줄링
- 테스트: 스프링 기반 테스트 지원
- 언어: 코틀린, 그루비

## 스프링 부트
- 스프링을 편리하게 사용할 수 있도록 지원, 최근에는 기본으로 사용
- 단독으로 실행할 수 있는 스프링 애플리케이션을 쉽게 생성
- Tomcat 같은 웹 서버를 내장해서 별도의 웹 서버를 설치하지 않아도 됨
- 손쉬운 빌드 구성을 위한 starter 종속성 제공
    - 관련 종속성을 모두 가져옴
- 스프링과 3rd party(외부) 라이브러리 자동 구성
- 메트릭, 상태 확인, 외부 구성 같은 프로덕션 준비 기능 제공
- 관례에 의한 간결한 설정

## 스프링 뜻

- 스프링 DI 컨테이너 기술
    - 스프링 빈을 관리
- 스프링 프레임워크
- 스프링 부트, 스프링 프레임워크 등을 모두 포함한 스프링 생태계

## 스프링의 핵심

- 자바 언어 기반의 프레임워크
- 자바 언어의 가장 큰 특징: **객체 지향 언어**
- 스프링은 객체 지향 언어가 가진 강력한 특징을 살려내는 프레임워크
- 스프링은 좋은 객체 지향 애플리케이션을 개발할 수 있게 도와주는 프레임워크

<br>

# 좋은 객체 지향 프로그래밍이란

## 객체 지향 특징

- 추상화
- 캡슐화
- 상속
- **다형성**

## 객체 지향 프로그래밍

- 컴퓨터 프로그램을 명령어의 목록으로 보는 시각에서 벗어나 여러 개의 독립된 단위, 즉 **"객체"들의 모임**으로 파악하고자 하는 것이다.
- 각각의 **객체는 메시지를 주고받고, 데이터를 처리할 수 있다.** (협력)
- **프로그램을 유연하고 변경이 용이하게** 만들기 때문에 대규모 소프트웨어 개발에 많이 사용된다.

## 다형성의 실세계 비유

- 실세계와 객체 지향은 1:1로 매칭되지 않는다.
- 그래도 실세계의 비유로 이해하기에는 좋다.
- **역할**과 **구현**으로 세상을 구분해보자.

## 역할과 구현을 분리 예시

- 역할과 구현으로 구분하면 단순해지고, 유연해지며 변경도 편리해진다.
- 클라이언트는 대상의 역할(인터페이스)만 알면 된다.
- 클라이언트는 구현 대상의 내부 구조를 몰라도 된다.
- 클라이언트는 구현 대상의 내부 구조가 변경되어도 영향을 받지 않는다.
- 클라이언트는 구현 대상 자체를 변경해도 영향을 받지 않는다.
- 즉, 클라이언트에 영향을 주지 않고 새로운 기능을 제공 가능하게 해준다.

## 자바 언어에서의 역할과 구현을 분리

- 자바 언어의 다형성을 활용한다.
- 역할 = 인터페이스
- 구현 = 인터페이스를 구현한 클래스, 구현 객체
- 객체를 설계할 때 역할과 구현을 명확히 분리해야 한다.
- 객체 설계시 역할(인터페이스)를 먼저 부여하고, 그 역할을 수행하는 구현 객체를 만든다.

## 객체의 협력이라는 관계부터 생각해보기

- 혼자 있는 객체는 없다.
- 클라이언트: 요청, 서버: 응답
- 수많은 클라이언트와 객체 서버는 서로 협력 관계를 가진다.

## 자바 언어의 다형성

- **오버라이딩**을 떠올려보자
- 오버라이딩은 자바 기본 문법이다.
- 다형성으로 인터페이스를 구현한 객체를 실행 시점에 유연하게 변경할 수 있다.
- 물론 클래스 상속 관계도 다형성, 오버라이딩 적용이 가능하다.
    - 인터페이스는 다중 상속이 가능하기 때문에 인터페이스로 하는게 더 낫다.

<img src="https://user-images.githubusercontent.com/35963403/146644359-4dff1fcf-d054-40cb-95ea-7b2a55808490.PNG" width="500">

```java
public class MemberService {
    
    private MemberRepository memberRepository = new MemoryMemberRepository();
}
```

```java
public class MemberService {
    
    // private MemberRepository memberRepository = new MemoryMemberRepository();
       private MemberRepository memberRepository = new JdbcMemberRepository();
}
```

- 클라이언트(멤버 서비스)는 멤버 리포지터리를 의존한다.
- 멤버 리포지터리 인터페이스를 구현한 리포지터리만 할당 가능하다.

## 다형성의 본질

- 인터페이스를 구현한 객체 인스턴스를 실행 시점에 유연하게 변경할 수 있다.
- 다형성의 본질을 이해하려면 협력이라는 객체 사이의 관계에서 시작해야 한다.
- **클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다.**
- 따라서 인터페이스를 안정적으로 잘 설계하는 것이 중요하다.

## 스프링과 객체 지향

- 다형성이 가장 중요하다.
- 스프링은 다형성을 극대화해서 이용할 수 있게 도와준다.
- 스프링에서 이야기하는 제어의 역전(IoC), 의존관계 주입(DI)은 다형성을 활용해서 역할과 구현을 편리하게 다룰 수 있도록 지원한다.
- 따라서 스프링을 사용하면 구현을 편리하게 변경할 수 있다.

<br>

# 좋은 객체 지향 설계의 5가지 원칙 **(SOLID)**

## **SOLID**

### 클린코드로 유명한 로버트 마틴이 정리한 좋은 객체 지향 설계의 5가지 원칙

- SRP: 단일 책임 원칙 (single responsibility principle)
- OCP: 개방-폐쇄 원칙 (open/closed principle)
- LSP: 리스코프 치환 원칙 (Liskov substitution principle)
- ISP: 인터페이스 분리 원칙 (Interface segregation principle)
- DIP: 의존관계 역전 원칙 (Dependency inversion principle)

## SRP 단일 책임 원칙

- 한 클래스는 하나의 책임만 가져야 한다.
- 하나의 책임의 중요한 기준은 **변경**이다.
- 변경이 있을 때 파급 효과가 적으면(한 클래스의 한 지점만 고치면) 단일 책임 원칙을 잘 따른 것이다.

## OCP 개방-폐쇄 원칙

- 소프트웨어 요소는 **확장에는 열려**있으나 **변경에는 닫혀**있어야 한다.
- 다형성을 활용하면 된다. (역할과 구현의 분리)
- 인터페이스를 구현한 새로운 클래스를 하나 만들어서 새로운 기능을 구현한다.
    - 기존 코드를 변경하지 않게 된다.

### 문제점

- MemberService 클라이언트가 구현 클래스를 직접 선택한다.
    - MemberRepository m = new MemoryMemberRepository(); // 기존 코드
    - MemberRepository m = new JdbcMemberRepository();   // 변경 코드
- 구현 객체를 변경하려면 클라이언트 코드를 변경해야 한다.
- 다형성을 사용했지만 OCP 원칙을 지킬 수 없다.
- 해결법: 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다.

## LSP 리스코프 치환 원칙

- 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.
- 다형성에서 하위 클래스는 인터페이스 규약을 다 지켜야 한다는 것이다.
- 인터페이스를 구현한 구현체를 믿고 사용하려면 이 원칙이 필요하다.
- ex) 자동차 인터페이스의 엑셀은 앞으로 가라는 기능인데, 뒤로 가게 구현하면 LSP 위반이다. 느리더라도 앞으로 가능 기능을 구현해야 한다.

## ISP 인터페이스 분리 원칙

- 특정 클라이언트를 위한 인터페이스 여러개가 범용 인터페이스 하나보다 낫다.
- 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
- 사용자 클라이언트 -> 운전자 클라이언트, 정비사 클라이언트로 분리
- 분리하면 정비 인터페이스 자체가 변경되어도 운전자 클라이언트에 영향을 주지 않는다.
- 인터페이스가 명확해지고, 대체 가능성이 높아진다.

## DIP 의존관계 역전 원칙

- 프로그래머는 **추상화에 의존해야지, 구체화에 의존하면 안된다.**
- 즉, 구현 클래스에 의존하지 말고, 인터페이스에 의존하라는 뜻이다.
- 인터페이스에 의존하려면 언제든지 구현 클래스를 교체할 수 있게 설계해야 한다.
- 구현 클래스에 의존하면 변경이 어려워진다.

### 문제점

- OCP에서 설명한 MemberService는 인터페이스에 의존하지만, 구현 클래스에도 동시에 의존한다.
- MemberService 클라이언트가 구현 클래스를 직접 선택
    - MemberRepository m = new MemoryMemberRepository();
- DIP 위반이다.

## 정리

- 객체 지향의 핵심은 다형성이다.
- 하지만 다형성만으로는 쉽게 부품을 갈아 끼우듯이 개발할 수 없다.
- 다형성만으로는 구현객체를 변경할 때 클라이언트 코드도 함께 변경된다.
- 다형성만으로는 OCP, DIP를 지킬 수 없다.
- 뭔가가 더 필요하다.

<br>

# 객체 지향 설계와 스프링

## 다시 스프링으로

- 스프링은 DI, DI 컨테이너로 다형성 + OCP, DIP를 가능하게 지원한다.
    - DI(Dependency Injection): 의존관계, 의존성 주입
    - DI 컨테이너 제공
- 클라이언트 코드의 변경 없이 기능 확장이 가능하다.
- 쉽게 부품을 교체하듯이 개발할 수 있다.

## 정리

- 모든 설계에 역할과 구현을 분리한다.
- 이상적으로는 모든 설계에 인터페이스를 부여해야 한다.

### 실무 고민

- 인터페이스를 도입하면 추상화라는 비용이 발생한다.
- 기능을 확장할 가능성이 없으면, 구현체 클래스를 직접 사용하고, 향후 꼭 필요할 때 리팩터링해서 인터페이스를 도입하는 것도 방법이다.

<br>

# 스프링 핵심 원리 이해 1 - 예제 만들기

## 비즈니스 요구 사항과 설계

### 회원

- 회원을 가입하고 조회할 수 있다.
- 회원은 일반과 VIP 두 가지 등급이 있다.
- 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다. (미확정)

### 주문과 할인 정책

- 회원은 상품을 주문할 수 있다.
- 회원 등급에 따라 할인 정책을 적용할 수 있다.
- 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용 (나중에 변경될 수 있다.)
- 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수도 있다.

#### 요구사항을 보면 회원 데이터, 할인 정책 같은 부분은 지금 결정하기 어려운 부분이다. 
#### 하지만 객체 지향 설계 방법을 이용해 인터페이스를 만들고 구현체를 언제든지 갈아끼울 수 있도록 설계하면 된다.

## 회원 도메인 설계

### 회원 도메인 협력 관계

<img src="https://user-images.githubusercontent.com/35963403/146675900-86cf335c-d474-4e77-b5d4-9cd5b7503003.PNG" width="700">

- 회원 저장소 역할 인터페이스의 구현을 메모리 회원 저장소, DB 회원 저장소, 외부 시스템 연동 저장소로 하고 세 개의 구현체 중 하나를 선택하면 된다.

### 회원 클래스 다이어그램

<img src="https://user-images.githubusercontent.com/35963403/146675872-fd073587-2982-4f80-bc88-101917ef6818.PNG" width="700">

### 회원 객체 다이어그램

<img src="https://user-images.githubusercontent.com/35963403/146675876-22da4f83-abcc-4a9c-918e-7b2b08598768.PNG" width="700">

- 서버가 실행되고 클라이언트 객체가 실제로 참조(사용)하는 객체들이다.
- 어떤 구현체를 넣을지는 동적으로 구현되기 때문에(서버가 뜰 때 선택) 객체 다이어그램이 필요하다.

## 회원 도메인 개발

#### 회원 엔티티

### 회원 등급

```java
package gxdxx.spring_basic.member;

public enum Grade {
  BASIC,
  VIP
}
```

### 회원 엔티티

```java
package gxdxx.spring_basic.member;

public class Member {
    
    private Long id;
    private String name;
    private Grade grade;

    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
```

#### 회원 저장소

### 회원 저장소 인터페이스

```java
package gxdxx.spring_basic.member;

public interface MemberRepository { 
    
    void save(Member member);
    
    Member findById(Long memberId);
}
```

### 메모리 회원 저장소 구현체

```java
package gxdxx.spring_basic.member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository{
    
    private static Map<Long, Member> store = new HashMap<>();
    
    @Override 
    public void save(Member member) {
        store.put(member.getId(), member);
    }
    
    @Override 
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
```

- HashMap은 동시성 문제가 있을 수 있어서 실무에선 ConcurrentHashMap을 써야한다.

#### 회원 서비스

### 회원 서비스 인터페이스

```java
package gxdxx.spring_basic.member;

public interface MemberService {
    
    void join(Member member);
    Member findMember(Long memberId);
}
```

### 회원 서비스 구현체

```java
package gxdxx.spring_basic.member;

public class MemberServiceImpl implements MemberService{
    
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    
    @Override 
    public void join(Member member) {
        memberRepository.save(member);
    }
    
    @Override 
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}

```

- 관례상 구현체가 하나만 있을 때는 인터페이스명 + Impl을 쓴다.
- memberserviceImpl
  - join, findmember을 하려면 MemberRepository 인터페이스가 필요하다.
  - join에서 save를 호출하면 다형성에 의해서 MemoryMemberRepository에 있는 save가 호출된다.

---

## 회원 도메인 실행과 테스트

### 회원 도메인 - 회원 가입 main

- 순수 자바로 테스트한다.

```java
package gxdxx.spring_basic;

import gxdxx.spring_basic.member.Grade;
import gxdxx.spring_basic.member.Member;
import gxdxx.spring_basic.member.MemberService;
import gxdxx.spring_basic.member.MemberServiceImpl;

public class MemberApp { 
    
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);
        
        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
```

- 애플리케이션 로직으로 테스트 하는 것은 좋은 방법이 아니다.
- JUnit 테스트를 사용한다.

### 회원 도메인 - 회원 가입 테스트

```java
package gxdxx.spring_basic.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    
    MemberService memberService = new MemberServiceImpl();
 
    @Test
    void join() {
        
        //given
        Member member = new Member(1L, "memberA", Grade.VIP);
 
        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        
        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
```

### 회원 도메인 설계의 문제점

- MemberServiceImpl
  - MemberRepository 인터페이스를 의존하고 있으므로 DIP를 잘 지키는 것처럼 보인다.
  - 하지만 실제 할당하는 부분이 memoryMemberRepository 구현체를 의존하고 있다.
  - 즉 추상화(인터페이스)에도 의존하고, 구체화(구현)에도 의존하고 있으므로 DIP 위반이다.

## 주문과 할인 도메인 설계

### 주문 도메인 협력, 역할, 책임

<img src="https://user-images.githubusercontent.com/35963403/146677617-cfd15044-d278-4651-9db6-c1c6b99b0a7a.PNG" width="700">

1. 주문 생성: 클라이언트는 주문 서비스에 주문 생성을 요청한다.
2. 회원 조희: 할인을 위해서는 회원 등급이 필요하므로 주문 서비스는 회원 저장소에서 회원을 조회한다.
3. 할인 적용: 주문 서비스는 회원 등급에 따른 할인 여부를 할인 정책에 위임한다.
4. 주문 결과 반환: 주문 서비스는 할인 결과를 포함한 주문 결과를 반환한다.

#### 실제로는 주문 데이터를 DB에 저장하지만 일단은 단순히 주문 결과를 반환한다.

### 주문 도메인 전체

<img src="https://user-images.githubusercontent.com/35963403/146677754-77180ef5-8697-4a09-8719-ca0c3b6aae22.PNG" width="700">

- **역할과 구현을 분리**해서 자유롭게 구현 객체를 조립할 수 있게 설계했다.
- 덕분에 회원 저장소, 할인 정책을 유연하게 변경할 수 있다.

### 주문 도메인 클래스 다이어그램

<img src="https://user-images.githubusercontent.com/35963403/146677843-145000e6-5bd8-4788-ad34-21faa289d83e.PNG" width="700">

### 주문 도메인 객체 다이어그램 1

<img src="https://user-images.githubusercontent.com/35963403/146677880-8b5fc739-5528-4f8b-9ef1-b04fb286041a.PNG" width="700">

- 회원을 메모리에서 조회하고, 정액 할인 정책(고정 금액)을 지원해도 주문 서비스를 변경하지 않아도 된다.
- 역할들의 협력 관계를 그대로 재사용할 수 있다.

### 주문 도메인 객체 다이어그램 2

<img src="https://user-images.githubusercontent.com/35963403/146677905-b7b950be-60bb-4f8e-abf8-489bd71a51f9.PNG" width="700">

- 회원을 메모리가 아닌 실제 DB에서 조회하고, 정률 할인 정책(주문 금액에 따라 % 할인)을 지원해도 주문 서비스를 변경하지 않아도 된다.
- 역할들의 협력 관계를 그대로 재사용할 수 있다.

## 주문과 할인 도메인 개발

### 할인 정책 인터페이스

```java
package gxdxx.spring_basic.discount;

import gxdxx.spring_basic.member.Member;

public interface DiscountPolicy {
    
    /**
     * * @return 할인 대상 금액
     * */
    int discount(Member member, int price);
}
```

### 정액 할인 정책 구현체

```java
package gxdxx.spring_basic.discount;

import gxdxx.spring_basic.member.Grade;
import gxdxx.spring_basic.member.Member;


public class FixDiscountPolicy implements DiscountPolicy {
 
    private int discountFixAmount = 1000; //1000원 할인
  
    @Override 
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        }
        return 0;
    }

}
```

- VIP면 1000원을 할인하고, 아니면 할인하지 않는다.

### 주문 엔티티

```java
package gxdxx.spring_basic.order;

public class Order {
    
    private Long memberId;
    private String itemName;
    private int itemPrice;
    private int discountPrice;

    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.discountPrice = discountPrice;
    }

    public int calculatePrice() {
        return itemPrice - discountPrice;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "memberId=" + memberId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
```

- toStinrg(): 객체를 출력하면 toString()이 호출된다.

### 주문 서비스 인터페이스

```java
package gxdxx.spring_basic.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
```

### 주문 서비스 구현체

```java
package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {
    
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
 
    @Override 
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```

1. 주문 요청이 들어오면
2. 회원 정보를 조회하고
3. 할인 정책에 회원 정보와 주문 가격을 넘겨서
4. 할인 정책을 적용한 최종 할인 금액을 받고
5. 생성된 order 객체를 반환한다.

- orderservice는 할인에 대해서는 잘 모르고 정보를 discountPolicy에게 넘겨서 리턴값만 받는다.
- **단일 책임 원칙**을 잘 따른것이다.

## 주문과 할인 도메인 실행과 테스트

### 주문과 할인 정책 실행

```java
package gxdxx.spring_basic;

import gxdxx.spring_basic.member.Grade;
import gxdxx.spring_basic.member.Member;
import gxdxx.spring_basic.member.MemberService;
import gxdxx.spring_basic.member.MemberServiceImpl;
import gxdxx.spring_basic.order.Order;
import gxdxx.spring_basic.order.OrderService;
import gxdxx.spring_basic.order.OrderServiceImpl;

public class OrderApp {
    
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        
        System.out.println("order = " + order);
    }
}
```

#### 결과

```
order = Order{memberId=1, itemName='itemA', itemPrice=10000, discountPrice=1000}
```

- 할인 금액이 잘 출력된다.
- 애플리케이션 로직으로 테스트 하는 것은 좋은 방법이 아니기 때문에 JUnit 테스트를 사용한다.

### 주문과 할인 정책 테스트

```java
package gxdxx.spring_basic.order;

import gxdxx.spring_basic.member.Grade;
import gxdxx.spring_basic.member.Member;
import gxdxx.spring_basic.member.MemberService;
import gxdxx.spring_basic.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest { 
    
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();
    
    @Test 
    void createOrder() {
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
```

- long: primitive type은 null을 넣을 수 없다.
- 객체 생성 단계에서 null을 넣어야 될수도 있어서 Long을 사용한다.
- junit5에는 displayName이라는 한글로 쓸수있는 기능을 제공한다.

#### 단위테스트를 잘 만드는게 중요하다.
- 단위테스트: 스프링이나 컨테이너의 도움 없이 순수 자바 코드로 테스트 하는것을 말한다.

<br>

# 스프링 핵심 원리 이해 2 - 객체 지향 원리 적용

## 새로운 할인 정책 개발

### 할인 정책을 확장한다.

- 기존 정책은 VIP가 10000원을 주문하든 20000만원을 주문하든 항상 1000원을 할인했다.
- 새로운 정책은 10% 할인을 적용해 10000원을 주문하면 1000원 할인, 20000원을 주문하면 2000원 할인을 해주려고 한다.

### RateDiscountPolicy 추가

<img src="https://user-images.githubusercontent.com/35963403/146946643-6aaa9bf3-b217-4ccf-8f64-d1e46b317e8c.PNG" width="700">

### RateDiscountPolicy 코드 추가

```java
package gxdxx.spring_basic.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {
    
    private int discountPercent = 10; //10% 할인
  
    @Override 
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        }
        return 0;
    }
}
```

### 테스크 코드 작성

```java
package gxdxx.spring_basic.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {
    
    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();
    
    @Test 
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.") 
    void vip_o() {
        //given
        Member member = new Member(1L, "memberVIP", Grade.VIP);
        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
        assertThat(discount).isEqualTo(1000);
    }
    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x() {
        //given
        Member member = new Member(2L, "memberBASIC", Grade.BASIC);
        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
        assertThat(discount).isEqualTo(0);
    }
}
```

## 새로운 할인 정책 적용과 문제점

### 할인 정책을 애플리케이션에 적용해본다.

#### 할인 정책을 변경하려면 클라이언트인 OrderServiceImpl 코드를 수정해야 한다.

```java
public class OrderServiceImpl implements OrderService { 
    
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    
}
```

#### 문제점

- 역할과 구현을 충실하게 분리했다.
- 다형성도 활용하고, 인터페이스와 구현 객체를 분리했다.
- DIP: 주문서비스 클라이언트(OrderServiceImpl)는 DiscountPolicy 인터페이스에 의존하고 있다.
  - 클래스 의존관계를 분석해보면 **추상(인터페이스) 뿐만 아니라 구체(구현) 클래스에도 의존하고 있다.**
    - 추상(인터페이스) 의존: DiscountPolicy
    - 구체(구현) 클래스: FixDiscountPolicy, RateDiscountPolicy
- OCP: 변경하지 않고 확장하고 있다.
  - 현재 코드는 기능을 확장해서 변경하면, 클라이언트 코드에 영향을 준다. 따라서 OCP를 위반한다.

### 클라이언트 코드를 변경해야 되는 이유

### 기대했던 의존관계

<img src="https://user-images.githubusercontent.com/35963403/146950031-86c101cd-4263-4a6c-ade5-a12b9c302b4d.PNG" width="700">

- 지금까지 단순히 DiscountPolicy 인터페이스에만 의존하고 있다고 생각했다.

### 실제 의존관계

<img src="https://user-images.githubusercontent.com/35963403/146950039-3b3b2b70-0043-474d-ba3a-56384fa24355.PNG" width="700">

- 실제로는 OrderServiceImpl이 DiscountPolicy 뿐만 아니라 FixDiscountPolicy 구체 클래스도 함께 의존하고 있었다.
- 실제 코드를 보면 DIP를 위반하고 있었다.

### 정책 변경

<img src="https://user-images.githubusercontent.com/35963403/146950052-13bfe9fd-afe4-4be3-9f5b-60c0870e3ce9.PNG" width="700>

- 그래서 FixDiscountPolicy를 RateDiscountPolicy로 변경하는 순간 OrderServiceImpl의 소스코드도 함께 변경해야 한다.
- OCP 위반이다.

### 문제 해결 방법

- 클라이언트 코드인 OrderServiceImpl 은 DiscountPolicy 의 인터페이스 뿐만 아니라 구체 클래스도 함께 의존한다.
- 그래서 구체 클래스를 변경할 때 클라이언트 코드도 함께 변경해야 한다.
- DIP를 위반하지 않도록 **인터페이스에만 의존하도록 의존관계를 변경**하면 된다.

### 인터페이스에 의존하도록 설계를 변경

<img src="https://user-images.githubusercontent.com/35963403/146950781-7eb7bb12-2ee4-42bf-9c08-f88e81bfbd03.PNG" width="700">

### 인터페이스에 의존하도록 코드를 변경

```java
public class OrderServiceImpl implements OrderService {
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    private DiscountPolicy discountPolicy;
}
```

- 인터페이스에만 의존하도록 설계와 코드를 변경했다.
- 그런데 구현체가 없는데 어떻게 코드를 실행할 수 있을까?
- 실제 실행을 해보면 NPE(null pointer exception)가 발생한다.

#### 해결방안

- 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를 대신 생성하고 주입해주어야 한다

## 관심사의 분리

생성자 주입: 생성자를 통해서 새로 생성된 객체가 들어감

클라이언트, 서버: A객체가 B객체의 메서드를 호출하면 A클래스가 클라이언트, B클래스가 서버가 된다.
클라이언트 서버라는 것은 작게는 이렇게 객체부터, 크게는 웹 브라우저(클라이언트) 서버(애플리케이션 서버) 개념까지 확장됩니다.

### OCP
소프트웨어 요소를 새롭게 확장해도 사용 영역의 변경은 닫혀 있다
- 변경할 필요가 없다는 뜻

## 스프링 전환
applicationContext: 객체들을 관리해줌

new AnnotationConfigApplicationContext(AppConfig.class);
- Appconfig에 있는 환경설정 정보를 가지고 스프링이 빈들을 스프링 컨테이너에 넣어서 관리해줌

getBena(): (객체이름, 객체타임)

## 스프링 컨테이너 생성

XML기반
- 요즘 잘 사용x


## 모든 빈 조회하기

- iter입력해서 리스트나 배열 for문을 자동으로 생성
- 타입을 지정안해줬기 때문에 ac.getBean(beanDefinitionName);하면 Object로 꺼내짐
- BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName); : 빈 하나하나에 대한 정보

## 스프링 빈 조회 - 기본

- Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);: memberService가 MemberServiceImpl의 인스턴스인지 호가인
- getBena()은 스프링 빈에 등록된 인스턴스 타입으로 결정되기 떄문에 인터페이스가 아닌 구체를 반환해도 가능

## 스프링 빈 조회 - 동일한 타입이 둘 이상

- NoUniqueBeanDefinitionException 에러가 발생한다.

## 스프링 빈 조회 - 상속 관계

- 실무에선 applicationContext에서 직접 getBean()을 할 일이 거의없다.
- 스프링 컨테이너가 자동으로 의존관계 주입 해주는걸 쓰거나
- Bean으로 직접 생성해서 쓴다.

- 스프링 빈 만드는 2가지 : 직접 ㅅ프링 빈 등록, 팩토리 빈으로 등록, 일반적으로는 팩토리 빈을 통해서 등록

# 싱글톤 컨테이너

## 웹 애플리케이션과 싱글톤

- 객체가 jvm 안에 딱 하나만 있어야 되는것

고객이 3번 요청하면 객체가 3개 생성된다.

- private static final  SingletonService instance = new SingletonService(); : 자기자신을 내부에 private로 가지고 있는데 static으로 가지고 있으면 클래스 레벨에 올라가기 때문에 딱 하나만 존재하게 된다.

1. static 영역에 객체 instance(자기자신)를 미리 하나 생성해서 instance 변수에 참조를 넣어둔다. 올려둔다

- same: 객체의 값이 같은지
- equal: 객체의 참조값이 같은지 

- 의존관계상 클라이언트가 구체 클래스에 의존한다. DIP를 위반한다.
  - ex) AppConfig에서 return new MemberServiceImpl.getInstance() 

## @Configuration과 싱글톤

- MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class); : Impl에 테스트 용도로 작성해놓은 메서드를 사용하기 위해 Impl로 꺼냄
  - 원래는 구체 타입으로 꺼내면 안좋음

### configuration 적용하지 않고 하면

- memberSerivce를 스프링빈으로 등록할 때 memberService() 메서드가 호출됩니다. 
- 이 메서드가 호출되면 new MemberServiceImpl()을 생성하면서 memberRepository() 메서드를 호출해서 의존관계를 찾습니다.
- 그런데 @Configuration이 없으니 memberRepository() 메서드가 스프링 코드인지 아닌지 인식하지 못하고, 순수한 자바 메서드로 호출 해버립니다. 
- 그러면 내부에서 new MemoryMemberRepository()가 호출되는 것이지요.
- 결국 스프링의 도움을 받지 못하고 스프링 빈이 아닌 순수 자바로 생성된 new MemoryMemberRepository()가 주입되어 버립니다. 
- 이것은 스프링이 관리하는 객체가 아니라 방금 발생한 메서드 호출로 새로 생성된 객체입니다!
- 결국 빈 1개, 일반 객체 2개가 존재하게 된다.

#### 결국 다음과 같이 되는 것이지요.

- @Autowired MemberService memberService; //여기 내부에 들어있는 memberRepository는 스프링 빈이 아님 단순히 new로 생성한 MemoryMemberRepository임
- @Autowired MemberRepository memberRepository; //스프링 빈이 등록한 memberRepository
- memberService.getMemberRepository() != memberRepository //따라서 둘은 다른 객체


## 의존관계 자동주입

- 스프링 컨텡이너
  - 스프링 컨테이너에 스프링 빈을 등록 -> 연관관계를 자동으로 주입(@Autowied 들을 자동으로 주입)

- 생성자 주입은 빈을 등록하면서 의존관계 주입도 같이 일어남

### 필드 주입

- 만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다
  - setter를 만들어서 해결 가능하긴 함