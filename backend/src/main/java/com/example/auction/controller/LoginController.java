package com.example.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

	@RequestMapping("/login")
	public Principal user(Principal user) {
		return user;
	}
}
