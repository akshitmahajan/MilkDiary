package com.manaksh.milkdiary.model;

/**
 * Created by akshmaha on 12/1/2015.
 * <p/>
 * Type is the hashtag
 * date is the date_selected
 * value is the selected value
 * state is hit or miss
 */
public class DataBean {
    private String _type = null;
    private String _date = null;
    private String _value = null;
    private String _state = null;

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }

    public String get_state() {
        return _state;
    }

    public void set_state(String _state) {
        this._state = _state;
    }
}
