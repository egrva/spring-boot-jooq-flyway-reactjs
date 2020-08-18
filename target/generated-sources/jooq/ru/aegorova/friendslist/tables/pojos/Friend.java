/**
 * This class is generated by jOOQ
 */
package ru.aegorova.friendslist.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Friend implements Serializable {

    private static final long serialVersionUID = 982340708;

    private final Integer id;
    private final String  name;

    public Friend(Friend value) {
        this.id = value.id;
        this.name = value.name;
    }

    public Friend(
        Integer id,
        String  name
    ) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Friend (");

        sb.append(id);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }
}
