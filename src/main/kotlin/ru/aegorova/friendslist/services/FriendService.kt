package ru.aegorova.friendslist.services

import org.springframework.stereotype.Service
import ru.aegorova.friendslist.dao.FriendDao
import ru.aegorova.friendslist.models.Friend

@Service
class FriendService(val friendDao: FriendDao) {

    // return list of all friends from db
    fun getFriends(): List<Friend> {
        return friendDao.findAll()
    }

    // save friend in db and returns full instance of Friend.class with declared id
    fun addFriend(name: String): Friend {
        return friendDao.addOne(name)
    }

    // return friend with declared id
    fun gerFriendById(id: Long): Friend? {
        return friendDao.findOneById(id)
    }
}