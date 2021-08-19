package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZooKeeperException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperException.class);


    public ZooKeeperException(String s) {
        logger.error(s);
    }
}
