package com.example.diybill.utils

import android.net.Uri
import com.example.diybill.App
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

/**
 * getAppFolder 获取外部应用文件夹
 * @param child 子文件夹名
 * @return File
 */
fun getAppFile(file: String = "") = File(App.CONTEXT.getExternalFilesDir(null), file)


/**
 * deleteAll 删除该文件夹下所有文件/删除文件
 * @param keepParent bool 是否保留父文件夹
 * @return
 */
fun File.deleteAll(keepParent: Boolean = true) {
    if (isDirectory || exists()) {
        listFiles()?.forEach {
            if (it.isDirectory) it.deleteAll(false)
            else it.delete()
        }
        if (!keepParent)
            delete()
    }
}

/**
 * getAppCacheFolder 获取外部缓存文件夹
 * @return File
 */
fun getAppCacheFolder() = App.CONTEXT.externalCacheDir

fun Uri.save(folder: String): String? {
    val parent = getAppFile(folder)
    val inputStream: InputStream = App.CONTEXT.contentResolver.openInputStream(this)
        ?: return null //context的方法获取URI文件输入流
    return try {
        val bytes = inputStream.readBytes()
        val path =
            md5(bytes) + // 文件名
                    this.lastPathSegment?.substringAfterLast(".", missingDelimiterValue = "")
                        .run { if (isNullOrBlank()) "" else ".$this" } //文件后缀
        val file = File(parent, path)
        file.writeBytes(bytes)
        "$folder/$path" //成功返回路径
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }finally {
        inputStream.close()
    }
}

fun Uri.saveCache():String?{
    val parent = getAppCacheFolder()
    val inputStream: InputStream = App.CONTEXT.contentResolver.openInputStream(this)
        ?: return null //context的方法获取URI文件输入流
    return try {
        val bytes = inputStream.readBytes()
        val path =
            md5(bytes) + // 文件名
                    this.lastPathSegment?.substringAfterLast(".", missingDelimiterValue = "")
                        .run { if (isNullOrBlank()) "" else ".$this" } //文件后缀
        val file = File(parent, path)
        file.writeBytes(bytes)
        path
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }finally {
        inputStream.close()
    }
}

/**
 * AppFile App外部私有目录操作
 * readText 从指定文件中读取字符串
 * write 写入指定字符串到指定文件
 * delete 删除指定文件
 */
object AppFile {

    fun moveIn(file: File, path: String): Boolean {
        if (file.isFile) {
            val folder = getAppFile(path)
            if (!folder.isDirectory) {
                folder.delete()
                folder.mkdirs()
            }
            file.renameTo(File(folder, file.name))
            return true
        }
        return false
    }

    /**
     * readText 从指定文件中读取字符串
     *
     * @param src 文件路径
     * @param charset 字符集
     * @return
     */
    fun readText(src: String, last: Long? = null, charset: Charset = Charsets.UTF_8): String? {
        val file = getAppFile(src) ?: return null
        if (file.isFile) {
            last?.let { file.setLastModified(it) }
            return file.readText(charset)
        }
        return null
    }

    /**
     * exist 判断文件是否存在且为文件
     */
    fun exist(src: String): Boolean {
        if (src.startsWith("tmp/")) {
            return AppCache.exist(src.substring("tmp/".length))
        }
        val file = getAppFile(src) ?: return false
        return file.isFile
    }


    /**
     * readText 从指定文件中读取字符串
     *
     * @param src 文件路径
     * @param charset 字符集
     * @return
     */
    fun read(src: String, last: Long? = null): ByteArray {
        if (src.startsWith("tmp/")) {
            return AppCache.read(src.substring("tmp/".length))
        }
        val file = getAppFile(src)
        if (file.isFile) {
            last?.let { file.setLastModified(it) }
            return file.readBytes()
        }
        return ByteArray(0)
    }

    /**
     * getFile 相对路径转 File 实例
     *
     * @param src 文件路径
     * @return
     */
    fun getFile(src: String): File? {
        if (src.startsWith("tmp/")) {
            return AppCache.getFile(src.substring("tmp/".length))
        }
        val folder = getAppFile()
        if (!folder.exists()) folder.mkdirs()
        if (folder.isDirectory) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            if (file.exists()) {
                return file
            }
        }
        return null
    }

    /**
     * getURL 相对路径转 Uri 实例
     *
     * @param src 文件路径
     * @return
     */
    fun getUri(src: String): String? {
        if (src.startsWith("tmp/")) {
            return AppCache.getUri(src.substring("tmp/".length))
        }
        val folder = getAppFile()
        if (folder != null) {
            if (!folder.exists()) folder.mkdirs()
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            if (file.exists()) {
                return "file://" + file.absolutePath
            }
        }
        return null
    }


    /**
     * write 写入指定字符串到指定文件
     * @param path 指定文件路径
     * @param byteArray 需写入的内容
     * @param last 最后修改时间
     * @return
     */
    fun write(
        path: String,
        byteArray: ByteArray,
        last: Long = System.currentTimeMillis()
    ): String? {
        val folder = getAppFile()
        if (folder.isDirectory) {
            val file = File(folder, path)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            file.writeBytes(byteArray)
            file.setLastModified(last)//更新最后修改时间
            return file.absolutePath
        }
        return null
    }


    /**
     * write 写入指定字符串到指定文件
     * @param path 指定文件路径
     * @param text 需写入的字符串
     * @param charset 字符集
     * @return
     */
    fun write(
        path: String,
        text: String,
        last: Long,
        charset: Charset = Charsets.UTF_8
    ): String? {
        val folder = getAppFile()
        if (folder.isDirectory) {
            val file = File(folder, path)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            file.writeText(text, charset)
            file.setLastModified(last)//更新最后修改时间
            return file.absolutePath
        }
        return null
    }

    /**
     * delete 删除指定文件
     *
     * @param path 文件路径
     */
    fun delete(path: String) {
        val file = getAppFile(path) ?: return
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     */
    fun foreach(dirPath: String = getAppFile()?.absolutePath ?: ""): List<File> {
        val f = File(dirPath)
        if (!f.exists()) { //判断路径是否存在
            return emptyList()
        }
        val files = f.listFiles() ?: return emptyList()
        val fileList = mutableListOf<File>()
        for (_file in files) { //遍历目录
            if (_file.isFile) {
                fileList.add(_file)
            } else if (_file.isDirectory) { //查询子目录
                fileList.addAll(foreach(_file.absolutePath))
            }
        }
        return fileList
    }
}

/**
 * AppCache App缓存操作
 * writeCache 写缓存文件
 * clear 清除所有缓存文件
 */
object AppCache {

    /**
     * write 向指定缓存文件写入字符串,如果文件存在,则直接覆盖
     * @param path String 相对路径
     * @param text String 需要写入的字符串
     * @param charset Charset 字符集,默认UTF8
     * @return String? 返回绝对路径如果写入成功()
     * @example writeCache("folder1/cache1.txt","This is a example")
     * @example return /sdcard/Android/data/{package}/cache/folder1/cache1.txt
     */
    fun write(
        path: String,
        text: String,
        charset: Charset = Charsets.UTF_8
    ): String? {
        val folder = getAppCacheFolder()
        if (folder?.exists() == false) folder.mkdir()
        if (folder?.isDirectory == true) {
            val file = File(folder, path)
            file.writeText(text, charset)
            return file.absolutePath
        }
        return null
    }

    /**
     * write 写入指定字符串到指定文件
     * @param path 指定文件路径
     * @param text 需写入的字符串
     * @param charset 字符集
     * @return
     */
    fun write(path: String, byteArray: ByteArray): String? {
        val folder = getAppCacheFolder()
        if (folder?.isDirectory == true) {
            val file = File(folder, path)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            file.writeBytes(byteArray)
            return file.absolutePath
        }
        return null
    }

    /**
     * readText 从指定文件中读取字符串
     *
     * @param src 文件路径
     * @param charset 字符集
     * @return
     */
    fun read(src: String): ByteArray {
        val folder = getAppCacheFolder()
        if (folder != null) {
            if (!folder.exists()) folder.mkdirs()
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            if (file.exists())
                return file.readBytes()
        }
        return ByteArray(0)
    }

    /**
     * exist 判断文件是否存在且为文件
     */
    fun exist(src: String): Boolean {
        val folder = getAppCacheFolder()
        if (folder != null) {
            if (!folder.exists()) folder.mkdirs()
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            return file.isFile
        }
        return false
    }

    /**
     * getFile 相对路径转 File?
     *
     * @param src 文件路径
     * @return
     */
    fun getFile(src: String): File? {
        val folder = getAppCacheFolder()
        if (folder != null) {
            if (!folder.exists()) folder.mkdirs()
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            if (file.exists())
                return file
        }
        return null
    }

    /**
     * getUri 相对路径转 Uri
     *
     * @param src 文件路径
     * @return
     */
    fun getUri(src: String): String? {
        val folder = getAppCacheFolder()
        if (folder != null) {
            if (!folder.exists()) folder.mkdirs()
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, src)
            if (file.parentFile?.exists() == false) {
                file.parentFile?.mkdirs()
            }
            if (file.exists())
                return "file://" + file.absolutePath
        }
        return null
    }

    /**
     * 清除指定缓存文件夹下的所有缓存,默认清除所有缓存,指定为文件时,直接删除文件
     * @param child String 需要清除缓存的子缓存文件夹,默认为整个缓存文件夹
     * @return
     */
    fun clear(child: String = "") {
        val folder = getAppCacheFolder()
        if (folder?.exists() == false) folder.mkdir()
        if (child == "") {
            folder?.deleteAll()
            return
        }
        if (folder?.isDirectory == true) {
            val file = File(folder, child)
            file.deleteAll()
            return
        }
    }
}