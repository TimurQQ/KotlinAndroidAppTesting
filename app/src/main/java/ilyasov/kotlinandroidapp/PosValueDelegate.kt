package ilyasov.kotlinandroidapp

import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PosValueDelegate : ReadWriteProperty<Any?, Long> {
    private var posValue: Long = 0

    override fun getValue(
            thisRef: Any?,
            property: KProperty<*>
    ): Long {
        Log.d("DELEGATE", "Get value")
        return posValue
    }

    override fun setValue(
            thisRef: Any?,
            property: KProperty<*>, value: Long
    ) {
        Log.d("DELEGATE", "Set value")
        posValue = if (value > 0) value else posValue
    }
}