/*
 * ~ Copyright (c) 2021
 * ~ Dev : Amir Bahador , Amiri
 * ~ City : Iran / Abadan
 * ~ time & date : 5/4/21 10:06 PM
 * ~ email : abadan918@gmail.com
 */

package ir.atgroup.linkshorter.utils.DTCenter;

public class Row {

    private final String type;
    private final boolean primary;
    private final boolean autoincrement;
    private boolean NotNull;

    public Row(String type, boolean primary, boolean autoincrement, boolean not_null) {
        this.type = type;
        this.primary = primary;
        this.autoincrement = autoincrement;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public boolean isNotNull() {
        return NotNull;
    }
}
