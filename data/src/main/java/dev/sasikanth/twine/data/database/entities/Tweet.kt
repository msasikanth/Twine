package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.sasikanth.twine.data.api.models.PublicMetrics
import java.time.Instant

@Entity(
  indices = [
    Index("conversation_id")
  ]
)
data class Tweet(
  @PrimaryKey
  val id: String,
  @ColumnInfo(name = "author_id")
  val authorId: String,
  @ColumnInfo(name = "conversation_id")
  val conversationId: String,
  @ColumnInfo(name = "in_reply_to_user_id")
  val inReplyToUserId: String?,
  val text: String,
  @ColumnInfo(name = "created_at")
  val createdAt: Instant,
  @ColumnInfo(name = "device_created_at")
  val deviceCreatedAt: Instant,
  @Embedded
  val publicMetrics: PublicMetrics
) {
  companion object
}
