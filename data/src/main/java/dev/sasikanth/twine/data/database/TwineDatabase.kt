package dev.sasikanth.twine.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sasikanth.twine.data.database.entities.Tweet
import dev.sasikanth.twine.data.util.LocalDatetimeConverter

@Database(
  version = 1,
  entities = [
    Tweet::class
  ]
)
@TypeConverters(
  LocalDatetimeConverter::class
)
abstract class TwineDatabase : RoomDatabase() {
}