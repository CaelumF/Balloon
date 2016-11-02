//Get to your courtyard!
package com.gmail.caelum119.utils.files

import java.awt.image.BufferedImage
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Created by Caelum on 8/8/14.
 */
//Todo: Rewrite again... In java. Apparently compiled Kotlin still requires a library, which may not be ideal if I ever expect other people to use Util
//Do I ever expect people to ever use Util?

object FileIO {

  /**
   * @return The path the program is running in.
   */
  @JvmStatic
  public val localPath = File("").absolutePath

//  @Throws(FileNotFoundException::class, IOException::class)
//  fun readLines(sFileName: String): Array<String> {
//    val lines = ArrayList<String>()
//    var br: BufferedReader? = null
//
//    try {
//      br = BufferedReader(FileReader(sFileName))
//
//      var line: String
//      br.readLine().forEach { line -> lines.add(line) }
//
//      br.close()
//    } catch (e: IOException) {
//      throw e
//      // redundant
//      //return (String[])lines.toArray();
//    } finally {
//      try {
//        if (br != null)
//          br.close()
//      } catch (e: IOException) {
//        throw e
//      }
//
//    }
//
//    return StringOperations.convertList(lines)
//  }

  @JvmStatic
  @Throws(FileNotFoundException::class, IOException::class)
  fun readLines(fileDir: String): Array<String> {
    val lines = ArrayList<String>()
    var br = BufferedReader(FileReader(fileDir))

    do{
      var line: String? = br.readLine()
      line?.let { lines.add(it) }
    }while(line != null)
    br.close()

    return lines.toTypedArray()
  }

  @JvmStatic
  fun makeDirRecursive(dir : String){
    val cd = StringBuilder()
    for (s in dir.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
      cd.append(s + "/")
      val created = File(cd.toString()).mkdir()
    }
  }

  @JvmStatic
  fun getUserDataPath() : String{
    val OS : String = System.getProperty("os.name").toUpperCase()
    if(OS.contains("WIN")){
      return System.getenv("AppData")
    }else{
      return System.getProperty("user.home");
    }
  }
  //TODO: This
//  @JvmStatic
//  public fun backup(filepath: String, backups: Int){
//    val path : Path = File(filepath).toPath()
//    Files.move(path,)
//  }

  @JvmStatic
  @Throws(IOException::class, FileNotFoundException::class)
  fun readText(fileName: String): String {
    var br: BufferedReader? = null
    try {
      br = BufferedReader(FileReader(fileName))
    } catch (e1: FileNotFoundException) {
      throw e1
    }

    try {
      val sb = StringBuilder()
      var line: String?

      line = br!!.readLine()

      while (line != null) {
        sb.append(line + "\n")
        line = br.readLine()
      }
      br.close()
      return sb.toString()

    } catch (e: IOException) {
      throw e
    } finally {
      try {
        br!!.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  private var written: Long = 0
  @JvmStatic
  fun writeZip(path: String, names: String, data: Array<ByteArray>) {
    try {
      val fos = FileOutputStream(path)
      val zos = ZipOutputStream(fos)
      print("length" + data.size)

      for (aData in data) {
        val ze = ZipEntry(names + written + ".txt".toString())
        zos.putNextEntry(ze)
        zos.write(aData)
        zos.closeEntry()

        written++
      }
      zos.close()
    } catch (ex: IOException) {
      ex.printStackTrace()
    }
  }

  @JvmStatic
  fun writeZip(path: String, vararg bi: BufferedImage) {
    for (byteImage in bi) {
      val baos = ByteArrayOutputStream()
    }
  }

  @JvmStatic
  fun writeText(path: String, text: Any) {
    var fos: FileOutputStream? = null
    try {
      fos = FileOutputStream(path)
      fos.write(text.toString().toByteArray())
      fos.flush()
      fos.close()
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  @JvmStatic
  fun listFiles(folder: File): List<File> {
    val files = ArrayList<File>()

    for (file in folder.listFiles()!!) {
      if (file.isDirectory) {
        listFiles(file)
      } else {
        println(file.name)
        files.add(file)
      }
    }
    return files
  }

  /**
   * Remove the file extension of [filePath]
   * Example:
   *"C:\Program Files (x86)\Pale Moon\palemoon.exe"
   * becomes
   * "C:\Program Files (x86)\Pale Moon\palemoon"
   */
  @JvmStatic
  fun removeExtension(filePath: String): String {
    return filePath.replaceAfterLast(".", "")
  }
}
