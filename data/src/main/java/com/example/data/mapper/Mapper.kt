package com.example.data.mapper

import com.example.data.model.PersonDetailsGit
import com.example.data.model.PersonGitHub
import com.example.domain.model.Person
import com.example.domain.model.PersonDetails

internal fun PersonGitHub.toLocalModel(): Person {
    return Person(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
}

internal fun PersonDetailsGit.toLocalModel(): PersonDetails {
    return PersonDetails(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        followers = followers,
        following = following,
        repository = repository,
        experience = experience
    )
}

fun Person.toGitModel(): PersonGitHub {
    return PersonGitHub(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
}

fun PersonDetails.toGitModel(): PersonDetailsGit {
    return PersonDetailsGit(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        followers = followers,
        following = following,
        repository = repository,
        experience = experience
    )
}