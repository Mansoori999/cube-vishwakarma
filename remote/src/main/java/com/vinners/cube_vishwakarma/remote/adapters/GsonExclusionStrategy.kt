package com.vinners.cube_vishwakarma.remote.adapters

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.vinners.cube_vishwakarma.remote.adapters.DoNotSerialize

class GsonExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return clazz?.getAnnotation(DoNotSerialize::class.java) != null
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return false
    }
}