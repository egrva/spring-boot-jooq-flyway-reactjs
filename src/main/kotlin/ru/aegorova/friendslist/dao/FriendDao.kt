package ru.aegorova.friendslist.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.aegorova.friendslist.Tables
import ru.aegorova.friendslist.models.Friend

@Component
class FriendDao(val dsl: DSLContext) {

    // return one friend with declared id
    fun findOneById(id: Long): Friend? {
        return dsl.selectFrom(Tables.FRIEND)
                .where(Tables.FRIEND.ID.equals(id))
                .fetchOne()
                .into(Friend::class.java)

    }

    //return list of all friends in db
    fun findAll(): List<Friend> {
        return dsl.selectFrom(Tables.FRIEND).orderBy(Tables.FRIEND.ID.asc())
                .fetch()
                .map { e -> e.into(Friend::class.java) }
                .toList()
    }

    // save friend in db and return full instance of Friend.class with declared id
    fun addOne(name: String): Friend {
        return dsl.insertInto(Tables.FRIEND, Tables.FRIEND.NAME)
                .values(name)
                .returning(Tables.FRIEND.ID, Tables.FRIEND.NAME)
                .fetchOne()
                .into(Friend::class.java)
    }

}