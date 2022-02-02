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