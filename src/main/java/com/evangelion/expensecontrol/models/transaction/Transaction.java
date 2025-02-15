package com.evangelion.expensecontrol.models.transaction;

import com.evangelion.expensecontrol.models.person.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "person_id")
    private Person person;

    public Transaction() {
    }

    public Transaction(Long id, String description, BigDecimal amount, TransactionTypeEnum type, Person person) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.person = person;
    }

    public Transaction(String description, BigDecimal amount, TransactionTypeEnum type, Person person) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
