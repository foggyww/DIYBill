package com.example.hustbill.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hustbill.App
import kotlinx.coroutines.flow.Flow

fun getAppDatabase() = AppDatabase.getDatabase(App.CONTEXT)

@Entity(tableName = "auto_record_table")
data class AutoRecord(
    /**
     * 自动支付捕捉到的消息
     */
    val msg: String,
    /**
     * 自动支付的金额
     */
    val amount:String,
    /**
     * 包名
     */
    val packetName:String,
    /**
     * 类名
     */
    val className:String,
    /**
     * 窗口ID
     */
    val windowId:Int
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

@Dao
interface AutoRecordDao{
    @Insert
    fun insertAutoRecord(autoRecord: AutoRecord)

    @Query("select * from auto_record_table")
    fun collectAutoRecord():Flow<List<AutoRecord>>

    @Query("select * from auto_record_table")
    fun queryAutoRecord():List<AutoRecord>
}
@Database(
    version = 1, entities = [AutoRecord::class], exportSchema = false
)
abstract class AppDatabase:RoomDatabase(){

    abstract fun autoRecordDao():AutoRecordDao

    companion object{

        @Volatile
        private var instance : AppDatabase? = null

        @Synchronized
        fun getDatabase(context:Context):AppDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "user_database"
            )
                .build().apply {
                    instance = this
                }
        }

        @Synchronized
        fun clearDatabase() {
            instance = null
        }
    }
}