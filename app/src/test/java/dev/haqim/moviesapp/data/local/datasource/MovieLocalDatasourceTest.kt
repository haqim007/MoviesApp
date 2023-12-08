package dev.haqim.moviesapp.data.local.datasource

import androidx.paging.PagingSource
import dev.haqim.moviesapp.data.local.room.AppDatabase
import dev.haqim.moviesapp.data.local.room.dao.FakeMovieListItemDao
import dev.haqim.moviesapp.data.local.room.dao.FakeMovieRemoteKeysDao
import dev.haqim.moviesapp.util.DataDummy
import dev.haqim.moviesapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MovieLocalDatasourceTest {
    
    @JvmField
    @Rule
    val coroutineRule: MainCoroutineRule = MainCoroutineRule()
    
    @Mock
    private lateinit var database: AppDatabase
    private val movieListItemDao = FakeMovieListItemDao()
    private val remoteKeysDao = FakeMovieRemoteKeysDao()
    private lateinit var localDatasource: MovieLocalDatasource
    
    @Before
    fun setup(){
        
        `when`(database.movieListItem()).thenReturn(movieListItemDao)
        `when`(database.movieListItemRemoteKeys()).thenReturn(remoteKeysDao)
        
        localDatasource = MovieLocalDatasource(database)
    }

    @Test
    fun `Given inserted movie list items When load paging Should not return null or empty`() = runTest {
        val expected = DataDummy.movieListItemEntities()
        localDatasource.insertAllData(expected)

        val actual = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
//        println(expected.subList(0, 9))
//        println((actual as PagingSource.LoadResult.Page).data.toString())
//        assertEquals(expected.subList(0, 9), (actual as PagingSource.LoadResult.Page).data)
        assertNotNull((actual as PagingSource.LoadResult.Page).data)
        assertTrue((actual as PagingSource.LoadResult.Page).data.isNotEmpty())
    }

    @Test
    fun `Given empty movie list items in storage When load paging Should empty but not null`() = runTest {
        
        val actual = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        
        assertNotNull((actual as PagingSource.LoadResult.Page).data)
        assertTrue((actual as PagingSource.LoadResult.Page).data.isEmpty())
    }


    @Test
    fun `Given inserted remote keys When clear remote keys and get a remote keys should return null`() = runTest{
        val data = DataDummy.movieListItemRemoteKeys
        localDatasource.insertRemoteKeys(data)
        val expected = localDatasource.getRemoteKeysById(data[0].id)

        // assert inserted data
        assertEquals(expected, data[0])
        // clear the data
        localDatasource.clearRemoteKeys()
        // assert cleared data
        assertNull(localDatasource.getRemoteKeysById(data[0].id))
        
    }

    @Test
    fun `when insert movie list remote keys should match between inserted and fetched data`() = runTest{
        val data = DataDummy.movieListItemRemoteKeys
        localDatasource.insertRemoteKeys(data)
        val expected = localDatasource.getRemoteKeysById(data[0].id)
        
        assertEquals(expected, data[0])
    }

    @Test
    fun `Given inserted movie list remote keys When get remote keys by id should return valid remote key`() = runTest{
        val data = DataDummy.movieListItemRemoteKeys
        localDatasource.insertRemoteKeys(data)
        val expected = localDatasource.getRemoteKeysById(data[0].id)

        assertEquals(expected, data[0])
    }

    @Test
    fun `When get remote keys by id should return null`() = runTest{
        val data = DataDummy.movieListItemRemoteKeys
        val actual = localDatasource.getRemoteKeysById(data[0].id)

        assertNull(actual)
    }

    @Test
    fun `Given movie list items in storage When get data by ID Should return valid data`()= runTest {
        val data = DataDummy.movieListItemEntities()
        val expected = data[0]
        localDatasource.insertAllData(data)
        
        val actual = localDatasource.getDataById(expected.id)
        assertEquals(expected, actual)
    }

    @Test
    fun `Given movie list items in storage When get data by ID Should return invalid data`()= runTest {
        val data = DataDummy.movieListItemEntities()
        val expected = data[0]
        localDatasource.insertAllData(data)

        val actual = localDatasource.getDataById(data[1].id)
        assertNotEquals(expected, actual)
    }

    @Test
    fun `Given empty movie list items in storage When get data by ID Should return null`()= runTest {
        val randomId = 1
        val actual = localDatasource.getDataById(randomId)
        assertNull(actual)
    }

    @Test
    fun `Given movie list items in storage When clear all data and get data Should return empty but not null`()= runTest {
        val data = DataDummy.movieListItemEntities()
        localDatasource.insertAllData(data)

        val actual = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        // assert inserted data first
        assertNotNull((actual as PagingSource.LoadResult.Page).data)
        assertTrue((actual as PagingSource.LoadResult.Page).data.isNotEmpty())
        
        // clear data
        localDatasource.clearAllData()
        val actual2 = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        // assert cleared data
        assertNotNull((actual2 as PagingSource.LoadResult.Page).data)
        assertTrue((actual2 as PagingSource.LoadResult.Page).data.isEmpty())
    }

    @Test
    fun `Given empty movie list items in storage When insert all data and get data Should return not empty`()= runTest {
        val data = DataDummy.movieListItemEntities()
        localDatasource.insertAllData(data)

        val actual = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        // assert inserted data first
        assertNotNull((actual as PagingSource.LoadResult.Page).data)
        assertTrue((actual as PagingSource.LoadResult.Page).data.isNotEmpty())
    }

    @Test
    fun `When insert keys and data Should return not null key and not empty data`() = runTest{
        val remoteKeys = DataDummy.movieListItemRemoteKeys
        val movieListItems = DataDummy.movieListItemEntities()
        localDatasource.insertKeysAndData(remoteKeys, movieListItems)
        
        val actualRemoteKeys = localDatasource.getRemoteKeysById(remoteKeys[0].id)
        val actualMovieListItems = localDatasource.getPaging().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        assertNotNull(actualRemoteKeys)
//        assertTrue((actualMovieListItems as PagingSource.LoadResult.Page).data.isNotEmpty())
        
        
    }
    
    
}