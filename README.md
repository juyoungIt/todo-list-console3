# TODO list console app version 3

## Introduction
![todo-list-console3](https://user-images.githubusercontent.com/35681772/73604203-0eadb280-45d0-11ea-94e2-cf901df5c92a.gif)

TDD 에 익숙해 지기 위해, OOP 의 관점에서 생각해 보기 위해 시작한 TODO list 콘솔 어플리케이션
기존에 개발했던 어플리케이션을 피드백을 바탕으로 개선하여 3번째 다시 개발함

이 프로젝트를 진행함에 있어 켄트 벡이 저술한 [테스트 주도 개발](https://book.naver.com/bookdb/book_detail.nhn?bid=7443642) 을 바탕으로 테스트 코드를 작성하였습니다.
또한 Unit test 에 있어 [Given-When-Then](https://en.wikipedia.org/wiki/Given-When-Then) 과 같은 패턴도 존재하지만, 
보다 직관적으로 이해가 쉬웠던 [Arrange-Act-Assert](https://wiki.c2.com/?ArrangeActAssert) 패턴으로 테스트를 작성했습니다.

 * Arrange : 객체를 초기화하고 테스트 중인 메서드에 전달되는 데이터의 값을 설정합니다.
 * Act : 정렬된 매개 변수를 사용하여 테스트 중인 메서드를 호출합니다.
 * Assert : 테스트 중인 메서드의 작업이 예상한 대로 작동하는지 확인합니다.

## 기능 요구사항
```
할일 목록(todo-list) 콘솔 어플리케이션 구현

### 기능
 * 사용자는 텍스트로 된 할일을 추가할 수 있다.
   * 할일 추가 시 다른 할일들을 참조 걸 수 있다.
   * 참조는 다른 할일의 id를 명시하는 형태로 구현한다. (예시 참고)
 * 사용자는 할일을 수정할 수 있다.
 * 사용자는 할일 목록을 조회할 수 있다.
   * 조회시 작성일, 최종수정일, 내용이 조회 가능하다.
 * 사용자는 할일을 완료처리 할 수 있다.
   * 완료처리 시 참조가 걸린 완료되지 않은 할일이 있다면 완료처리할 수 없다. (예시 참고)

### 과제 요구사항
 * 콘솔 어플리케이션으로 개발
 * 콘솔 어플리케이션 개발언어는 Java 권장함 (단, 다른 언어에 특별히 자신있는 경우 선택에 제한을 두지 않음)
 * 단위테스트 필수, 통합테스트는 선택
 * README.md 파일에 기능목록, 문제해결 전략 및 프로젝트 빌드, 실행 방법 명시

### 목록조회 예시 및 설명
메뉴
[1] 리스트 [2] TODO 생성 [3] TODO 수정 [4] TODO 삭제 [5] TODO 완료
예)
명령어를 입력하세요.
1 (엔터)
| id | 할일 | 작성일시 | 최종수정일시 | 완료처리 |
|——|——————|——————————|—————|——————————|
| 1 | 집안일 | 2018-04-01 10:00:00 | 2018-04-01 13:00:00 |  |
| 2 | 빨래 @1 | 2018-04-01 11:00:00 | 2018-04-01 11:00:00 |  |
| 3 | 청소 @1 | 2018-04-01 12:00:00 | 2018-04-01 13:00:00 |  |
| 4 | 방청소 @1 @3 | 2018-04-01 12:00:00 | 2018-04-01 13:00:00 |  |
2 공부 @1 (엔터치면 리스트)
5 5 (엔터치면 리스트)
| 5 | 공부 @1 | 2020-01-01 13:00:00 | 2018-04-01 13:00:00 | 2018-04-01 13:00:00  |
5 1 (엔터치면 설명)
2, 3, 4가 완료되지 않아 완료할 수 없습니다

 * 할일 2, 3번은 1번에 참조가 걸린 상태이다.
 * 할일 4번은 할일 1, 3번에 참조가 걸린 상태이다.
 * 할일 1번은 할일 2번, 3번, 4번이 모두 완료되어야 완료처리가 가능하다.
 * 할일 3번은 할일 4번이 완료되어야 완료처리가 가능하다.
```

## 기술 스택
 * Java(version: '12.0.1')
 * JUnit(version: '4.12')
 * Jupiter(version: '5.5.2')
 
## Todo-list-console 아키텍처

<img width="1072" alt="Screen Shot 2020-06-01 at 4 18 07 PM" src="https://user-images.githubusercontent.com/35681772/83385078-83301b00-a423-11ea-8cdb-0d426b6a7d3c.png">

Todo-list-console 은 OOP(Object-Oriented Programming) 의 관점에서 개발하려고 노력했습니다. 
또한 콘솔 단과 비즈니스 로직을 분리하여 추후에 이것이 웹 프로젝트로 변경되더라도 도메인은 변화가 없도록 설계하였습니다.

### console 단
 * TodoListConsoleMain : main 함수가 위치하고 있습니다. main 함수에서는 각 processor 에 필요한 의존성을 주입 해 주고 있습니다.
 이렇게 필요한 의존성을 외부에서 주입 받는식으로 구현하여, 추후에 구현체가 변경이 되더라도 코드 수정을 최소화 할 수 있는 방향으로 개발하였습니다.
 * TodoListConsole : 실행 가능한 기능들을 사용자의 입력과 매핑시켜 적절한 processor 를 실행시킵니다.
 Processor 구현에 있어 종료와 리스트를 보여주는 기능을 Processor 로 함께 제공하려 했으나, 
 의존관계가 엮여 있어 if 문으로 처리한 점이 아쉽습니다.  

<img width="550" alt="Screen Shot 2020-06-01 at 4 02 15 PM" src="https://user-images.githubusercontent.com/35681772/83384122-8d511a00-a421-11ea-9624-76a474c09856.png">

> 개인적으로 아쉬웠던 구현

 * IOHelper : 콘솔 단에서의 I/O 에 관한 모든 기능을 담당합니다. 
 따로 I/O 만을 담당하는 객체를 두어 비즈니스 로직에 불필요한 입출력 관련 코드가 섞이지 않도록 설계하였습니다. 

### Domain
 * IdGenerator : 할일의 고유성을 보장하기 위해 id를 발급하는 역할을 담당합니다.
 * Todo : 할일을 나타냅니다.
 * TodoRepository : 할일에 대한 저장소 역할을 담당합니다. CRUD 기능을 제공합니다.
 
### Menu
 * TodoMenu : 제공하는 기능을 나타냅니다.
 * TodoMenuParameter : 각 기능별로 다르게 필요한 파라미터를 나타냅니다.
 
### Processor
 * TodoMenuProcessor
 * TodoCreateMenuProcessor : 할일의 생성 기능을 담당합니다.
 * TodoFinishMenuProcessor : 할일의 종료 기능을 담당합니다.
 * TodoRemoveMenuProcessor : 할일의 삭제 기능을 담당합니다.
 * TodoUpdateMenuProcessor : 할일의 갱신 기능을 담당합니다.

## 기능 목록
 * 기본 기능
   * 메뉴를 표시한다.
   * 명령어를 입력받는다.
 
 * 할 일 생성
   * id를 생성한다.
   * 할 일을 생성한다.
   * 할 일을 생성할 때 의존성이 있는 경우 설정할 수 있다.
   * 할 일을 생성할 때 의존성이 있는 경우 존재하지 않는 할 일 id의 의존은 예외를 던진다.
   * 할 일을 생성할 때 자기 자신을 의존하도록 의존성을 설정하는 경우 메세지를 출력하며 해당 의존에 대해서만 무시된다.
   * 할 일은 id, 내용, 의존성이 있어야 생성된다.
   * 할 일을 생성할 때 내용이 공백이면 예외를 던진다.
   * 할 일은 다른 할 일과 의존성을 가질 수 있다.
   * 할 일은 생성과 동시에 생성 시간이 자동으로 할당된다.
   * 할 일은 id, 내용, 의존성, 작성 시간, 최종 수정 시간, 완료 시간에 대한 정보를 제공한다.

 * 할 일 추가
   * 새로운 할 일을 리스트에 추가한다.
   
 * 할 일 조회
   * 할 일을 id로 조회한다.
   * 리스트에 존재하지 않는 할 일 id에 대한 조회는 예외를 던진다.
   * 여러 id에 대해 일치하는 할 일을 모두 찾아 리스트로 반환한다.
   * 입력 받은 id를 의존하고 있는 객체를 리스트로 만들어 반환한다.
      
 * 할 일 수정
   * 할 일의 내용을 수정할 수 있다.
   * 할 일은 다른 할 일과 의존성을 가지도록 수정할 수 있다.
   * 할 일을 수정할 때 존재하지 않는 할 일의 id에 대한 수정은 예외를 던진다.
   * 할 일을 수정할 때 의존성은 수정시 입력된 값으로 새롭게 갱신된다.
   * 할 일을 수정할 때 의존성이 있는 경우 존재하지 않는 할 일 id의 의존은 예외를 던진다.
   * 할 일을 수정할 때 자기 자신을 의존하도록 의존성을 설정하는 경우 메세지를 출력하며 해당 의존에 대해서만 무시된다.
   * 할 일이 수정되면 수정 시간이 자동으로 갱신된다.
      
 * 할 일 삭제
   * 삭제할 할 일의 id를 입력 받아 할 일을 삭제한다.
   * 할 일을 삭제할 때 존재하지 않는 할 일의 id에 대한 삭제는 예외를 던진다.
   * 다른 할 일이 의존하는 할 일을 삭제하려는 경우 예외를 던진다.
   * 할 일을 객체를 받아와 일치하는 객체를 삭제한다.
   * 할 일을 삭제할 때 리스트에 존재하지 않는 할 일 객체에 대한 삭제는 예외를 던진다. 
 
 * 할 일 완료
   * 할 일을 완료하면 완료 시간이 자동으로 할당된다.
   * 완료할 할 일의 id를 입력 받아 할 일을 완료한다.
   * 할 일을 완료할 때 존재하지 않는 할 일의 id에 대한 완료는 예외를 던진다.   
   * 다른 할 일(자식)이 의존하는 할 일(부모)을 완료하려는 경우 다른 모든 할 일(자식)이 완료되었을 때 완료할 수 있다.
   * 다른 할 일(자식)이 의존하는 할 일(부모)을 완료하려는 경우 다른 모든 할 일(자식)이 완료되지 않았을 때 예외를 던진다.
 
 * 예외
   * 허용되지 않는 값이 입력된 경우 이유를 출력하고 메뉴를 다시 출력한다.

## Achievement

<img width="505" alt="Screen Shot 2020-06-01 at 4 21 26 PM" src="https://user-images.githubusercontent.com/35681772/83385318-f6d22800-a423-11ea-82c1-21063f395f4e.png">

> Unreachable 한 switch-case 문에서의 default 영역과 콘솔 단의 I/O 코드를 제외하고 Test Coverage 100%

위 프로젝트에서 Switch-Case 문을 사용하였는데, enum 문으로 분기가 나뉘기 때문에 enum 의 valueOf 역할을 하는 함수에서 예외를 잡도록 구현했습니다. 
그래서 잘못된 분기로 빠질 여지가 없어 default block 에는 도달할 수 없습니다.
또한 콘솔 단의 I/O 에 관련된 코드는 직접 입력하거나 실행하여 눈으로 확인할 수 있어 별 다른 테스트 코드를 작성하지 않았습니다. 
이를 제외하고 테스트 커버리지를 100%로 맞춰 __작성한 코드의 기능이 예상한 대로 작동할 것이라 자신감__ 을 얻을 수 있었습니다.

또한, __발생 가능한 예외들에 대한 충분한 고민을 할 수 있어__ 구현을 먼저 했다면 미흡했을 수 있는 예외 처리에 있어 보다 꼼꼼하게 점검할 수 있었습니다.

처음엔 테스트 코드 작성이 익숙하지 않아 어떤 것을 어떻게 테스트 해야 할 지 몰랐었으나, 
프로젝트에 필요한 __기능 목록__ 을 구체적으로 작성하고 기능 별로 테스트 코드를 작성하는 방식으로 이를 개선했습니다.

의존 관계가 설정되어 있는 경우 [Mockito](https://site.mockito.org/) 의 도움을 받아 보다 편리한 테스트 코드 작성을 할 수 있었습니다.

__Mockito__ 는 Mocking 과 Verification 을 도와주는 프레임워크 입니다.
그래서 테스트를 진행할 대상이 되는 코드 외의 의존하는 객체를 Mocking 하여 특정 테스트에만 집중할 수 있었습니다.

__Junit__ 은 자바용 단위 테스트 작성을 위한 프레임워크 입니다. 
해당 프로젝트에서는 4버전을 사용하였으나, 5버전이 나오며 꽤 많은 차이가 생겼습니다.

#### JUnit 4
 - All in One, 단일 jar
 
#### JUnit 5
 - JUnit Platform, JUnit Jupiter, JUnit Vintage 모듈로 구성되어 있습니다. 
 - 테스트 작성자를 위한 API 모듈과 테스트 실행을 위한 API가 분리되어 있다.
   * 예를 들어, JUnit Jupiter는 테스트 코드 작성에 필요한 junit-jupiter-api 모듈과 테스트 실행을 위한 junit-jupiter-engine 모듈로 분리되어 있다.
 - 자바 8 이상 버전 요구

테스트 코드를 작성하며 마주한 어려움으로 __private method 에 대한 test__ 를 어떻게 해야할 지 몰랐었습니다.
그래서 다음과 같은 방법으로 해결이 가능하다는 것을 확인했습니다.
 - private method 를 호출하는 public method 를 테스트 함으로써 해결
 - Reflection 을 활용 하여 해결
 - Spock 테스트 프레임워크 사용

> 출처 : https://stackoverflow.com/questions/34571/how-do-i-test-a-private-function-or-a-class-that-has-private-methods-fields-or

위 프로젝트에서는 첫 번째 방법으로 해결하였습니다.

Reflection 이란 객체를 통해 클래스의 정보를 분석해 내는 프로그램 기법인데, 생소하고 내용이 어려워 완벽히 이해되지 않았습니다.

세 번째 방법으로 Spock 프레임워크를 사용하는 것이 있습니다. 
Spock 프레임워크는 Groovy 언어 기반이기 때문에 private method 에 대한 테스트를 수행할 수 있습니다.
[관련 자료](https://stackoverflow.com/questions/26464980/why-can-test-private-methods-fields-in-spock-without-problems)

테스트 작성에 있어 __테스트 메서드 이름을 알기 쉽게 적기 힘들거나, Mock 객체 사용이 쉽지 않았던 점, JUnit, Hamcrest, Mockito를 전부 알아야 하는 점__ 등의 어려움이 있었습니다.

하지만 Spock 프레임 워크를 사용하면 다음과 같은 보다 직관적인 코드 작성이 가능했습니다.

```groovy
def "정상적인 사용자 정보를 로딩할 때 반환된 객체 타입과 정보 확인"() {  
    setup:
    def email = "test@mail"
    def user = new User()
    user.email = email

    def mockUserRepo = Mock(UserRepository.class)
    def service = new PylonUserDetailsService(mockUserRepo)

    when:
    def userDetails = service.loadUserByUsername(email)

    then:
    mockUserRepo.findByEmail(email) >> user
    userDetails instanceof PylonUser
    userDetails.username == "whiteship"
}
```
> 출처 : https://d2.naver.com/helloworld/568425                                   

그래서 앞으로의 테스트 작성에 있어 Spock 을 공부하여 보다 빠른 개발이 가능하도록 개선하려고 합니다.