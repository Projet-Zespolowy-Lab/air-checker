import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.FileOutputStream

fun getDatabase(context: Context, dbName: String): SQLiteDatabase {
  val dbFile = context.getDatabasePath(dbName)

  if (!dbFile.exists()) {
    // Kopiowanie bazy danych z assets do wewnętrznego katalogu aplikacji
    context.assets.open(dbName).use { inputStream ->
      FileOutputStream(dbFile).use { outputStream ->
        inputStream.copyTo(outputStream)
      }
    }
    println("Baza danych została skopiowana do: ${dbFile.absolutePath}")
  }

  // Otwarcie bazy danych
  return SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READWRITE)
}


fun readRecordsFromDatabase(context: Context): List<Map<String, Any>> {
  val dbName = "air_checker.db"
  val dbPath = context.getDatabasePath(dbName).absolutePath
  val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)

  val cursor = db.rawQuery("SELECT * FROM MeasureHistory", null)
  val results = mutableListOf<Map<String, Any>>()

  // Iteracja przez rekordy
  while (cursor.moveToNext()) {
    val record = mapOf(
      "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")),
      "krajowyIndeks" to cursor.getDouble(cursor.getColumnIndexOrThrow("krajowyIndeks")),
      "kolor" to cursor.getString(cursor.getColumnIndexOrThrow("kolor")),
      "timestamp" to cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
    )
    results.add(record)
  }

  cursor.close()
  db.close()
  return results
}

