package com.gxdxx.jpa.repository;

import com.gxdxx.jpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // select m from Member m where m.name = :name
    List<Member> findByName(String name);

}
