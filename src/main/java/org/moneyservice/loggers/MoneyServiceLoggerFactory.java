package org.moneyservice.loggers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MoneyServiceLoggerFactory {

    private static Map<String, Logger> loggers = new HashMap<>();
    private static MoneyServiceLoggerFactoryConfigurationUtils moneyServiceLoggerFactoryConfiguration;

    public MoneyServiceLoggerFactory(String path, String rolledFilesPath,String machineName) {
        moneyServiceLoggerFactoryConfiguration = new MoneyServiceLoggerFactoryConfigurationUtils(path,rolledFilesPath,machineName);
    }

    private MoneyServiceLoggerFactory() {
    }


    public synchronized Logger getLoggerInstance(String loggerName, String filename)
    {
        if(Objects.isNull(loggers.get(loggerName))){
            moneyServiceLoggerFactoryConfiguration.initializeLogger(filename,loggerName,null,null,null);
            loggers.put(loggerName,LogManager.getLogger(loggerName));
        }
        Objects.requireNonNull(loggers.get(loggerName));
        return loggers.get(loggerName);
    }

    public synchronized Logger getLoggerInstance(String loggerName)
    {
        if(Objects.isNull(loggers.get(loggerName))){
            moneyServiceLoggerFactoryConfiguration.initializeLogger(loggerName.toLowerCase(),loggerName,null,null,null);
            loggers.put(loggerName,LogManager.getLogger(loggerName));
        }
        Objects.requireNonNull(loggers.get(loggerName));
        return loggers.get(loggerName);
    }

    public synchronized  Logger getLoggerInstance(String loggerName,String filename,String fileMaxSize,String rolloverStrategyMaxFiles,String timeBasedTriggerValue)
    {
        if(Objects.isNull(loggers.get(loggerName))){
            moneyServiceLoggerFactoryConfiguration.initializeLogger(filename,loggerName,fileMaxSize,rolloverStrategyMaxFiles,timeBasedTriggerValue);
            loggers.put(loggerName,LogManager.getLogger(loggerName));
        }
        Objects.requireNonNull(loggers.get(loggerName));
        return loggers.get(loggerName);
    }

    public synchronized  Logger getLoggerInstance(String loggerName,String fileMaxSize,String rolloverStrategyMaxFiles,String timeBasedTriggerValue)
    {
        if(Objects.isNull(loggers.get(loggerName))){
            moneyServiceLoggerFactoryConfiguration.initializeLogger(loggerName.toLowerCase(),loggerName,fileMaxSize,rolloverStrategyMaxFiles,timeBasedTriggerValue);
            loggers.put(loggerName,LogManager.getLogger(loggerName));
        }
        Objects.requireNonNull(loggers.get(loggerName));
        return loggers.get(loggerName);
    }

}
