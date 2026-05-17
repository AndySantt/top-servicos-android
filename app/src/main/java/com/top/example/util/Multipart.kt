package com.top.example.net

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import kotlin.math.max


fun contentUriToSmartPart(
resolver: ContentResolver,
uri: Uri,
fieldName: String = "file",
compressThresholdBytes: Long = 400 * 1024L, // 400 KB
maxSizePx: Int = 1280,                      // lado maior alvo
quality: Int = 82                           // qualidade JPEG
): MultipartBody.Part {

// ---- meta (nome, tamanho, mime) ----
var displayName: String? = null
var size: Long? = null
resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)
?.use { c: Cursor ->
if (c.moveToFirst()) {
val iName = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
if (iName != -1) displayName = c.getString(iName)
val iSize = c.getColumnIndex(OpenableColumns.SIZE)
if (iSize != -1) size = c.getLong(iSize).takeIf { it >= 0 }
}
}

val mimeStr = resolver.getType(uri) ?: "image/jpeg"
val mime: MediaType? = mimeStr.toMediaTypeOrNull()
val fileName = displayName ?: when (mime?.toString()) {
"image/png"  -> "avatar.png"
"image/webp" -> "avatar.webp"
else         -> "avatar.jpg"
}

val isImage = mimeStr.startsWith("image/")
val mustCompress = isImage && (size ?: Long.MAX_VALUE) > compressThresholdBytes

Log.d("AVATAR", "smartPart: name=$fileName, mime=$mimeStr, size=${size ?: "?"}, compress=$mustCompress, uri=$uri")

    return if (mustCompress) {
        val jpegBytes = compressImageFromResolver(
            resolver = resolver,
            uri = uri,
            maxSizePx = maxSizePx,
            quality = quality
        )
        val body = jpegBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(fieldName, ensureJpegName(fileName), body)

    } else {
        val bytes = resolver.openInputStream(uri)!!.readBytes()
        val body = bytes.toRequestBody(mime)

        MultipartBody.Part.createFormData(
            fieldName,
            fileName,
            body
        )
    }
} // 👈 ESSE CARA ESTAVA FALTANDO

private fun ensureJpegName(name: String): String =
if (name.lowercase().endsWith(".jpg") || name.lowercase().endsWith(".jpeg")) name
else name.substringBeforeLast('.', name) + ".jpg"

/**
 * Decodifica com inSampleSize, corrige EXIF orientation e comprime para JPEG.
*/
private fun compressImageFromResolver(
resolver: ContentResolver,
uri: Uri,
maxSizePx: Int,
quality: Int
): ByteArray {
// 1) ler bounds para calcular inSampleSize
val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

var inSample = 1
var halfW = bounds.outWidth / 2
var halfH = bounds.outHeight / 2
while ((halfW / inSample) >= maxSizePx || (halfH / inSample) >= maxSizePx) inSample *= 2

// 2) decodificar com downsample
val opts = BitmapFactory.Options().apply { inSampleSize = inSample }
val bmp = resolver.openInputStream(uri)!!.use {
BitmapFactory.decodeStream(it, null, opts)!!
}

// 3) scale final para caber em maxSizePx, preservando proporção
val (nw, nh) = if (max(bmp.width, bmp.height) > maxSizePx) {
val r = bmp.width.toFloat() / bmp.height
if (bmp.width >= bmp.height) maxSizePx to (maxSizePx / r).toInt()
else (maxSizePx * r).toInt() to maxSizePx
} else bmp.width to bmp.height

var outBmp = if (nw != bmp.width || nh != bmp.height)
Bitmap.createScaledBitmap(bmp, nw, nh, true) else bmp

// 4) corrigir EXIF orientation (se existir)
try {
resolver.openInputStream(uri)?.use { input ->
val exif = ExifInterface(input)
val orientation = exif.getAttributeInt(
ExifInterface.TAG_ORIENTATION,
ExifInterface.ORIENTATION_NORMAL
)
val matrix = Matrix()
when (orientation) {
ExifInterface.ORIENTATION_ROTATE_90  -> matrix.postRotate(90f)
ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
ExifInterface.ORIENTATION_FLIP_VERTICAL   -> matrix.preScale(1f, -1f)
}
if (!matrix.isIdentity) {
val rotated = Bitmap.createBitmap(outBmp, 0, 0, outBmp.width, outBmp.height, matrix, true)
if (outBmp !== bmp) outBmp.recycle()
outBmp = rotated
}
}
} catch (t: Throwable) {
Log.w("AVATAR", "EXIF orientation skip: ${t.message}")
} finally {
if (bmp !== outBmp) bmp.recycle()
}

// 5) comprimir para JPEG
val bos = ByteArrayOutputStream()
outBmp.compress(Bitmap.CompressFormat.JPEG, quality, bos)
outBmp.recycle()
return bos.toByteArray()
}
