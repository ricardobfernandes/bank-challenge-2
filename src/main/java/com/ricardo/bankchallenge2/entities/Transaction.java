package com.ricardo.bankchallenge2.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_transactions")
public class Transaction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateTime;
	private String type;
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private Account account;

	public Transaction() {
	}
	
	public Transaction(String type, Double amount, Account account) {
		super();
		this.dateTime = LocalDateTime.now();
		this.type = type;
		this.amount = amount;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Account getAccount() {
	    return account;
	}

	public void setAccount(Account account) {
	    this.account = account;
	}

	public LocalDateTime getDateTime() {
	    return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
	    this.dateTime = dateTime;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		DateTimeFormatter fmt =	DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return dateTime.format(fmt) +";" + type + ";" + amount;
	}
}
