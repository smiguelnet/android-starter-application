package net.smiguel.app.config.networking;

import java.util.ArrayList;
import java.util.List;

public abstract class DataMapper<TR, TL> {

    /**
     * Converts to Local Type
     *
     * @param item - Remote Type
     * @return
     */
    public abstract TL map(TR item);

    /**
     * Converts to Remote Type
     *
     * @param item - Type Local
     * @return
     */
    public abstract TR reverseMap(TL item);

    /**
     * Converts to Local List
     *
     * @param items
     * @return
     */
    public List<TL> map(List<TR> items) {
        if (items == null || items.size() == 0) {
            return new ArrayList<>();
        }
        List<TL> list = new ArrayList<>(items.size());
        for (TR item : items) {
            list.add(map(item));
        }
        return list;
    }

    /**
     * Converts to Remote List
     *
     * @param items - Type Local
     * @return
     */
    public List<TR> reverseMap(List<TL> items) {
        if (items == null || items.size() == 0) {
            return new ArrayList<>();
        }
        List<TR> list = new ArrayList<>(items.size());
        for (TL item : items) {
            list.add(reverseMap(item));
        }
        return list;
    }
}
