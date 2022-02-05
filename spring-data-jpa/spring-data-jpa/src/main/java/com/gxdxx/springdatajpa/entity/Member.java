package com.gxdxx.springdatajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA가 프록시를 쓸 때를 위해 protected로 생성해놔야 한다.
@ToString(of = {"id", "username", "age"})
@NamedQuery(    // 실무에서 거의 사용안함, 애플리케이션 로딩 시점에 쿼리를 파싱해서 오류가 있으면 에러를 발생시킨다.
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);    // Team에 있는 Members 컬렉션을 조회해서 Member 객체의 인스턴스를 저장
    }
}