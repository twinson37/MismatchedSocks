package MismatchedSocks.socksAI.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Img {
    @Id
    @GeneratedValue
    @Column(name = "img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;

    private String UUID;
    private String name;

    public void setMember(Member member) {
        this.member = member;
        member.getImg().add(this);
    }
}