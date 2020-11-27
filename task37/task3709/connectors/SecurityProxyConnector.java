package com.javarush.task.task37.task3709.connectors;

import com.javarush.task.task37.task3709.security.SecurityChecker;

public class SecurityProxyConnector implements Connector {
    private SecurityChecker checker;
    private SimpleConnector connector;

    public SecurityProxyConnector(String s) {
        connector = new SimpleConnector(s);
    }

    @Override
    public void connect() {
        if (checker.performSecurityCheck()) {
          connector.connect();
        }
    }
}
