package com.omslab.weather.data.mapper

import com.omslab.weather.data.models.TableModel
import com.omslab.weather.domain.models.User

object UserMapper {
    fun mapToDomain(model: TableModel): User {
        return User(
            id = model.Id,
            name = model.name,
            email = model.email,
            password = model.password
        )
    }

    fun mapToData(user: User): TableModel {
        return TableModel(
            name = user.name,
            email = user.email,
            password = user.password
        ).apply {
            Id = user.id
        }
    }

    fun mapToDomainList(models: List<TableModel>): List<User> {
        return models.map { mapToDomain(it) }
    }
}