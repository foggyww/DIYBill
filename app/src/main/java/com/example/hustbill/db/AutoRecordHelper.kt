package com.example.hustbill.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AutoRecordHelper{

    suspend fun insertAutoRecord(autoRecord: AutoRecord,ioCallback: IOCallback<Unit>){
        withContext(Dispatchers.IO){
            try {
                getAppDatabase().autoRecordDao().insertAutoRecord(
                    autoRecord
                )
                ioCallback.onCompleted(Unit)
            }catch (t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }

    suspend fun queryAutoRecord(ioCallback: IOCallback<List<AutoRecord>>){
        withContext(Dispatchers.IO){
            try {
                ioCallback.onCompleted(
                    getAppDatabase().autoRecordDao().queryAutoRecord()
                )
            }catch(t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }
}