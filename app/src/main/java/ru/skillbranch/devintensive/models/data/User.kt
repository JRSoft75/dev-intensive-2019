package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false
) {

    constructor(id: String) : this(id, "John", "Doe")

    constructor(builder: Builder) : this(
        builder.id,
        builder.firstName,
        builder.lastName,
        builder.avatar,
        builder.rating,
        builder.respect,
        builder.lastVisit,
        builder.isOnline
    )

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    class Builder {
        var id: String = "0"
        var firstName: String? = null
        var lastName: String? = null
        var avatar: String? = null
        var rating: Int = 0
        var respect: Int = 0
        var lastVisit: Date? = null
        var isOnline: Boolean = false

        fun id(id: String = "0") = apply {this.id = id}
        fun firstName(firstName: String?) = apply {this.firstName = firstName}
        fun lastName(lastName: String?) = apply {this.lastName = lastName}
        fun avatar(avatar: String?) = apply {this.avatar = avatar}
        fun rating(rating: Int = 0) = apply {this.rating = rating}
        fun respect(respect: Int = 0) = apply {this.respect = respect}
        fun lastVisit(lastVisit: Date? = null) = apply {this.lastVisit = lastVisit}
        fun isOnline(isOnline: Boolean = false) = apply {this.isOnline = isOnline}
        fun build() = User(this)
    }


    fun printMe() =
        println(
            """
            id: $id
            firstName: $firstName
            lastName: $lastName
            avatar: $avatar
            rating: $rating
            respect: $respect
            lastVisit: $lastVisit
            isOnline: $isOnline
        """.trimIndent()
        )

    fun toUserItem(): UserItem {
        val lastActivity = when{
            lastVisit == null -> "Еще ни разу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
            id,
            "${firstName.orEmpty()} ${lastName.orEmpty()}",
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivity,
            false,
            isOnline
        )
    }

    companion object Factory {
        private var lastId: Int = -1

        fun makeUser(fullname: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullname)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }
    }
}