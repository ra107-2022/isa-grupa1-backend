package grupa1.jutjubic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class WatchPartyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // room id
    private String code;

    @JsonIgnore
    @ManyToOne
    private User owner;

    public  WatchPartyRoom() {}
    public WatchPartyRoom(String code, User owner) {
        this.code = code;
        this.owner = owner;
    }

    // get, set
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

}
