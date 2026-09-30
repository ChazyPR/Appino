package tv.own.owntv.features.live

import tv.own.owntv.core.customize.CustomizeKeys
import tv.own.owntv.core.customize.SectionCustomizations
import tv.own.owntv.core.database.entity.ChannelEntity

/**
 * Channel numbers are stored in the existing per-profile customization map under a reserved key.
 * This keeps them stable across playlist refreshes and includes them in the existing backup/export
 * path without requiring a database migration or a newer core-library schema.
 */
internal const val CHANNEL_NUMBER_KEY_PREFIX = "channel-number:"

internal fun channelNumberCustomizationKey(channel: ChannelEntity): String =
    CHANNEL_NUMBER_KEY_PREFIX + CustomizeKeys.channel(channel)

internal fun SectionCustomizations.channelNumberOverride(channel: ChannelEntity): Int? =
    itemNames[channelNumberCustomizationKey(channel)]?.toIntOrNull()

internal fun SectionCustomizations.hasChannelNumberOverride(channel: ChannelEntity): Boolean =
    channelNumberCustomizationKey(channel) in itemNames

/** Applies both user-visible Live TV overrides in one place. */
internal fun SectionCustomizations.applyToChannel(channel: ChannelEntity): ChannelEntity {
    val key = CustomizeKeys.channel(channel)
    val customName = itemNames[key]
    val customNumber = channelNumberOverride(channel)
    return if (customName == null && customNumber == null) channel
    else channel.copy(name = customName ?: channel.name, number = customNumber ?: channel.number)
}
