package dev.sasikanth.twine.common.testing.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class TwineTestRunner : AndroidJUnitRunner() {

  override fun newApplication(
    cl: ClassLoader?,
    className: String?,
    context: Context?
  ): Application {
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }
}
