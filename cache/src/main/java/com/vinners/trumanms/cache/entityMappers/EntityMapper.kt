package com.vinners.trumanms.cache.entityMappers

interface EntityMapper<FROM, TO> {

    fun mapFromCached(type: FROM): TO

    fun mapToCached(type: TO): FROM
}








