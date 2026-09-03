package com.ricardo.bankchallenge2.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@PostMapping
	public ResponseEntity<Account> insert(@RequestBody Account account){
	    account = service.insert(account);
	    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(account.getId()).toUri();
	    return ResponseEntity.created(uri).body(account);
	}

}
