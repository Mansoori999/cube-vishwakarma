package com.vinners.cube_vishwakarma.cache.entityMappers

interface EntityMapper<FROM, TO> {

    fun mapFromCached(type: FROM): TO

    fun mapToCached(type: TO): FROM
}








