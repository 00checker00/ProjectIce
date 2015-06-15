package de.project.ice.editor.editors;

import com.kotcrab.vis.ui.widget.NumberSelector;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.text.ParseException;

public abstract class NumberEditor<T extends Number> extends ValueEditor<T> {
    private VisTextField valueField;

    @Override
    protected void updateValue() {
        valueField.setText(String.valueOf(value));
    }

    @Override
    protected void createUi() {
        valueField = new VisTextField(String.valueOf(value));
        add(valueField);
    }

    @Override
    public void act(float delta) {
        try {
            value = parseValue();
        } catch (NumberFormatException ignore) {}

        super.act(delta);
    }

    protected T parseValue() {
        try {
            return parse(valueField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract T parse(String value) throws ParseException;

    public static class FloatEditor extends NumberEditor<Float> {
        @Override
        protected Float parse(String value) throws ParseException {
            return Float.parseFloat(value);
        }
    }

    public static class DoubleEditor extends NumberEditor<Double> {
        @Override
        protected Double parse(String value) throws ParseException {
            return Double.parseDouble(value);
        }
    }

    public static class LongEditor extends NumberEditor<Long> {

        @Override
        protected Long parse(String value) throws ParseException {
            return Long.parseLong(value);
        }
    }

    public static class IntegerEditor extends NumberEditor<Integer> {
        private NumberSelector numberSelector;

        @Override
        protected void createUi() {
            numberSelector = new NumberSelector(null, value, 0, Integer.MAX_VALUE);
            add(numberSelector);
        }

        @Override
        protected Integer parseValue() {
            return numberSelector.getValue();
        }

        @Override
        protected void updateValue() {
            numberSelector.setValue(value);
        }

        @Override
        protected Integer parse(String value) throws ParseException {
            return Integer.parseInt(value);
        }
    }

    public static class ShortEditor extends NumberEditor<Short> {
        @Override
        protected Short parse(String value) throws ParseException {
            return Short.parseShort(value);
        }
    }
}
