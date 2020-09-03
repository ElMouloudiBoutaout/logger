package org.moneyservice.loggers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;



public class MoneyServiceLoggerFactoryConfigurationUtils {

    private  static final String MAX_FILES = String.valueOf(Integer.MAX_VALUE);
    private  static final String DEFAULT_MAX_FILE_SIZE_MB = "25 MB";
    private  static final String APPENDER_NAME_PREFIX = "Appender";
    private  static final String TIME_BASED_TRIGGER_VALUE = "5";

    private  String path ;
    private  String rolledFilesPath;
    private String machineName;

    private ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    public MoneyServiceLoggerFactoryConfigurationUtils(String path, String rolledFilesPath,String machineName) {
        this.path = path;
        this.rolledFilesPath = rolledFilesPath;
        this.machineName = machineName;
    }

    private MoneyServiceLoggerFactoryConfigurationUtils() { }

    protected void initializeLogger(String filename, String loggerName, String maxFileSizePolicySize, String rolloverStrategyMaxFiles, String timeBasedTriggerValue, String... levelFilters){
        initializeTheLogger(filename,loggerName,maxFileSizePolicySize,rolloverStrategyMaxFiles,timeBasedTriggerValue,null);

    }

    private void initializeTheLogger(String filename,String loggerName, String maxFileSizePolicySize,String rolloverStrategyMaxFiles,String timeBasedTriggerValue,String levelFilter) {

        String defaultSizeBasedTriggeringPolicySize = maxFileSizePolicySize==null || maxFileSizePolicySize.isEmpty() ? DEFAULT_MAX_FILE_SIZE_MB : maxFileSizePolicySize ;
        String defaultRolloverStrategyMax = rolloverStrategyMaxFiles==null || rolloverStrategyMaxFiles.isEmpty() ? MAX_FILES : rolloverStrategyMaxFiles ;
        String defaultTimeBasedTriggerValue = timeBasedTriggerValue==null || timeBasedTriggerValue.isEmpty() ? TIME_BASED_TRIGGER_VALUE : timeBasedTriggerValue ;

        LoggerComponentBuilder customLogger = builder.newLogger(loggerName,Level.INFO);

        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %m%n");

        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", defaultSizeBasedTriggeringPolicySize))
                .addComponent(builder.newComponent("TimeBasedTriggeringPolicy").addAttribute("interval",defaultTimeBasedTriggerValue).addAttribute("modulate","true"));

        /*ComponentBuilder rollOverStrategy = builder.newComponent("DefaultRolloverStrategy")
                                                    .addAttribute("max",defaultRolloverStrategyMax);*/

        AppenderComponentBuilder appenderBuilder = builder.newAppender(filename.concat(APPENDER_NAME_PREFIX), "RollingFile")
                .addAttribute("fileName", new StringBuilder().append(getPath()).append(filename).append("-logs/").append(filename).toString().concat(".log"))
                .addAttribute("filePattern",new StringBuilder().append(getRolledFilesPath()).append(filename).append("-logs/")
                        .append("$${date:yyyy-MM}/$${date:dd}/").append(filename)
                        .append("-").append(machineName)
                        .append("-%d{HH-mm}-%i.log").toString())
                .add(layoutBuilder)
                .addComponent(triggeringPolicy);
        //  .addComponent(rollOverStrategy);

        builder.add(appenderBuilder);
        customLogger.add(builder.newAppenderRef(filename.concat(APPENDER_NAME_PREFIX)));
        customLogger.addAttribute( "additivity", false );
        builder.add(customLogger);

        Configurator.reconfigure(builder.build());

    }

    private String getPath(){
        return this.path==null ? "" : this.path.concat("/");
    }

    private String getRolledFilesPath(){ return this.rolledFilesPath==null ? "" : this.rolledFilesPath.concat("/"); }

}
