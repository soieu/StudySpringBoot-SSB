package demo.soieu.member;

import java.util.ArrayList;
import java.util.List;

import demo.soieu.member.entity.Member;
import demo.soieu.member.repository.MemberRepository;
import demo.soieu.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void insertRecords() {
        int batchSize = 1000;
        int transactionSize = 10000;
        int totalRecords = 900_000;

        for (int batch = 0; batch < totalRecords; batch += transactionSize) {
            insertWithSeparateTransaction(batch, Math.min(batch + transactionSize, totalRecords), batchSize);

            if (batch % 50000 == 0) {
                System.out.println("처리된 레코드: " + (batch + transactionSize));
            }
        }

        assertEquals(900_000, memberRepository.count());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void insertWithSeparateTransaction(int start, int end, int batchSize) {
        for (int i = start; i < end; i += batchSize) {
            List<Member> members = new ArrayList<>();

            int batchEnd = Math.min(i + batchSize, end);
            for (int j = i + 1; j <= batchEnd; j++) {
                Member member = Member.builder()
                        .name("사용자" + j)
                        .email("member" + j + "@example.com")
                        .build();
                members.add(member);
            }

            memberRepository.saveAll(members);
            entityManager.flush();
            entityManager.clear();
        }
    }
}
