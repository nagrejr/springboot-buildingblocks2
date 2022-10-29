package com.newlife.springbootbuildingblocks.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.newlife.springbootbuildingblocks.entities.Order;
import com.newlife.springbootbuildingblocks.entities.User;
import com.newlife.springbootbuildingblocks.exceptions.UserNotFoundException;
import com.newlife.springbootbuildingblocks.repositories.OrderRepository;
import com.newlife.springbootbuildingblocks.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class OrderController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	// get All Orders for a user

	@GetMapping("/{userid}/orders")
	public List<Order> getAllOrders(@PathVariable Long userid) throws UserNotFoundException {

		Optional<User> userOptional = userRepository.findById(userid);
		if (!userOptional.isPresent())
			throw new UserNotFoundException("User Not Found");

		return userOptional.get().getOrders();
	}

	// Create Order

	@PostMapping("{userid}/orders")
	public Order createOrder(@PathVariable Long userid, @RequestBody Order order) throws UserNotFoundException {
		Optional<User> userOptional = userRepository.findById(userid);

		if (!userOptional.isPresent())
			throw new UserNotFoundException("User Not Found");

		User user = userOptional.get();
		order.setUser(user);
		return orderRepository.save(order);

	}

	// get order by order id
	@GetMapping("{userid}/orders/{orderid}")
	public Optional<Order> getOrderByOrderId(@PathVariable Long userid, @PathVariable Long orderid) {
		Optional<User> userOptional = userRepository.findById(userid);
		Optional<Order> orderOptional = orderRepository.findById(orderid);

		if (!userOptional.isPresent() || !orderOptional.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please provide correct user id or order id");
		return orderOptional;
	}

}
