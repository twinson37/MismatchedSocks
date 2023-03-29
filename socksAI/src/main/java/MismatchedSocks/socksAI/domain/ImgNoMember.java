package MismatchedSocks.socksAI.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter


public class ImgNoMember {
    @Id
    @GeneratedValue
    @Column(name = "img_id")
    private Long id;

    private String UUID;
    private String name;
}
