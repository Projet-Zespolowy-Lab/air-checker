import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.air_checker.database.Measure
import com.example.air_checker.database.MeasureHistory
import com.example.air_checker.model.Place
import java.io.FileOutputStream

// Nazwa bazy danych
// Fizycznie plik jest w .../air-checker/app/src/main/assets/air_checker.db
const val dbName = "air_checker.db"

fun getDatabase(context: Context): SQLiteDatabase {

  val dbFile = context.getDatabasePath(dbName)

  Log.d("baza","Baza danych znajduje się pod ścieżką: ${dbFile.absolutePath}")
  if (!dbFile.exists()) {
    // Kopiowanie bazy danych z assets do wewnętrznego katalogu aplikacji
    context.assets.open(dbName).use { inputStream ->
      FileOutputStream(dbFile).use { outputStream ->
        inputStream.copyTo(outputStream)
      }
    }
    Log.d("baza", "Baza danych została skopiowana do: ${dbFile.absolutePath}")
  }

  // Otwarcie bazy danych
  return SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READWRITE)
}


fun readRecordsFromDatabase(context: Context): MeasureHistory {
  val dbName = "air_checker.db"
  val dbPath = context.getDatabasePath(dbName).absolutePath
  val db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)

  val cursor = db.rawQuery("SELECT ID, place, qualityIndex, qualityCategory, color, pm10, pm25, no2, so2, o3, timestamp FROM MeasureHistory", null)
  val measures = mutableListOf<Measure>()

  // Iteracja przez rekordy
  while (cursor.moveToNext()) {
    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
    val place = cursor.getString(cursor.getColumnIndexOrThrow("place"))
    val qualityIndex = cursor.getDouble(cursor.getColumnIndexOrThrow("qualityIndex"))
    val qualityCategory = cursor.getString(cursor.getColumnIndexOrThrow("qualityCategory"))
    val color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
    val pm10 = cursor.getString(cursor.getColumnIndexOrThrow("pm10"))
    val pm25 = cursor.getString(cursor.getColumnIndexOrThrow("pm25"))
    val no2 = cursor.getString(cursor.getColumnIndexOrThrow("no2"))
    val so2 = cursor.getString(cursor.getColumnIndexOrThrow("so2"))
    val o3 = cursor.getString(cursor.getColumnIndexOrThrow("o3"))
    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))

    // Tworzymy obiekt Measure i dodajemy do listy
    measures.add(Measure(id, place, qualityIndex, qualityCategory, color, pm10, pm25, no2, so2, o3, timestamp))
  }

  cursor.close()
  db.close()

  // Zwracamy jako MeasureHistory
  return MeasureHistory(measures)
}

fun insertRecordToDatabase(context: Context, measure: Measure) {
  val db = getDatabase(context)  // Otwórz bazę danych do zapisu

  val query = """
        INSERT INTO MeasureHistory (place, qualityIndex, qualityCategory, color, pm10, pm25, no2, so2, o3)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

  val statement = db.compileStatement(query)
  statement.bindString(1, measure.place)
  measure.qualityIndex?.let { statement.bindDouble(2, it) }
  statement.bindString(3, measure.qualityCategory)
  statement.bindString(4, measure.color)
  statement.bindString(5, measure.pm10)
  statement.bindString(6, measure.pm25)
  statement.bindString(7, measure.no2)
  statement.bindString(8, measure.so2)
  statement.bindString(9, measure.o3)

  statement.executeInsert()  // Wykonanie zapytania
  db.close()

  Log.d("baza", "Nowy rekord został dodany do bazy danych.")
}


fun deleteFromDatabase(context: Context, id: Int) {
  val db = getDatabase(context)  // Otwórz bazę danych do zapisu

  val query = """DELETE FROM MeasureHistory WHERE ID = ?""".trimIndent()

  val statement = db.compileStatement(query)
  statement.bindString(1, id.toString())


  statement.executeUpdateDelete()  // Wykonanie zapytania
  db.close()

  Log.d("baza",  "Rekord o id $id został usunięty z bazy danych, o ile istniał.")
}

fun fetchAllPlaces(context: Context): List<Place> {
  val db = getDatabase(context)  // Otwórz bazę danych do zapisu
  val places = mutableListOf<Place>()

  val query = "SELECT name, district, voivodeship, lat, lon FROM Places;"

  val cursor = db.rawQuery(query, null)

  cursor.use { cursor ->
    if (cursor.moveToFirst()) {
      do {
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val district = cursor.getString(cursor.getColumnIndexOrThrow("district"))
        val voivodeship = cursor.getString(cursor.getColumnIndexOrThrow("voivodeship"))
        val lat = cursor.getDouble(cursor.getColumnIndexOrThrow("lat"))
        val lon = cursor.getDouble(cursor.getColumnIndexOrThrow("lon"))

        val place = Place(name, district, voivodeship, lat, lon)
        places.add(place)
      } while (cursor.moveToNext())
    }
  }
  db.close()
  return places
}