package com.newlife.springbootbuildingblocks.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.newlife.springbootbuildingblocks.entities.Order;
import com.newlife.springbootbuildingblocks.entities.User;
import com.newlife.springbootbuildingblocks.exceptions.UserNotFoundException;
import com.newlife.springbootbuildingblocks.repositories.UserRepository;
import com.newlife.springbootbuildingblocks.services.UserService;

@RestController
@RequestMapping(value = "/hateoas/users")
@Validated
public class UserHateoasController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	// getAllUsers Method
	@GetMapping
	public Resources<User> getAllUsers() throws UserNotFoundException {

		List<User> allUsers = userService.getAllUsers();

		for (User user : allUsers) {
			// hateos self link for each user
			
			Long userid = user.getUserid();
			Link selflink = ControllerLinkBuilder.linkTo(this.getClass()).slash(user).withSelfRel();
			user.add(selflink);

			//hateos relation link for each user with getallorders
			
			Resources<Order> finalOrder = ControllerLinkBuilder.methodOn(OrderHateoasController.class).getAllOrders(userid);
			Link relationlink = ControllerLinkBuilder.linkTo(finalOrder).withRel("all-orders");
			user.add(relationlink);
		}

		//self link for all users
		
		Link selflinkgetAllUsers = ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
		
		Resources<User> finalResource = new Resources<User>(allUsers, selflinkgetAllUsers);
		return finalResource;

	}

	// getUserById
	@GetMapping("/{id}")
	public Resource<User> getUserById(@PathVariable("id") @Min(1) Long id) {

		try {
			// hateos self link
			Optional<User> userOptional = userService.getUserById(id);
			User user = userOptional.get();
			Long userId = user.getUserid();
			Link selflink = ControllerLinkBuilder.linkTo(this.getClass()).slash(userId).withSelfRel();
			user.add(selflink);
			Resource<User> finalResource = new Resource<User>(user);
			return finalResource;

		} catch (UserNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}

	}
}
