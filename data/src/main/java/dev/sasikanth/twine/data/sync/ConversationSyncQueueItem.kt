package dev.sasikanth.twine.data.sync

enum class Status {
  Enqueued,
  InProgress,
  Failure
}

data class ConversationSyncQueueItem(
  val tweetId: String,
  val tweetBy: String,
  val status: Status = Status.Enqueued
) {

  fun updateStatus(status: Status): ConversationSyncQueueItem {
    return copy(status = status)
  }
}
