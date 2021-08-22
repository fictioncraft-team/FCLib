package fictioncraft.wintersteve25.fclib.common.base;

import java.util.ArrayList;

public class FCList<T> extends ArrayList<T> {

    public FCList<T> addContent(T e) {
        super.add(e);
        return this;
    }
}
