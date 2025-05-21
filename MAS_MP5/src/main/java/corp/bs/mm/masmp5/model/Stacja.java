package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Stacja {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stacjaId;

    @NotBlank
    private String nazwa;

    @NotNull
    @Min(1)
    private int tory;

    @OneToMany(mappedBy = "stacja", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Postoj> postoje = new HashSet<>();
}
