package com.ricardo.bankchallenge2.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ricardo.bankchallenge2.entities.Account;
import com.ricardo.bankchallenge2.services.AccountService;

@RestController
@RequestMapping(value = "/accounts")
public class AccountResource {
	
	@Autowired
	private AccountService service;
	
	@GetMapping
	public ResponseEntity<List<Account>> findAll() {
	    List<Account> list = service.findAll();
	    return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Account> findById(@PathVariable Long id) {
	    Account account = service.findById(id);
	    return ResponseEntity.ok().body(account);
	}
	
	@PostMapping
	public ResponseEntity<Account> insert(@RequestBody Account account){
	    account = service.insert(account);
	    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account.getId()).toUri();
	    return ResponseEntity.created(uri).body(account);
	}
	
	@PostMapping("/{id}/deposit")
	public ResponseEntity<Void> deposit(@PathVariable Long id, @RequestParam Double amount) {
	    service.deposit(id, amount);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/withdraw")
	public ResponseEntity<Void> withdraw(@PathVariable Long id, @RequestParam Double amount) {
	    service.withdraw(id, amount);
	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}/balance")
	public ResponseEntity<Double> checkBalance(@PathVariable Long id) {
	    Double balance = service.checkBalance(id);
	    return ResponseEntity.ok().body(balance);
	}
	
	@GetMapping("/{id}/limit")
	public ResponseEntity<Double> checkLimit(@PathVariable Long id) {
	    Double limit = service.checkLimit(id);
	    return ResponseEntity.ok().body(limit);
	}
	
	@PostMapping("/{id}/request-limit")
	public ResponseEntity<Void> requestLimit(@PathVariable Long id, @RequestParam Double amount) {
	    service.requestLimit(id, amount);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/change-password")
	public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestParam String currentPassword, @RequestParam String newPassword) {
	    service.changePassword(id, currentPassword, newPassword);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/change-address")
	public ResponseEntity<Void> changeAddress(@PathVariable Long id, @RequestParam String newAddress) {
	    service.changeAddress(id, newAddress);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/change-contact")
	public ResponseEntity<Void> changeContactInformation(@PathVariable Long id, @RequestParam String phoneNumber, @RequestParam String email) {
	    service.changeContactInformation(id, phoneNumber, email);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/block-card")
	public ResponseEntity<Void> blockCard(@PathVariable Long id) {
	    service.blockCard(id);
	    return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/request-loan")
	public ResponseEntity<Void> requestLoan(@PathVariable Long id, @RequestParam Double amount) {
	    service.requestLoan(id, amount);
	    return ResponseEntity.noContent().build();
	}
}
