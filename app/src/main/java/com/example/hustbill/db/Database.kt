package com.example.hustbill.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
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

@Entity(tableName = "bill_table", indices = [Index(value = ["date"])])
data class BaseBill(
    /**
     * 账单名称
     */
    val name:String,
    /**
     * 账单信息
     */
    val msg: String,
    /**
     * 账单种类
     */
    val type:String,
    /**
     * 账单金额
     */
    val amount:String,
    /**
     * 账单日期
     */
    val date:Int,
    /**
     * 所属账本的id
     */
    val bookId:Int,
    /**
     * 账单来源
     */
    val source:String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

@Entity(tableName = "bill_book_table", indices = [Index(value = ["name"], unique = true)])
data class BillBook(
    /**
     * 账本名字
     */
    val name: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
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

@Dao
interface BillDao{
    @Insert
    fun insertBill(baseBill: BaseBill)

    @Update
    fun updateBill(baseBill: BaseBill)

    @Delete
    fun deleteBill(baseBill: BaseBill)

    @Query("select * from bill_table where date>=:start and date<=:end and bookId=:bookId")
    fun collectBillByDate(bookId: Int,start:Int,end:Int):Flow<List<BaseBill>>

    @Query("select * from bill_table where date>=:start and date<=:end and bookId=:bookId")
    fun queryBillByDate(bookId: Int,start:Int,end:Int):List<BaseBill>
}

@Dao
interface BillBookDao{
    @Insert
    fun insertBillBook(billBook: BillBook)

    @Update
    fun updateBillBook(billBook: BillBook)

    @Delete
    fun deleteBillBook(billBook: BillBook)

    @Query("select * from bill_book_table")
    fun queryAllBillBook():List<BillBook>
}

@Database(
    version = 1, entities = [AutoRecord::class,BaseBill::class,BillBook::class], exportSchema = false
)
abstract class AppDatabase:RoomDatabase(){

    abstract fun autoRecordDao():AutoRecordDao
    abstract fun billDao():BillDao
    abstract fun billBookDao():BillBookDao

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