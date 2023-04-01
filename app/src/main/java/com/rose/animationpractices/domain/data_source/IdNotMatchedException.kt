package com.rose.animationpractices.domain.data_source

class IdNotMatchedException(id: String) : Exception("Can not found data with id $id")