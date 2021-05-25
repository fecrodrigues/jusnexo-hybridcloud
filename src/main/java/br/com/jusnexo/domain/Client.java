package br.com.jusnexo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "advocate", nullable = false)
    private Boolean advocate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "biography")
    private String biography;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "oabnumber")
    private String oabnumber;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Credential credential;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "clientEvaluator" }, allowSetters = true)
    private Set<ClientRating> clientRatings = new HashSet<>();

    @OneToMany(mappedBy = "clientEvaluator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "clientEvaluator" }, allowSetters = true)
    private Set<ClientRating> clientEvaluatorRatings = new HashSet<>();

    @ManyToMany(mappedBy = "clients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Set<AreaOfExpertise> areas = new HashSet<>();

    @ManyToMany(mappedBy = "clientReceivers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messages", "clientReceivers", "clientSenders" }, allowSetters = true)
    private Set<Chat> chatReceivers = new HashSet<>();

    @ManyToMany(mappedBy = "clientSenders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messages", "clientReceivers", "clientSenders" }, allowSetters = true)
    private Set<Chat> chatSenders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getAdvocate() {
        return this.advocate;
    }

    public Client advocate(Boolean advocate) {
        this.advocate = advocate;
        return this;
    }

    public void setAdvocate(Boolean advocate) {
        this.advocate = advocate;
    }

    public String getBiography() {
        return this.biography;
    }

    public Client biography(String biography) {
        this.biography = biography;
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Client birthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Client firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Client lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOabnumber() {
        return this.oabnumber;
    }

    public Client oabnumber(String oabnumber) {
        this.oabnumber = oabnumber;
        return this;
    }

    public void setOabnumber(String oabnumber) {
        this.oabnumber = oabnumber;
    }

    public String getPhone() {
        return this.phone;
    }

    public Client phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Client createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Client picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Client pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Credential getCredential() {
        return this.credential;
    }

    public Client credential(Credential credential) {
        this.setCredential(credential);
        return this;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Set<ClientRating> getClientRatings() {
        return this.clientRatings;
    }

    public Client clientRatings(Set<ClientRating> clientRatings) {
        this.setClientRatings(clientRatings);
        return this;
    }

    public Client addClientRating(ClientRating clientRating) {
        this.clientRatings.add(clientRating);
        clientRating.setClient(this);
        return this;
    }

    public Client removeClientRating(ClientRating clientRating) {
        this.clientRatings.remove(clientRating);
        clientRating.setClient(null);
        return this;
    }

    public void setClientRatings(Set<ClientRating> clientRatings) {
        if (this.clientRatings != null) {
            this.clientRatings.forEach(i -> i.setClient(null));
        }
        if (clientRatings != null) {
            clientRatings.forEach(i -> i.setClient(this));
        }
        this.clientRatings = clientRatings;
    }

    public Set<ClientRating> getClientEvaluatorRatings() {
        return this.clientEvaluatorRatings;
    }

    public Client clientEvaluatorRatings(Set<ClientRating> clientRatings) {
        this.setClientEvaluatorRatings(clientRatings);
        return this;
    }

    public Client addClientEvaluatorRating(ClientRating clientRating) {
        this.clientEvaluatorRatings.add(clientRating);
        clientRating.setClientEvaluator(this);
        return this;
    }

    public Client removeClientEvaluatorRating(ClientRating clientRating) {
        this.clientEvaluatorRatings.remove(clientRating);
        clientRating.setClientEvaluator(null);
        return this;
    }

    public void setClientEvaluatorRatings(Set<ClientRating> clientRatings) {
        if (this.clientEvaluatorRatings != null) {
            this.clientEvaluatorRatings.forEach(i -> i.setClientEvaluator(null));
        }
        if (clientRatings != null) {
            clientRatings.forEach(i -> i.setClientEvaluator(this));
        }
        this.clientEvaluatorRatings = clientRatings;
    }

    public Set<AreaOfExpertise> getAreas() {
        return this.areas;
    }

    public Client areas(Set<AreaOfExpertise> areaOfExpertises) {
        this.setAreas(areaOfExpertises);
        return this;
    }

    public Client addArea(AreaOfExpertise areaOfExpertise) {
        this.areas.add(areaOfExpertise);
        areaOfExpertise.getClients().add(this);
        return this;
    }

    public Client removeArea(AreaOfExpertise areaOfExpertise) {
        this.areas.remove(areaOfExpertise);
        areaOfExpertise.getClients().remove(this);
        return this;
    }

    public void setAreas(Set<AreaOfExpertise> areaOfExpertises) {
        if (this.areas != null) {
            this.areas.forEach(i -> i.removeClient(this));
        }
        if (areaOfExpertises != null) {
            areaOfExpertises.forEach(i -> i.addClient(this));
        }
        this.areas = areaOfExpertises;
    }

    public Set<Chat> getChatReceivers() {
        return this.chatReceivers;
    }

    public Client chatReceivers(Set<Chat> chats) {
        this.setChatReceivers(chats);
        return this;
    }

    public Client addChatReceiver(Chat chat) {
        this.chatReceivers.add(chat);
        chat.getClientReceivers().add(this);
        return this;
    }

    public Client removeChatReceiver(Chat chat) {
        this.chatReceivers.remove(chat);
        chat.getClientReceivers().remove(this);
        return this;
    }

    public void setChatReceivers(Set<Chat> chats) {
        if (this.chatReceivers != null) {
            this.chatReceivers.forEach(i -> i.removeClientReceiver(this));
        }
        if (chats != null) {
            chats.forEach(i -> i.addClientReceiver(this));
        }
        this.chatReceivers = chats;
    }

    public Set<Chat> getChatSenders() {
        return this.chatSenders;
    }

    public Client chatSenders(Set<Chat> chats) {
        this.setChatSenders(chats);
        return this;
    }

    public Client addChatSender(Chat chat) {
        this.chatSenders.add(chat);
        chat.getClientSenders().add(this);
        return this;
    }

    public Client removeChatSender(Chat chat) {
        this.chatSenders.remove(chat);
        chat.getClientSenders().remove(this);
        return this;
    }

    public void setChatSenders(Set<Chat> chats) {
        if (this.chatSenders != null) {
            this.chatSenders.forEach(i -> i.removeClientSender(this));
        }
        if (chats != null) {
            chats.forEach(i -> i.addClientSender(this));
        }
        this.chatSenders = chats;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", advocate='" + getAdvocate() + "'" +
            ", biography='" + getBiography() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", oabnumber='" + getOabnumber() + "'" +
            ", phone='" + getPhone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            "}";
    }
}
