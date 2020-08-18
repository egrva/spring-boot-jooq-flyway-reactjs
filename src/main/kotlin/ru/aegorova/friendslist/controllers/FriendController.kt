package ru.aegorova.friendslist.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.aegorova.friendslist.models.Friend
import ru.aegorova.friendslist.services.FriendService

@RestController
class FriendController(val friendService: FriendService) {

    // request to get list of friends
    @GetMapping("/api/friends")
    fun getFriends(): List<Friend> {
        return friendService.getFriends();
    }

    // request to send to server new friend. It saves new friend in db and return full instance of Friend.class with declared id
    @PostMapping("/api/friends")
    fun addFriend(@RequestParam name: String): Friend {
        return friendService.addFriend(name);
    }

    // request to check connection
    @GetMapping("/ping")
    fun sayHello() : ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }
}