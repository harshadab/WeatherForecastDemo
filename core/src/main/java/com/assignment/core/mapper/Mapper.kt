package com.assignment.core.mapper

interface Mapper<T, R> {
    fun map(data: T): R
}
