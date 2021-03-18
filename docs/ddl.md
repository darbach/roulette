## Data Definition Language (DDL)
```sqlite
CREATE TABLE IF NOT EXISTS `Spin`
(
    `spin_id`   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `timestamp` INTEGER                           NOT NULL,
    `value`     TEXT
);

CREATE INDEX IF NOT EXISTS `index_Spin_timestamp` ON `Spin` (`timestamp`);

CREATE INDEX IF NOT EXISTS `index_Spin_value` ON `Spin` (`value`);

CREATE TABLE IF NOT EXISTS `Wager`
(
    `wager_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `spin_id`  INTEGER                           NOT NULL,
    `color`    INTEGER,
    `value`    TEXT,
    `row`      INTEGER                           NOT NULL,
    `amount`   INTEGER                           NOT NULL,
    FOREIGN KEY (`spin_id`) REFERENCES `Spin` (`spin_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `index_Wager_spin_id` ON `Wager` (`spin_id`);

CREATE VIEW `ValueCount` AS
SELECT value,
       COUNT(*)                                                                 AS `count`,
       (100.0 * COUNT(*) / (SELECT COUNT(*) FROM spin WHERE value IS NOT NULL)) AS `percent`
FROM Spin
WHERE value IS NOT NULL
GROUP BY value
ORDER BY CASE value WHEN '00' THEN -1 ELSE CAST(value AS INTEGER) END;
```

[`ddl.sql`](sql/ddl.sql)