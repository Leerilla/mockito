package com.ohgiraffers.mockito.spy;

import com.ohgiraffers.mockito.section01.AccountInformation;
import com.ohgiraffers.mockito.section01.Bank;
import com.ohgiraffers.mockito.section01.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.BootstrapWith;

public class SpyCustomTest {

    private static Customer customer;

    /*
    * spy
    * 실제 객체를 감싸고 있는 감시자를 생성하여 실제 객체의 메서드 호출은 감시되지만 실제 구현이 실행된다.
    * 실제 객체의 일부 메서드만 호출을 감시하고 싶을때 주로 사용된다.
    * */
    private static AccountInformation accountInformation;
    @Mock
    private static Bank bank;

    @BeforeAll
    static void setUp(){
        bank = Mockito.mock(Bank.class);
        accountInformation = Mockito.spy(new AccountInformation("3333-33-3333", 0, bank));
        customer =  new Customer("김길동",20, accountInformation,bank);
    }

    /*
    * Mock VS Spy
    * mock
    * 가짜 객체를 주입하기 때문에 값을 임의로 설정해도 실제 객체에 영향을 미치지 않는다.
    * 특정 클래스를 격리하여 테스트하기에 유용하고 의존성을 제거하여 테스트 코드의 간결함을 유지할 수 있으나
    * 객체 간의 상호 작용으로 발생하는 로직의 정확한 테스트에는 어려움이 있다.
    *
    * Spy
    * 실제 객체를 감싸는 객체를 생성하기 때문에 실제 로직을 유지하고 확인할 수 있다.
    * 특정 메서드의 동작을 변경하거나 테스트할 때 유용하고, 실제 객체의 동작을 확인하여 테스트 할 수 있는 장점이 있다.
    * 그러나 격리된 테스트가 어려우며 연관된 객체를 모두 생성하여 주입해주어야 하는 단점이 존재한다.
    *
    * 일반 객체 검증 방식과 Spy의 차이점
    * 일반 객체 검증 방식의 경우 테스트 수행에서는 spy와 큰 차이는 없으나
    * 테스트 추적이 불가능하다는 단점이 있으며 오롯이 값을 기준으로 체크가 가능하다.
    * 하지만 spy의 경우 실제 객체를 감싸주는 모의 객체를 통해서 생성되어 다양한 검증이 가능해진다.
    * */
    @Test
    void spy_메서드_호출_검증_테스트(){
        // given
        Mockito.when(bank.depositRequest(ArgumentMatchers.anyString())).thenReturn("입금 요청 성공");
        int amount = 1000;

        //when
        String result = customer.deposit(amount);
        System.out.println(result);

        //then
        // Spy의 객체의 경우 다음과 같은 방식으로 검증이 안되는 경우가 있다
        // verify는 모의 객체를 검증하기 위한 수단이기 때문이다. (아래는 테스트 가능)
        Mockito.verify(accountInformation).deposit(ArgumentMatchers.anyInt(),ArgumentMatchers.any());
        Assertions.assertEquals("1000가 입금되어 총 : 1000가 되었습니다.", result);
    }


    @Test
    void spy_메서드_반환값_검증(){
        //given
        Mockito.when(bank.depositRequest(ArgumentMatchers.anyString())).thenReturn("입금 요청 성공");
        int amount = 1000;

        //when
        String result = customer.deposit(amount);

        //then
        Mockito.verify(accountInformation).deposit(ArgumentMatchers.anyInt(),ArgumentMatchers.any());

        Assertions.assertEquals("1000가 입금되어 총 : 1000가 되었습니다.", result);
        Mockito.doReturn(2000).when(accountInformation).deposit(ArgumentMatchers.anyInt(), ArgumentMatchers.any());
    }




}
