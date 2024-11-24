package com.example.diybill.ui.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import com.example.diybill.utils.saveCache

data class ChooseFile(val input: String = "image/*", val callback: (String) -> Unit)

open class Picker:ActivityResultContract<ChooseFile,Uri?>(){

    private var input = ChooseFile{}

    @CallSuper
    override fun createIntent(context: Context, input: ChooseFile): Intent {
        this.input = input
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input.input)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false)
    }

    final override fun getSynchronousResult(
        context: Context,
        input: ChooseFile
    ): SynchronousResult<Uri?>? = null

    final override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        val result = intent.takeIf {
            resultCode == Activity.RESULT_OK
        }?.data
        if (result!=null){
            val res = result.saveCache()
            if(res!=null){
                input.callback(res)
            }
        }
        return result
    }

}