package de.project.ice.editor.editors

import java.text.ParseException

abstract class NumberEditor<T : Number> : ValueEditor<T>() {
    private var valueField: VisTextField? = null

    override fun updateValue() {
        valueField!!.text = value.toString()
    }

    override fun createUi() {
        valueField = VisTextField(value.toString())
        add<VisTextField>(valueField)
    }

    override fun act(delta: Float) {
        try {
            value = parseValue()
        } catch (ignore: NumberFormatException) {
        }

        super.act(delta)
    }

    protected open fun parseValue(): T? {
        try {
            return parse(valueField!!.text)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(ParseException::class)
    protected abstract fun parse(value: String): T

    class FloatEditor : NumberEditor<Float>() {
        @Throws(ParseException::class)
        override fun parse(value: String): Float {
            return java.lang.Float.parseFloat(value)
        }
    }

    class DoubleEditor : NumberEditor<Double>() {
        @Throws(ParseException::class)
        override fun parse(value: String): Double {
            return java.lang.Double.parseDouble(value)
        }
    }

    class LongEditor : NumberEditor<Long>() {

        @Throws(ParseException::class)
        override fun parse(value: String): Long {
            return java.lang.Long.parseLong(value)
        }
    }

    class IntegerEditor : NumberEditor<Int>() {
        private var numberSelector: NumberSelector? = null

        override fun createUi() {
            numberSelector = NumberSelector(null, value!!.toFloat(), 0f, Integer.MAX_VALUE.toFloat(), 1.0f, 0)
            add<NumberSelector>(numberSelector)
        }

        override fun parseValue(): Int? {
            return numberSelector!!.value.toInt()
        }

        override fun updateValue() {
            numberSelector!!.value = value!!.toFloat()
        }

        @Throws(ParseException::class)
        override fun parse(value: String): Int {
            return Integer.parseInt(value)
        }
    }

    class ShortEditor : NumberEditor<Short>() {
        @Throws(ParseException::class)
        override fun parse(value: String): Short {
            return java.lang.Short.parseShort(value)
        }
    }
}
