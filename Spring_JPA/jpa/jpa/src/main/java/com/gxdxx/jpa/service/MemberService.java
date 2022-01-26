package com.gxdxx.jpa.service;

import com.gxdxx.jpa.domain.Member;
import com.gxdxx.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // 모든 데이터 변경이나 로직은 트랜잭션 안에서 이루어져야 한다. readOnly를 하면 성능최적화
@RequiredArgsConstructor    // final이 있는 필드만 가지고 생성자를 만들어 준다.
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired  // 생성자가 하나만 있는 경우엔 @Autowired가 없어도 스프링이 자동으로 주입해준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);  // db에 저장되기 전이여도 id값이 생성됨
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
