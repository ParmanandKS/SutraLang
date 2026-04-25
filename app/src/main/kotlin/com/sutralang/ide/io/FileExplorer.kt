package com.sutralang.ide.io

import android.content.Context
import com.sutralang.ide.utils.FileUtils
import java.io.File

data class FileItem(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val lastModified: Long = 0
)

class FileExplorer(private val context: Context) {
    
    private var currentDirectory: File = context.filesDir
    
    interface FileExplorerCallback {
        fun onFileSelected(fileItem: FileItem)
        fun onDirectoryChanged(newDir: File)
        fun onError(message: String)
    }
    
    var callback: FileExplorerCallback? = null

    fun listFiles(): List<FileItem> {
        val files = currentDirectory.listFiles() ?: return emptyList()
        return files.map { file ->
            FileItem(
                name = file.name,
                path = file.absolutePath,
                isDirectory = file.isDirectory,
                size = file.length(),
                lastModified = file.lastModified()
            )
        }.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
    }

    fun navigateTo(directory: File) {
        if (directory.isDirectory) {
            currentDirectory = directory
            callback?.onDirectoryChanged(directory)
        } else {
            callback?.onError("Not a directory: ${directory.name}")
        }
    }

    fun navigateUp(): Boolean {
        val parent = currentDirectory.parentFile
        if (parent != null && parent.absolutePath.startsWith(context.filesDir.absolutePath)) {
            navigateTo(parent)
            return true
        }
        return false
    }

    fun getCurrentPath(): String {
        return currentDirectory.absolutePath
    }
    
    fun openFile(fileItem: FileItem): String? {
        if (fileItem.isDirectory) {
            navigateTo(File(fileItem.path))
            return null
        } else {
            return FileUtils.readFile(context, fileItem.name)
        }
    }
    
    fun deleteFile(fileItem: FileItem): Boolean {
        return FileUtils.deleteFile(context, fileItem.name)
    }
    
    fun renameFile(fileItem: FileItem, newName: String): Boolean {
        return FileUtils.renameFile(context, fileItem.name, newName)
    }
}
