package com.gxdxx.springdatajpa.repository;

import com.gxdxx.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
