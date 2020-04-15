# TODO list console app version 3

## Introduction
![todo-list-console3](https://user-images.githubusercontent.com/35681772/73604203-0eadb280-45d0-11ea-94e2-cf901df5c92a.gif)

공부하기 위한 TODO list 콘솔 어플리케이션.
기존에 개발했던 어플리케이션을 피드백을 바탕으로 개선하여 3번째 다시 개발함. 


## 기술 스택
 * Java(version: '12.0.1')
 * JUnit(version: '4.12')
 * Jupiter(version: '5.5.2')
 
## Todo-list-console 아키텍처

Todo-list-console 은 OOP(Object-Oriented Programming) 의 관점에서 개발하려고 노력했습니다. 
또한 콘솔 단과 비즈니스 로직을 분리하여 추후에 이것이 웹 프로젝트로 변경되더라도 도메인은 변화가 없도록 설계하였습니다.
 
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
 
## 과제 요구사항
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