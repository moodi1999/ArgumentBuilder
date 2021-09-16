package me.moodi1999.argument_annotation.bundler

import java.io.Serializable

class MapWrapper<T>(val map: T) : Serializable where T : Map<*, *>?, T : Serializable?