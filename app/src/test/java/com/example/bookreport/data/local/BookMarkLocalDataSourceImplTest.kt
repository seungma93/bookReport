import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*


class BookMarkLocalDataSourceImplTest {
    private lateinit var db: BookMarkDatabase
    private lateinit var dataSource: BookMarkLocalDataSourceImpl

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookMarkDatabase::class.java
        ).allowMainThreadQueries().build()

        dataSource = BookMarkLocalDataSourceImpl(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_test() = runTest {
        dataSource.insert(BookMark(""))
        verify(db.bookMarkDao(), times(1))
    }
}
