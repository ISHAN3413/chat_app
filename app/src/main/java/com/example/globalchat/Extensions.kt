package com.example.globalchat

import android.content.Context
import android.net.Uri
import okio.IOException
import kotlin.jvm.Throws

@Throws(IOException::class)
fun Uri.uriToByteArray(context: Context) =
    context.contentResolver.openInputStream(this)?.use {it.buffered().readBytes()}