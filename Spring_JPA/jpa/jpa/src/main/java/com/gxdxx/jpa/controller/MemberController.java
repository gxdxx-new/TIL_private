package com.gxdxx.jpa.controller;

import com.gxdxx.jpa.domain.Address;
import com.gxdxx.jpa.domain.Member;
import com.gxdxx.jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // 컨트롤러에서 뷰로 넘어갈 때 데이터를 넘긴다.
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {  // @Valid가 MemberForm에 설정한 validation들을 확인해준다., form에 오류가 있으면 BindingResult에 담겨서 실행된다.

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); // API를 만들 때는 엔티티를 외부로 반환하면 안된다.
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
