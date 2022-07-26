package dev.sasikanth.twine.common.testing.auth

import dev.sasikanth.twine.auth.AuthManager
import dev.sasikanth.twine.auth.TwineAuthState
import dev.sasikanth.twine.auth.TwineAuthState.FAILED_TO_LOGIN
import dev.sasikanth.twine.auth.TwineAuthState.IDLE
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_IN
import dev.sasikanth.twine.auth.TwineAuthState.LOGGED_OUT
import dev.sasikanth.twine.auth.TwineLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeAuthManager : AuthManager {

  private val _authState = MutableStateFlow(IDLE)
  override val authState: StateFlow<TwineAuthState>
    get() = _authState.asStateFlow()

  override fun buildTwineLoginActivityResult(): TwineLogin? {
    return null
  }

  override suspend fun onLoginResult(result: TwineLogin.Result?) {
    _authState.update {
      when {
        result?.response != null -> LOGGED_IN
        result?.error != null -> FAILED_TO_LOGIN
        else -> LOGGED_OUT
      }
    }
  }

  override suspend fun logout() {
    _authState.update { LOGGED_OUT }
  }
}
