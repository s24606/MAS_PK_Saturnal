package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.TypUlgi;
import corp.bs.mm.masmp5.enums.TypOsoby;
import corp.bs.mm.masmp5.model.constraints.RolesAttributesValidation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RolesAttributesValidation
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long osobaId;

    @ElementCollection(targetClass = TypOsoby.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "osoba_role", joinColumns = @JoinColumn(name = "osoba_id"))
    @Column(name = "rola")
    @UniqueElements(message = "role w osobie nie mogą się powtarzać")
    @NotEmpty(message = "Lista ról nie może być pusta")
    private List<TypOsoby> role = new ArrayList<>();

    @NotBlank
    private String imie;

    @NotBlank
    private String nazwisko;

    @NotBlank
    @Email(message = "Nieprawidłowy adres e-mail")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Numer telefonu może zawierać tylko cyfry")
    private String telefon;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "kupujacy", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Bilet> bilety = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Nullable
    private TypUlgi ulga;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Nullable
    private String kodSluzbowy;

    public TypUlgi getUlga(){
        if(role.contains(TypOsoby.PASAZER))
            return ulga;
        else return null;
    }
    public void setUlga(TypUlgi ulga) throws Exception {
        if(role.contains(TypOsoby.PASAZER))
            this.ulga = ulga;
        else throw new Exception("dane dostepne tylko dla obiektów z rolą PASAZER");
    }

    public String getKodSluzbowy(){
        if(role.contains(TypOsoby.PRACOWNIK))
            return kodSluzbowy;
        else return null;
    }
    public void setKodSluzbowy(String kodSluzbowy) throws Exception {
        if(role.contains(TypOsoby.PRACOWNIK))
            this.kodSluzbowy = kodSluzbowy;
        else throw new Exception("dane dostepne tylko dla obiektów z rolą PRACOWNIK");
    }

    public void makePasazer(TypUlgi ulga) throws Exception {
        if (!role.contains(TypOsoby.PASAZER)) {
            role.add(TypOsoby.PASAZER);
            this.ulga=ulga;
        }
    }
    public void makePracownik(String kodSluzbowy) throws Exception {
        if (!role.contains(TypOsoby.PRACOWNIK)) {
            role.add(TypOsoby.PRACOWNIK);
            this.kodSluzbowy = kodSluzbowy;
        }
    }

    public void degradePasazer() throws Exception {
        if (role.size() == 1 && role.contains(TypOsoby.PASAZER)) {
            throw new Exception("Nie mozna usunac roli Pasażer - osoba nie może pozostać bez żadnej roli.");
        }
        if (role.contains(TypOsoby.PASAZER)) {
            role.remove(TypOsoby.PASAZER);
            this.ulga = null;
        }
    }
    public void degradePracownik() throws Exception {
        if (role.size() == 1 && role.contains(TypOsoby.PRACOWNIK)) {
            throw new Exception("Nie mozna usunac roli Pracownik - osoba nie może pozostać bez żadnej roli.");
        }
        if (role.contains(TypOsoby.PRACOWNIK)) {
            role.remove(TypOsoby.PRACOWNIK);
            this.kodSluzbowy = null;
        }
    }

    public String przejrzyjBilety() throws Exception {
        StringBuilder result= new StringBuilder();
        if(role.contains(TypOsoby.PASAZER))
            for(Bilet b: this.bilety){
                if(result.isEmpty())
                    result.append("Bilet z ").append(b.getStacjaOdjazd().getNazwa())
                            .append(" do ").append(b.getStacjaPrzyjazd().getNazwa())
                            .append(" (cena: ").append(b.getCena())
                            .append(") - ").append(b.getStatus());
                else
                    result.append("\n").append("Bilet z ").append(b.getStacjaOdjazd().getNazwa())
                            .append(" do ").append(b.getStacjaPrzyjazd().getNazwa())
                            .append(" - cena: ").append(b.getCena())
                            .append(") - ").append(b.getStatus());
            }
        else throw new Exception("dane dostepne tylko dla obiektów z rolą PASAZER");
        return result.toString();
    }
}
