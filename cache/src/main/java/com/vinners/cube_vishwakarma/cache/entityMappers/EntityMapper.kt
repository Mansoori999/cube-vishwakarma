package com.vinners.cube_vishwakarma.cache.entityMappers

import com.vinners.cube_vishwakarma.cache.entities.CachedProfile

interface EntityMapper<FROM, TO> {

    fun mapFromCached(type: CachedProfile): TO

    fun mapToCached(type: TO): FROM
}








