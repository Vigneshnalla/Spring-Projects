package com.vignesh.user.service.UserService.controllers;


import com.vignesh.user.service.UserService.entities.Rating;
import com.vignesh.user.service.UserService.entities.User;
import com.vignesh.user.service.UserService.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1=userService.saveUser(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }


    @GetMapping("/{userId}")
//    @CircuitBreaker(name="ratingHotelBreaker",fallbackMethod="ratingHotelFallback")
//    @Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId, HttpServletRequest request) {
        logger.info("getSingleUser userController ");

        retryCount++;
        User user1 = userService.getUser(userId);
        return ResponseEntity.ok(user1);
    }

    int retryCount=1;
    public ResponseEntity<User> ratingHotelFallback(String userId, HttpServletRequest request, Throwable ex) {
        ex.printStackTrace();
    logger.info("retry count "+retryCount);
        System.out.println("Fallback is executed because some service is down");

        User user = User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .about("This dummy user is created because some service is down")
                .userId("14")
                .build();

        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        // Logging the request method and headers
        logger.info("Request Method: {}", request.getMethod());
        request.getHeaderNames().asIterator().forEachRemaining(header ->
                logger.info("Request Header: {} = {}", header, request.getHeader(header))
        );
        List<User> users = userService.getAllUser();
        logger.info("users 73 usercontroller "+users);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{userId}/ratings/{ratingId}")
    public ResponseEntity<User> updateUserRating(@PathVariable String userId,
                                                 @PathVariable String ratingId,
                                                 @RequestBody @Valid Rating updateRating) {
        User updatedUser = userService.updateUserRating(userId, ratingId, updateRating);
        return ResponseEntity.ok(updatedUser);
    }
}
