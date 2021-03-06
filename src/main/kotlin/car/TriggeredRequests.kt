package car

import car.cockpit.engine.EMPTY_STRING
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun launchRequest(url:String) = CoroutineScope(Dispatchers.IO).launch { doRequest(url) }

private fun doRequest(url: String): String {
    val con: HttpURLConnection?
    val urlGet: URL
    val requestInputStream: InputStream?
    val sb =  StringBuilder()
    try {
        urlGet = URL(url)
        con = urlGet.openConnection() as HttpURLConnection
        requestInputStream = con.run {
            readTimeout = 500 /* milliseconds */
            connectTimeout = 500 /* milliseconds */
            requestMethod = "GET"
            doInput = true
            // Start the query
            connect()
            inputStream
        }

        val bufferReader = BufferedReader(InputStreamReader(requestInputStream), 4096)
        var line: String?

        line = bufferReader.readLine()
        while (line != null) {
            sb.append(line)
            line = bufferReader.readLine()
        }
        bufferReader.close()
    } catch (e: IOException) {
        //handle the exception !
        sb.append(e.message ?: EMPTY_STRING)
    }
    return sb.toString()
}