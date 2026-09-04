package com.ricardo.bankchallenge2.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ricardo.bankchallenge2.entities.enums.AccountType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_accounts")
public class Account implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer agencyNumber;
	private Integer accountNumber;
	@JsonIgnore
	private String password;
	private String address;
	private String phoneNumber;
	private String email;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	private String accountHolder;
	private String cardNumber;
	private String cardExpirationDate;
	@JsonIgnore
	private String cardCVV;
	private boolean cardBlocked;
	private Double investedBalance;
	private Double balance;
	private Double creditLimit;
	private Double interestRate;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Transaction> transactions;

	public Account(){
	}
	
	public Account(Long id, Integer agencyNumber, Integer accountNumber, String password, String address, String phoneNumber,
			String email, AccountType accountType, String accountHolder, Double balance) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.agencyNumber = agencyNumber;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.accountType = accountType;
		this.accountHolder = accountHolder;
		this.cardNumber = generateCardNumber();
		this.cardExpirationDate = generateExpirationDate();
		this.cardCVV = generateCVV();
		this.cardBlocked = false;
		this.investedBalance = 0.0;
		this.balance = balance;
		this.transactions = new ArrayList<>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getInvestedBalance() {
		return investedBalance;
	}

	public void setInvestedBalance(Double investedBalance) {
		this.investedBalance = investedBalance;
	}

	private String generateExpirationDate() {
		LocalDate date = LocalDate.now().plusYears(5);
		return String.format("%02d/%02d", date.getMonthValue(), date.getYear() % 100);
	}

	private String generateCardNumber() {
		Random random = new Random();
		return String.format("%04d %04d %04d %04d", random.nextInt(10000), random.nextInt(10000), random.nextInt(10000),
				random.nextInt(10000));
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardExpirationDate() {
		return cardExpirationDate;
	}

	public String getCardCVV() {
		return cardCVV;
	}

	private String generateCVV() {
		Random random = new Random();
		return String.format("%03d", random.nextInt(1000));
	}

	public boolean isCardBlocked() {
		return cardBlocked;
	}

	public void blockCard() {
		this.cardBlocked = true;
	}

	public void unblockCard() {
		this.cardBlocked = false;
	}

	public Integer getAgencyNumber() {
		return agencyNumber;
	}

	public void setAgencyNumber(Integer agencyNumber) {
		this.agencyNumber = agencyNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public void withdraw(double amount) {
		balance -= amount;
	}

	public void deposit(double amount) {
		balance += amount;
	}
	
	public void increaseCreditLimit(Double increaseAmount) {
		this.creditLimit += increaseAmount;
	}
	
	public void updateBalance() {
		balance += balance * interestRate;
	}

	public void addTransaction(String type, Double amount) {
	    Transaction transaction = new Transaction(type, amount, this);
	    transactions.add(transaction);
	}

	public List<Transaction> getTransactions() {
		return transactions;
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
		Account other = (Account) obj;
		return Objects.equals(id, other.id);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Account Details:\n");
		sb.append("Agency Number: ").append(agencyNumber).append("\n");
		sb.append("Account Number: ").append(accountNumber).append("\n");
		sb.append("Account Type: ").append(accountType).append("\n");
		sb.append("Account Holder: ").append(accountHolder).append("\n");
		return sb.toString();
	}

}
