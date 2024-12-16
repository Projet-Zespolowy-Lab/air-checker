import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.air_checker.database.Measure
import com.example.air_checker.database.MeasureHistory
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

  val cursor = db.rawQuery("SELECT ID, krajowyIndeks, kolor, timestamp FROM MeasureHistory", null)
  val measures = mutableListOf<Measure>()

  // Iteracja przez rekordy
  while (cursor.moveToNext()) {
    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
    val krajowyIndeks = cursor.getDouble(cursor.getColumnIndexOrThrow("krajowyIndeks"))
    val kolor = cursor.getString(cursor.getColumnIndexOrThrow("kolor"))
    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))

    // Tworzymy obiekt Measure i dodajemy do listy
    measures.add(Measure(id, krajowyIndeks, kolor, timestamp))
  }

  cursor.close()
  db.close()

  // Zwracamy jako MeasureHistory
  return MeasureHistory(measures)
}

fun insertRecordToDatabase(context: Context, krajowyIndeks: Double, kolor: String) {
  val db = getDatabase(context)  // Otwórz bazę danych do zapisu

  val query = """
        INSERT INTO MeasureHistory (krajowyIndeks, kolor)
        VALUES (?, ?)
    """.trimIndent()

  val statement = db.compileStatement(query)
  statement.bindDouble(1, krajowyIndeks)
  statement.bindString(2, kolor)

  statement.executeInsert()  // Wykonanie zapytania
  db.close()

  Log.d("baza", "Nowy rekord został dodany do bazy danych.")
}

