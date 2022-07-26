package dev.sasikanth.twine.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sasikanth.twine.common.utils.TweetLinkParser
import dev.sasikanth.twine.data.clipboard.Clipboard
import dev.sasikanth.twine.data.database.entities.RecentConversation
import dev.sasikanth.twine.data.database.repository.TweetsRepository
import dev.sasikanth.twine.data.sync.ConversationSyncQueue
import dev.sasikanth.twine.data.sync.ConversationSyncQueueItem
import dev.sasikanth.twine.data.sync.Status
import dev.sasikanth.twine.home.usecase.PagedSourceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val clipboard: Clipboard,
  private val tweetLinkParser: TweetLinkParser,
  private val conversationSyncQueue: ConversationSyncQueue,
  private val tweetsRepository: TweetsRepository,
  pagedRecentConversationsUseCase: PagedSourceUseCase<RecentConversation>
) : ViewModel() {

  private val defaultUiState = HomeUiState.DEFAULT
  private val _homeUiState = MutableStateFlow(defaultUiState)
  val homeUiState: StateFlow<HomeUiState>
    get() = _homeUiState.asStateFlow()

  val recentConversations = pagedRecentConversationsUseCase
    .invoke(
      pagingSourceFactory = { tweetsRepository.recentConversations() }
    )
    .cachedIn(viewModelScope)

  init {
    viewModelScope.launch {
      conversationSyncQueue
        .queue()
        .collect { syncQueue ->
          _homeUiState.update {
            it.onSyncQueueLoaded(syncQueue)
          }
        }
    }
  }

  fun tweetUrlChanged(tweetUrl: String?) {
    _homeUiState.update {
      it.onTweetUrlChanged(tweetUrl = tweetUrl)
    }
  }

  fun pasteUrl() {
    val clipboardContent = clipboard.content
    if (!clipboardContent.isNullOrBlank()) {
      tweetUrlChanged(tweetUrl = clipboardContent)
      validateAndSync()
    }
  }

  fun clearUrl() {
    tweetUrlChanged(tweetUrl = null)
  }

  fun validateAndSync() {
    val tweetLink = _homeUiState.value.tweetUrl.orEmpty()
    val isAValidTweetLink = tweetLinkParser.isAValidTweetUrl(tweetLink = tweetLink)

    if (!isAValidTweetLink) {
      _homeUiState.update { it.invalidUrl() }
    } else {
      val tweetId = tweetLinkParser.getId(tweetLink)!!
      val tweetBy = tweetLinkParser.getUserName(tweetLink)!!

      val conversationSyncQueueItem = ConversationSyncQueueItem(
        tweetId = tweetId,
        tweetBy = tweetBy
      )

      conversationSyncQueue.add(conversationSyncQueueItem)
      clearUrl()
    }
  }

  fun cancelSync(item: ConversationSyncQueueItem) {
    conversationSyncQueue.remove(item)
  }

  fun retrySync(item: ConversationSyncQueueItem) {
    conversationSyncQueue.remove(item)
    conversationSyncQueue.add(item.updateStatus(Status.Enqueued))
  }
}
