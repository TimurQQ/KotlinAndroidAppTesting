package ilyasov.kotlinandroidapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {
     var pattern:String = ""
         set(value) {
             Log.d("SETTER", "Set Value")
             field = "Sum of values between 2 and $value equals "
        }
        get() {
            Log.d("GETTER", "Get Value")
            return field
        }
    lateinit var textResult: TextView
    lateinit var callable: Callable<Long>
    lateinit var future: FutureTask<*>
    private var x:Long by PosValueDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // months Task
        val month = 3
        val season:String
        season = when (month) {
            1, 2, 12 -> "Зимушка-зима"
            3, 4, 5 -> "Весна"
            6, 7, 8 -> "Лето"
            9, 10, 11 -> "Осень"
            else -> "Не знаю"
        }

        Log.d("SEASON", season);
        //
        // GetDataFromCallable Task
        textResult = findViewById(R.id.sumResult)
        initCountUpClick()

        val numberText: TextView = findViewById(R.id.editTextNumber)
        numberText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {}
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                x = s.toString().toLong()
                pattern = s.toString()
                textResult.text = pattern
            }
            override fun afterTextChanged(s: Editable) {}
        })
        //
    }

    private fun initCountUpClick() {
        val button: Button = findViewById(R.id.countUpButton)
        button.setOnClickListener(object: View.OnClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(v: View) {
                val summator = Thread(object: Runnable {
                    override fun run() {
                        synchronized(this) {
                            callable = getDataFromCallable()
                            Log.d("LATEINIT", ::future.isInitialized.toString())
                            future = FutureTask(callable)
                            Log.d("LATEINIT", ::future.isInitialized.toString())
                            Thread(future).start()
                            try {
                                textResult.text = pattern + future.get()
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                })
                summator.start()
            }
        })
    }

    private fun getDataFromCallable(): Callable<Long> {
        return object : Callable<Long> {
            @Throws(Exception::class)
            override fun call():Long {
                var result:Long = 0
                for (i in 2..x) {
                    result += i.toLong()
                }
                return result
            }
        }
    }
}