package br.com.jusnexo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "chat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chat" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_chat__client_receiver",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "client_receiver_id")
    )
    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    private Set<Client> clientReceivers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_chat__client_sender",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "client_sender_id")
    )
    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    private Set<Client> clientSenders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Chat createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public Chat messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Chat addMessage(Message message) {
        this.messages.add(message);
        message.setChat(this);
        return this;
    }

    public Chat removeMessage(Message message) {
        this.messages.remove(message);
        message.setChat(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setChat(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setChat(this));
        }
        this.messages = messages;
    }

    public Set<Client> getClientReceivers() {
        return this.clientReceivers;
    }

    public Chat clientReceivers(Set<Client> clients) {
        this.setClientReceivers(clients);
        return this;
    }

    public Chat addClientReceiver(Client client) {
        this.clientReceivers.add(client);
        client.getChatReceivers().add(this);
        return this;
    }

    public Chat removeClientReceiver(Client client) {
        this.clientReceivers.remove(client);
        client.getChatReceivers().remove(this);
        return this;
    }

    public void setClientReceivers(Set<Client> clients) {
        this.clientReceivers = clients;
    }

    public Set<Client> getClientSenders() {
        return this.clientSenders;
    }

    public Chat clientSenders(Set<Client> clients) {
        this.setClientSenders(clients);
        return this;
    }

    public Chat addClientSender(Client client) {
        this.clientSenders.add(client);
        client.getChatSenders().add(this);
        return this;
    }

    public Chat removeClientSender(Client client) {
        this.clientSenders.remove(client);
        client.getChatSenders().remove(this);
        return this;
    }

    public void setClientSenders(Set<Client> clients) {
        this.clientSenders = clients;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chat)) {
            return false;
        }
        return id != null && id.equals(((Chat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
