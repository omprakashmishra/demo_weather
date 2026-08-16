package com.omslab.weather.data.dbcall.local
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

    @Query("SELECT * FROM $LocationTable WHERE email = :email ORDER BY entryDateTime DESC")
    fun getStoredLocation(email:String): Flow<List<UserLocationTableModel>>

    //------------------------
    @Query(" DELETE FROM $LocationTable WHERE email = :email AND id NOT IN (SELECT id FROM $LocationTable WHERE email = :email ORDER BY id DESC LIMIT 1)")
    suspend fun deleteOldLocations(email: String?)

    @Query("DELETE FROM $UserTable")
    suspend fun clearDb(): Int

}
