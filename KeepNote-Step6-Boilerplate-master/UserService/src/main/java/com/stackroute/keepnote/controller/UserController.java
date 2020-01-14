package com.stackroute.keepnote.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

import io.swagger.annotations.ApiOperation;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {
	private Log log = LogFactory.getLog(getClass());

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	@Autowired
	private UserService service;
	@Autowired
	public UserController(UserService userService) {
		this.service = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/user" using HTTP POST method
	 */
	@ApiOperation("value =* Define a handler method which will create a specific user by reading the\r\n" + 
			"Serialized object from request body and save the user details in the")
	@PostMapping("/api/v1/user")
	public ResponseEntity<User> registerUser(@RequestBody User user, HttpSession session){
		log.info("registerUser : STARTED");
		User registerUser;
				try {
					user.setUserAddedDate(new Date());
					registerUser = service.registerUser(user);
					return new ResponseEntity<User>(registerUser,HttpStatus.CREATED);
				} catch (UserAlreadyExistsException e) {
					return new ResponseEntity<User>(user,HttpStatus.CONFLICT);

				}	
	}
	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the user updated successfully.
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
	 */
	@PutMapping("/api/v1/user/{id}")
	public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable("id") String id, HttpSession session){
	
		try {
			
			User updateUser = service.updateUser(user.getUserId(), user);
			if (updateUser!=null) {
				return new ResponseEntity<User>(updateUser,HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		}
		catch (Exception e) {
	            return new ResponseEntity<String>("Not Found", HttpStatus.NOT_FOUND);
	        }
	}
	/*
	 * Define a handler method which will delete a user from a database.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/api/v1/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id, HttpSession session){
		try {
			
			boolean isDelete = service.deleteUser(id);
			if (isDelete) {
				return new ResponseEntity<User>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		}
		catch (Exception e) {
	            return new ResponseEntity<String>("Not Found", HttpStatus.NOT_FOUND);
	        }
	}
	/*
	 * Define a handler method which will show details of a specific user. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user found successfully. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
	 * replaced by a valid userId without {}
	 */
	@GetMapping("/api/v1/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable String id, HttpSession session){
		try {
			
			User updateUser = service.getUserById(id);
			if (updateUser!=null) {
				return new ResponseEntity<User>(updateUser,HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		}
		catch (Exception e) {
	            return new ResponseEntity<String>("Not Found", HttpStatus.NOT_FOUND);
	        }
	}
}
