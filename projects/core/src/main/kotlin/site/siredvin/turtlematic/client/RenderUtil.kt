package site.siredvin.turtlematic.client

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import site.siredvin.peripheralium.extra.dsl.rml1.RMLParser
import site.siredvin.peripheralium.extra.dsl.rml1.RMLParsingException
import site.siredvin.peripheralium.extra.dsl.rml1.RenderInstruction
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

object RenderUtil {
    private val parser: RMLParser = RMLParser()
    private val cache = CacheBuilder.newBuilder()
        .expireAfterAccess(30, TimeUnit.SECONDS)
        .maximumSize(3_000).build(CacheLoader.from(::parseRMLRawProtected))

    init {
        parser.injectDefault()
    }

    @Throws(RMLParsingException::class)
    fun parseRMLRaw(string: String): List<RenderInstruction> {
        return parser.parse(string)
    }

    private fun parseRMLRawProtected(string: String): List<RenderInstruction> {
        return try {
            parseRMLRaw(string)
        } catch (ignored: RMLParsingException) {
            emptyList()
        }
    }

    fun parseRML(string: String): List<RenderInstruction> {
        return cache.get(string)
    }
}
