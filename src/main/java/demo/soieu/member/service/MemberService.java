package demo.soieu.member.service;

import demo.soieu.member.entity.Member;
import demo.soieu.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }
}
