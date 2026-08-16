package com.omslab.weather.database
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.omslab.weather.data.models.TableModel
import com.omslab.weather.data.models.UserLocationTableModel
import com.omslab.weather.common.util.Constants.LocationTable
import com.omslab.weather.common.util.Constants.UserTable
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDAO {

    @Query("SELECT * FROM $UserTable ORDER BY id ASC")
    fun getUser(): Flow<List<TableModel>>
    @Insert(onConflict = IGNORE)
   suspend fun register(user: TableModel) :Long

    @Query("SELECT * FROM $UserTable WHERE Id =:userId")
    fun getUserById(userId: Int): Flow<List<TableModel>>

    @Query("SELECT * FROM $UserTable WHERE email =:email AND password =:password")
    fun getUser(email:String,password:String): Flow<List<TableModel>>

    //-------------------------
    @Insert(onConflict = IGNORE)
    suspend fun insertLocationData(userLocationTableModel: UserLocationTableModel)

    @Query("SELECT * FROM $LocationTable WHERE email =:email")
    fun getStoredLocation(email:String): Flow<List<UserLocationTableModel>>

    //------------------------
    @Query("DELETE FROM $UserTable")
    fun clearDb(): Flow<Unit>

}
