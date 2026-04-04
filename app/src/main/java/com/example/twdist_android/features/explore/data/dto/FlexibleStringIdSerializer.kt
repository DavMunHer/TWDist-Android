package com.example.twdist_android.features.explore.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

object FlexibleStringIdSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleStringId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            val primitive = element as? JsonPrimitive
                ?: throw SerializationException("Expected primitive JSON value for ID")

            // Accept IDs as JSON string ("1") or number (1), but reject booleans/null.
            val content = primitive.contentOrNull
                ?: throw SerializationException("ID cannot be null")

            val isValid = primitive.isString || primitive.longOrNull != null || primitive.doubleOrNull != null
            if (!isValid) {
                throw SerializationException("Expected ID as string or number, got: $content")
            }

            content
        } else {
            decoder.decodeString()
        }
    }
}
