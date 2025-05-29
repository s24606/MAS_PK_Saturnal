package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typMiejsca;
import corp.bs.mm.masmp5.enums.typOsoby;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long osobaId;

    @ElementCollection(targetClass = typOsoby.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "osoba_role", joinColumns = @JoinColumn(name = "osoba_id"))
    @Column(name = "rola")
    @UniqueElements(message = "role w osobie nie mogą się powtarzać")
    @NotEmpty(message = "Lista ról nie może być pusta")
    private List<typOsoby> role = new ArrayList<>();

    @NotBlank
    private String imie;

    @NotBlank
    private String nazwisko;

    @NotBlank
    @Email(message = "Nieprawidłowy adres e-mail")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{9,14}", message = "Numer telefonu musi byc ciagiem cyfr o dlugosci od 9 do 14 znakow")
    private String telefon;

    @OneToMany(mappedBy = "kupujacy", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Okaziciel> okaziciele = new HashSet<>();

}
