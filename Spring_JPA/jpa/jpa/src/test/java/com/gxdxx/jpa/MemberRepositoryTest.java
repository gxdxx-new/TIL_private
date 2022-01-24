package com.gxdxx.jpa;

import com.gxdxx.jpa.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void testMember() throws Exception {
        // 엔티티 매니져를 통한 모든 데이터 변경은 항상 트랜잭션 내에서 이뤄저야 되기 때문에 트랜잭션이 있어야함
        // @Transactional 어노테이션이 테스트케이스에 잇ㅇ므ㅕㄴ 테스크가 끝난뒤 바로 롤백해버린다.
        // 만약 롤백되지 않으려면 @Rollback(false) 어노테이션을 추가하면 된다.
        Member member = new Member();
        member.setUsername("memberA");

        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);    // 같은 트랜잭션 안에서 저장하고 조회하면 영속성 컨텍스트가 같기 때문에 id값이 같으면 같은 엔티티로 식별
    }
}