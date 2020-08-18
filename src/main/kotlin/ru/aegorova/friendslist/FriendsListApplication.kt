package ru.aegorova.friendslist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class FriendsListApplication

fun main(args: Array<String>) {
    runApplication<FriendsListApplication>(*args)
}
