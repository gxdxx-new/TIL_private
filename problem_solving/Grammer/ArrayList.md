# ArrayList

#### List 인터페이스를 상속받은 클래스로 크기가 가변적으로 변하는 선형리스트이다.

#### 객체들이 추가되어 저장 용량을 초과하면 자동으로 부족한 크기만큼 저장용량이 늘어난다.
- 배열은 한 번 생성되면 크기가 변하지 않는다.

### 선언

```java
ArrayList list = new ArrayList(); //타입 미설정 Object로 선언됨
ArrayList<Student> members = new ArrayList<Student>(); //타입 설정 Student 객체만 사용 가능
ArrayList<Integer> num = new ArrayList<Integer>(); //타입 설정 int 타입만 사용 가능
ArrayList<Integer> num2 = new ArrayList<>(); //new에서 타입 파라미터 생략 가능
ArrayList<Integer> num3 = new ArrayList<Integer>(10); //초기 용량(capacity)지정
ArrayList<Integer> list2 = new ArrayList<Integer>(Arrays.asList(1,2,3)); //생성시 값추가
```

- ArrayList에 타입을 명시해주는게 좋다.
- 제네릭스는 선언할 수 있는 타입이 객체 타입이다.
- int는 기본자료형이기 때문에 들어갈수 없으므로 int를 객체화시킨 wrapper클래스를 사용해야 한다.

### 값 추가

#### add(index, value)

```java
ArrayList<Integer> list = new ArrayList<Integer>();
list.add(3); //값 추가
list.add(null); //null값도 add가능
list.add(1,10); //index 1에 10 삽입
```

```java
ArrayList<Student> members = new ArrayList<Student>();
Student student = new Student(name,age);
members.add(student);
members.add(new Member("홍길동",15));
```

- index를 생략하면 ArrayList 맨 뒤에 추가된다.
- index 중간에 값을 추가하면 해당 인덱스부터 마지막 인덱스까지 모두 1씩 뒤로 밀려난다.
- index 중간에 데이터를 추가할 경우가 많으면 LinkedList를 활용하는게 성능상에서 더 좋다.

### 값 삭제

#### remove(index)

```java
ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1,2,3));
list.remove(1);  //index 1 제거
list.clear();  //모든 값 제거
```

- 특정 index의 객체를 삭제하면 해당 index 뒤부터 마지막 index까지 모드 앞으로 1씩 당겨진다.
- 모든 값을 제거하려면 clear()를 사용하면 된다.

### 크기 구하기

#### size()

```java
ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1,2,3));
System.out.println(list.size()); //list 크기 : 3
```

### 값 출력

#### get(index)

```java
ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1,2,3));

System.out.println(list.get(0));//0번째 index 출력
		
for(Integer i : list) { //for문을 통한 전체출력
    System.out.println(i);
}

Iterator iter = list.iterator(); //Iterator 선언 
while(iter.hasNext()){//다음값이 있는지 체크
    System.out.println(iter.next()); //값 출력
}
```

### 값 검색

#### contains(index)

#### indexOf(value)

```java
ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1,2,3));
System.out.println(list.contains(1)); //list에 1이 있는지 검색 : true
System.out.println(list.indexOf(1)); //1이 있는 index반환 없으면 -1
```

##### [블로그] [[JAVA] 자바 ArrayList 사용법 & 예제 총정리(https://coding-factory.tistory.com/551) - ArrayList