package dev.sasikanth.twine.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
  indices = [
    Index("tweet_id")
  ],
  foreignKeys = [
    ForeignKey(
      entity = Tweet::class,
      parentColumns = ["id"],
      childColumns = ["tweet_id"],
      onDelete = CASCADE
    )
  ],
  primaryKeys = [
    "tweet_id",
    "start",
    "end"
  ]
)
data class TweetEntity(
  @ColumnInfo(name = "tweet_id")
  val tweetId: String,
  val start: Int,
  val end: Int,
  @ColumnInfo(name = "display_url")
  val displayUrl: String,
  @ColumnInfo(name = "expanded_url")
  val expandedUrl: String
) {
  companion object
}
