package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UsuarioDto;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.Usuario;
import com.example.demo.service.IUsuarioService;
import com.example.demo.service.impl.UserDetailsServiceImpl;
import com.example.demo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/authentication")
	public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
	
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
	} catch (BadCredentialsException e) {
		throw new Exception("Incorrect username or password, e");
	}
		UserDetails usuario = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String jwt = "Bearer "+jwtUtil.generateToken(usuario);
		return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.ACCEPTED);	
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> createAccount(@RequestBody UsuarioDto usuarioDto){
		Usuario usuario = usuarioService.save(usuarioDto);
		usuario.setPassword(null);
		return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
	}
	
}
