package sv.edu.ufg.fis.amb.navarretesibrian_ns100221_texto

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    private lateinit var boton: Button
    private lateinit var texto: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton = findViewById(R.id.btn_guardar)
        texto = findViewById(R.id.txt_data)

        escrituraArchivosAlmacenamientoInterno(this, "Archivo_almacenamiento_interno.txt", "Este es un contenido de almacenamiento Interno")
        escrituraArchivosAlmacenamientoExterno(this, "Archivo_almacenamiento_externo.txt", "Este es un contenido de almacenamiento Externo")

        boton.setOnClickListener {
            escrituraArchivosAlmacenamientoInterno(this, "archivo_con_texto.txt", texto.text.toString())
        }
    }

    private fun escrituraArchivosAlmacenamientoInterno(context: Context, filename: String, content: String) {
        val filePath = context.filesDir.absolutePath + "/$filename"
        val file = File(filePath)

        try {
            file.writeText(content)
            Log.v("Escritura en almacenamiento Interno", "RUTA: '$filePath'")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun escrituraArchivosAlmacenamientoExterno(context: Context, filename: String, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/$filename"
            val file = File(filePath)

            try {
                file.writeText(content)
                Log.v("Escritura Archivos", "Ruta: '$filePath'")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val filePath = context.getExternalFilesDir(null)?.absolutePath + "/$filename"
            val file = File(filePath)

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    file.writeText(content)
                    Log.v("Escritura Archivos", "Ruta: '$filePath'")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso concedido, puedes escribir en el almacenamiento externo
            } else {
                // Permiso denegado, no puedes escribir en el almacenamiento externo
            }
        }
    }
}
