/**
 * This class is generated by jOOQ
 */
package ru.aegorova.friendslist;


import javax.annotation.Generated;

import ru.aegorova.friendslist.tables.FlywaySchemaHistory;
import ru.aegorova.friendslist.tables.Friend;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = ru.aegorova.friendslist.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.friend</code>.
     */
    public static final Friend FRIEND = ru.aegorova.friendslist.tables.Friend.FRIEND;
}
