package com.ohgiraffers.mockito.section01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.util.Assert;


@ExtendWith(MockitoExtension.class)
class CustomerTest {
    /* 단위 목킹 */
    /*
    * 단위 모킹은 소프트웨어 개발에서 사용되는 테스트 기법 중 하나이다.
    * 해당 기법은 소프트웨어의 각 부분을 개별적으로 테스트하는 것을 중점으로 하고 있으며
    * 외부 의존성을 제거하여 격리된 환경에서 테스트를 진행하는 것에 목적을 두고 있다.
    * 단위 모킹의 핵심은 테스트 대상이 되는 코드의 외부 의존성을 가짜 객체로 대체하는
    * mock 객체를 생성하는 것이 핵심이다.
    * */

    /*
    * InjectMocks
    * 테스트 대상 객체에 대한 의존성 주입을 자동으로 처리하는 메소드로
    * 주입 받을 필드에 @InjectMocks 어노테이션으로 표시한다.
    * 테스트 대상 객체의 의존성을 자동으로 주입하여 테스트를 간단하게 설정할 수 있으며
    * 객체가 필요로 하는 모든 의존성을 주입하기 때문에 복잡한 의존성 구조를 간단하게 설정할 수 있다.
    *
    * 의존성에 필요한 모의 객체는 @Mock으로 생성해야 한다.
    * */
    @InjectMocks
    private Customer customer;

    /* 가짜 mock을 생성한다.
    * section01의 구조는 customer <- accountInformation 객체를 주입받아야 한다.
    * 기존의 테스트 방식으로 진행하면 beforeAll 과정에 accountInformation 객체를 생성해서 할당해야 하지만
    * 이렇게 진행되는 경우 테스트의 커버리지는 늘어나게 되지만 단위 테스트 진행에는 맞지 않는 내용이다.
    * 아래의 mock은 테스트 대상에서 필요한 객체의 가짜 객체를 생성하여 격리된 환경에서 테스트를 진행할 수 있도록 한다.
    * */
    @Mock
    private AccountInformation accountInformation;

    @Mock
    private Bank bank;

    // 모의 객체 생성
    /*
    * customer.payments()는 아래의 코드를 실행한 result 결과의 값을 기준으로 성공여부를 판단한다.
    * int result = this.accountInformation.getBalance() - amount;
    *
    * 위 코드를 테스트 하기 위해서는 일반적인 방법은 AccountInformation을 생성하고 해당 객체에 값을 주입하여
    * 연산을 수행해야 하지만 격리된 환경에서 payments 메서드만 테스트 한다고 볼 수 없으나
    * mock 객체를 이용하여 가짜 객체를 생성하여 주입하게 되면 해당 메서드의 실행한 결과만을 가지고 테스트를 수행하는 것이 가능하다.
    * */
    @Test
    void 일반_결제_테스트(){
        String result = customer.payments(100);
        Assertions.assertEquals("결제가 실패하였습니다.", result);
    }

    // stubbing (스터빙) .thenReturn(값)
    /*
    * 스터빙은 테스트 중인 코드에서 특정 메서드 호출에 대한 반환 값을 설정하는 것을 의미하며 일반적으로 테스트 케이스에서 mock 객체를 사용할 때 사용한다.
    * 테스트 중에 호출되는 메서드가 외부 의존성이나 환경에 의해 원하는 대로 동작을 하지 않을때, 테스트의 반환값을 제어하여 원하는 결과를 얻기 위해서 사용한다.
    * 스터빙을 통해 가짜로 대체된 메서드나 객체에 대한 호출에 대해 특정한 동작을 지정할 수 있다.
    *
    * 그러나 스터빙을 많이 사용하는 것은 해당 메서드가 많은 모듈에 의존성을 맺고 있다는 것을 의미하며 이는 메서드의 책임이 분리되지 않았을 수 있다.
    * 그래서 스터빙은 많이 사용하지 않는 것이 좋은 방법이다.
    * */
    @Test
    void 스터빙_테스트(){
        Mockito.when(accountInformation.getBalance()).thenReturn(1000);
        String result = customer.payments(500);

        Assertions.assertEquals("결제가 성공하였습니다.", result);
    }

    /*
     * 호출 횟수 테스트 Mockito.times()
     * 해당 모듈에서 연관관계를 맺고 있는 객체를 테스트하는 시점에서 몇번 호출을 시도하는지
     * 확인하기 위해 사용하는 메서드이다.
     *
     * 검증 테스트 Verity
     * 테스트 코드에서 특정 메서드가 몇번 호출되었는지 검증하거나
     * */
    @Test
    void 호출_횟수_태스트(){
        customer.payments(1000);

        // payments를 실행하면 accountInformation의 .getBalance가 1번 호출되었음을 검증한다.
        // 기본의 default 값이 1이며  Mockito.times(1) 생략해도 무방하다.
        Mockito.verify(accountInformation /*, Mockito.times(1)*/).getBalance();
        // payments를 실행하면 accountInformation의 .getAccount()가 호출되지 않았음을 검증
        Mockito.verify(accountInformation, Mockito.never()).getAccount();

    }


    /* 인수 일치자를 사용하지 않는 경우
    * 인수 일치자를 사용하지 않는 경우 Mockito.when에서 설정한 것과 같이 동일한 매개변수를 전달해야
    * thenReturn()에서 반환하는 값이 출력되는데 만약 여기서 매개 변수의 값을 다르게 하는 경우 사전에
    * 정의한 매개변수의 값이 달라 오류가 발생하게 된다.
    * */
    @Test
    void 인수_일치자_안쓰는_경우_테스트(){

        int[] moneys = {1000, 2000};

        // 1. anyInt() 사용 (테스트 메서드 외부로 이동)
        Mockito.when(accountInformation.deposit(moneys[0],null)).thenReturn(1000);
        String result = customer.deposit(moneys[0]);

        Mockito.when(accountInformation.deposit(moneys[1], null)).thenReturn(2000);
        String result2 = customer.deposit(moneys[1]);

        Assertions.assertAll(
                () -> Assertions.assertEquals(moneys[0]+"가 입금되어 총 : 1000가 되었습니다.", result),
                () -> Assertions.assertEquals(moneys[1]+"가 입금되어 총 : 2000가 되었습니다.", result2)
        );

    }

    /* 인수 일치자 테스트 anyInt()
    * 전달되는 매개 변수의 값의 상관 없이 매칭하여 Mockito.when에서 설정한
    * .thenReturn(반환값)을 균일하게 반환하게 되어 자유로운 테스트를 진행할 수 있다.
    *
    * ArgumentMathcers.any~~()의 경우 해당 타입으로 입력되는 값에 제약을 받지 않고
    * 값을 일치시키고자 하는 경우 사용하게 되는데 실제 객체의 경우 Mockito.when을 사용할 수 없다.
    * 그래서 주로 가짜 객체에 값을 설정하는 경우 많이 사용하게 된다.
    * 아래와 같이 전달되는 매개변수를 다른 객체에 주입하는 경우 사용된다.
    * */
    @Test
    void 인수_일치자_테스트(){
        int[] moneys = {1000,2000,3000};

        // 1. anyInt() 사용 (테스트 메서드 외부로 이동)
        Mockito.when(accountInformation.deposit(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).thenReturn(1000);
        String result = customer.deposit(moneys[0]);
        String result2 = customer.deposit(moneys[1]);
        String result3 = customer.deposit(moneys[2]);

        // deposit의 매개변수가 int 형으로 입력되었음을 검증함
        Mockito.verify(accountInformation, Mockito.times(3)).deposit(ArgumentMatchers.anyInt(),ArgumentMatchers.any());

        Assertions.assertAll(
                () -> Assertions.assertEquals(moneys[0]+"가 입금되어 총 : 1000가 되었습니다.",result),
                () -> Assertions.assertEquals(moneys[1]+"가 입금되어 총 : 1000가 되었습니다.",result2),
                () -> Assertions.assertEquals(moneys[2]+"가 입금되어 총 : 1000가 되었습니다.",result3)
        );
    }

    /*
    * Mockito.doThrow
    * 해당 클래스는 특정 예외가 발생될 수 있도록 설정하여 테스트를 수행한다.
    * customer를 테스트하는 관점에서 accountInformation에서 음수가 입력되면 오류를 발생할 수 있도록 하였다.
    * 하지만 클래스의 테스트 관점은 customer에서 진행하는 것으로 accountInformation은 모의 객체임으로 음수를 입력해도
    * 모의 객체의 반환값은 없는 같으로 취급되어 반환 Exception의 따른 테스트를 수행할 수 없게 된다.
    * 위와 같이 격리된 환경에서 테스트를 진행하는 상황에서 모의 객체인 accountInformation의 반환 값을 Exception을 고정하고 테스트를 수행하도록 한다.
    * */
    @Test
    void 특정_예외를_발생(){
        Mockito.doThrow(new IllegalArgumentException()).when(accountInformation).deposit(ArgumentMatchers.anyInt(),ArgumentMatchers.any());
        Assertions.assertThrows(IllegalArgumentException.class, () -> customer.deposit(-10000), "입금 금액은 0원보다 작을 수 없습니다.");
    }

    /* 더 많은 호출이 없는지 확인 Mockito.verifyNoMoreInteractions
    * verifyNoMoreInteractions는 주로 verify와 한 세트로 사용하게 된다.
    * verify는 특정 모의 객체의 특정 메서드의 호출 회수를 확인할 때 주로 사용하지만
    * 특정 메서드외 다른 메서드 호출을 감지하지 않는 특징을 가지고 있다.
    * 이를 통해 도메인 로직에서 특정 메서드외 다른 메서드 호출이 있는지 감지하고자 하는
    * 경우 verifyNoMoreInteractions를 통해 지정된 메서드를 제외하고 다른 메서드의 호출이 있는지 감지한다.
    * */
    @Test
    void 호출_확인_테스트(){
        accountInformation.getAccount();
        accountInformation.getAccount();
        accountInformation.getAccount();

        accountInformation.getBalance();

        Mockito.verify(accountInformation,Mockito.times(3)).getAccount();
        // 아래의 코드를 주석으로 변경하면 오류가 발생하는데 위에서 times로 .getAccount()가 3번 호출될 것을 기대하였기 때문
//        Mockito.verify(accountInformation).getBalance();
        Mockito.verifyNoMoreInteractions(accountInformation);

    }
    /*
    * never()
    * 모의 객체의 메서드 호출이 없는지 검증하기 위해 사용하는 메서드이다.
    * */
    @Test
    void 모의객체_호출하지_않는_검증_테스트(){
        Mockito.when(accountInformation.deposit(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).thenReturn(1000);
        String result = customer.deposit(1000);

        Mockito.verify(accountInformation, Mockito.never()).getBalance();
    }

    /*
    * inOrder
    * 호출되는 모의 객체의 순서를 확인하기 위해 사용하는 메서드이다.
    * inorder.verify()에 메서드에서 호출하는 순서대로 기술하여 검증하게 된다.
    * 주의
    * 모의 객체에서 다른 객체를 호출하는 것은 검증이 불가능하다.
    * */
    @Test
    void 모의객체_호출순서_테스트(){
        Mockito.when(accountInformation.getAccount()).thenReturn("3333-33-33333");
        Mockito.when(accountInformation.getBalance()).thenReturn(8000);
        Mockito.when(bank.getName()).thenReturn("하나은행");

        String result = customer.accontInfo();
        InOrder inOrder = Mockito.inOrder(bank,accountInformation);
        inOrder.verify(bank).getName();
        inOrder.verify(accountInformation).getAccount();
        inOrder.verify(accountInformation).getBalance();
    }

    /*
    * 연속 호출 스터빙
    * 동일한 메서드 호출에 대해 다른 반환 값/예외를 사용하여 스터빙을 해야하는 경우 사용한다.
    * */

    @Test
    void 연속_호출_스터빙(){
        Mockito.when(accountInformation.deposit(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
                .thenReturn(1000)
                .thenThrow(new IllegalArgumentException());
        // 값의 대한 축약 표현
//        Mockito.when(accountInformation.deposit(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
//                .thenReturn(1000,200,300);
        Assertions.assertAll(
                () -> Assertions.assertEquals("1000가 입금되어 총 : 1000가 되었습니다.", customer.deposit(1000)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> customer.deposit(-1000))
        );
    }

    /*
    * 예외는 있지만 반환 타입이 없는 예외 스터빙
    * 반환 타입은 존재하지 않으나 예외는 존재하는 메서드가 있다.
    * 이러한 경우 doThrow를 이용하여 예외를 체크할 수 있다.
    * */
    @Test
    void 반환_값이_없는_스터빙(){
        // 반환 값은 없지만 예외는 있는 경우
        Mockito.doThrow(new RuntimeException()).when(accountInformation).testException();
        customer.exception();

        // 반환값이 아무것도 없는 경우
        Mockito.doNothing().when(bank).test();
        bank.test();
    }

    /*
    * ArgumentCaptor
    * Mockito에서 제공하는 기능으로, mock 객체의 메서드 호출 시 전달되는 인수를 캡처하여
    * 테스트 코드에서 접근할 수 있도록 한다.
    * 주로 메서드가 호출될 때 전달된 매개변수 값을 확인하거나 검증하는 용도로 사용한다.
    *
    * jdk 1.7부터 사용이 가능하다.
    *
    * ArgumentCaptor를
    * */
    @Test
    void 스텁_없는_테스트(){
        ArgumentCaptor<Bank> argumentCaptor = ArgumentCaptor.forClass(Bank.class);
        OtherBank otherBank = Mockito.mock(OtherBank.class);
        System.out.println(otherBank.bankInfo(bank));
        Mockito.verify(otherBank).bankInfo(argumentCaptor.capture());
    }




}
