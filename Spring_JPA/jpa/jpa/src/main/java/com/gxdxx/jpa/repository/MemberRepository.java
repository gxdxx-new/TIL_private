package com.gxdxx.jpa.repository;

import com.gxdxx.jpa.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // 컴포넌트 스캔으로 인해 자동으로 스프링 빈으로 관리됨
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;   // 스프링이 엔티티 매니져를 만들어서 주입해준다.

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);   // id에 맞는 member를 찾아서 반환해준다.
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)   // JPQL은 엔티티 객체를 대상으로 쿼리를 실행한다.
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)  // 파라미터를 바인딩한다.
                .setParameter("name", name)
                .getResultList();
    }
}
