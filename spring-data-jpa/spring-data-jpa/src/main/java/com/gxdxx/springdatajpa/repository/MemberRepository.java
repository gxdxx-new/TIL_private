package com.gxdxx.springdatajpa.repository;

import com.gxdxx.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}